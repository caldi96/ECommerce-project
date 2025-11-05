package io.hhplus.ECommerce.ECommerce_project.order.application;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.common.exception.*;
import io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity.Coupon;
import io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity.UserCoupon;
import io.hhplus.ECommerce.ECommerce_project.coupon.domain.repository.CouponRepository;
import io.hhplus.ECommerce.ECommerce_project.coupon.domain.repository.UserCouponRepository;
import io.hhplus.ECommerce.ECommerce_project.order.application.command.CreateOrderFromCartCommand;
import io.hhplus.ECommerce.ECommerce_project.order.domain.constants.ShippingPolicy;
import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.OrderItem;
import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.Orders;
import io.hhplus.ECommerce.ECommerce_project.order.domain.repository.OrderItemRepository;
import io.hhplus.ECommerce.ECommerce_project.order.domain.repository.OrderRepository;
import io.hhplus.ECommerce.ECommerce_project.order.presentation.response.CreateOrderFromCartResponse;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.entity.Payment;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentMethod;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.repository.PaymentRepository;
import io.hhplus.ECommerce.ECommerce_project.point.domain.entity.Point;
import io.hhplus.ECommerce.ECommerce_project.point.domain.repository.PointRepository;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.user.domain.entity.User;
import io.hhplus.ECommerce.ECommerce_project.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderFromCartUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final PointRepository pointRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public CreateOrderFromCartResponse execute(CreateOrderFromCartCommand command) {
        // 1. 사용자 확인
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // 2. 장바구니 아이템 조회 (cartItemIds)
        List<Cart> cartList = command.cartItemIds().stream()
                .map(cartId -> {
                    Cart cart = cartRepository.findById(cartId)
                            .orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND));

                    // 유저의 카트인지 확인
                    if (!cart.isSameUser(command.userId())) {
                        throw new CartException(ErrorCode.CART_ACCESS_DENIED);
                    }

                    return cart;
                })
                .toList();

        // 3. 각 장바구니 아이템에 대해 상품 검증 및 재고 처리
        List<Product> productList = new ArrayList<>();
        for (Cart cart : cartList) {
            // 상품 조회
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

            // 상품 주문 가능 여부 확인 (활성화, 재고)
            if (!product.canOrder(cart.getQuantity())) {
                throw new OrderException(ErrorCode.ORDER_PRODUCT_CANNOT_BE_ORDERED);
            }

            // 재고 차감 (동시성 처리 필요!)
            product.decreaseStock(cart.getQuantity());

            // 판매량 증가
            product.increaseSoldCount(cart.getQuantity());

            // 변경사항 저장
            productRepository.save(product);

            productList.add(product);
        }

        // 4. 주문 금액 계산
        BigDecimal totalAmount = calculateTotalAmount(cartList, productList);

        // 5. 배송비 계산 (상수 클래스 사용)
        BigDecimal shippingFee = ShippingPolicy.calculateShippingFee(totalAmount);

        // 6. 쿠폰 처리
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (command.couponId() != null) {
            // 8-1. 사용자 쿠폰 조회
            UserCoupon userCoupon = userCouponRepository
                    .findByUserIdAndCouponId(command.userId(), command.couponId())
                    .orElseThrow(() -> new CouponException(ErrorCode.USER_COUPON_NOT_FOUND));

            // 8-2. 쿠폰 정보 조회
            Coupon coupon = couponRepository.findById(command.couponId())
                    .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));

            // 8-3. 쿠폰 유효성 검증 (활성화, 사용 가능 기간 확인)
            coupon.validateAvailability();

            // 8-4. 사용자 쿠폰 사용 가능 여부
            userCoupon.validateCanUse(coupon.getPerUserLimit());

            // 8-5. 할인 금액 계산 (최소 주문 금액 검증 포함)
            discountAmount = coupon.calculateDiscountAmount(totalAmount);

            // 8-6. 쿠폰 사용 처리
            userCoupon.use(coupon.getPerUserLimit());
            userCouponRepository.save(userCoupon);

            // 8-7. 쿠폰 사용 횟수 증가
            coupon.increaseUsageCount();
            couponRepository.save(coupon);
        }

        // 7. 포인트 사용 (있으면) - 임시 저장용 리스트
        BigDecimal pointAmount = BigDecimal.ZERO;
        List<Point> pointsToMarkAsUsed = new ArrayList<>();
        List<Point> pointUsageHistory = new ArrayList<>();

        if (command.pointAmount() != null
                && command.pointAmount().compareTo(BigDecimal.ZERO) > 0) {

            // 사용 가능한 포인트 조회
            List<Point> availablePoints = pointRepository.findAvailablePointsByUserId(command.userId());

            // 사용 가능한 포인트 합계 계산
            BigDecimal totalAvailablePoint = availablePoints.stream()
                    .map(Point::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 포인트 잔액 검증
            if (totalAvailablePoint.compareTo(command.pointAmount()) < 0) {
                throw new PointException(ErrorCode.POINT_INSUFFICIENT_POINT);
            }

            // 포인트 사용 처리 (선입선출) - Order ID가 필요하므로 임시 저장
            BigDecimal remainingPoint = command.pointAmount();
            for (Point point : availablePoints) {
                if (remainingPoint.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                BigDecimal pointToUse = point.getAmount().min(remainingPoint);

                // 나중에 사용 이력 생성을 위해 임시 저장
                pointUsageHistory.add(Point.use(
                    command.userId(),
                    null,  // orderId는 나중에 설정 (주문 생성 후)
                    pointToUse,
                    "주문 결제"
                ));

                // 기존 포인트는 사용됨 표시를 위해 임시 저장
                pointsToMarkAsUsed.add(point);

                remainingPoint = remainingPoint.subtract(pointToUse);
            }

            pointAmount = command.pointAmount();
        }

        // 8. Order 생성 (최종 금액은 Order에서 create할때 계산)
        Orders order = Orders.createOrder(
                command.userId(),
                totalAmount,            // 상품 총액
                shippingFee,            // 배송비
                command.couponId(),     // 쿠폰 ID
                discountAmount,         // 쿠폰 할인 금액
                pointAmount             // 포인트 사용 금액
        );

        // 9. 저장
        Orders savedOrder = orderRepository.save(order);

        // 10. 포인트 사용 이력 저장 (Order ID가 필요하므로 주문 생성 후 처리)
        if (!pointUsageHistory.isEmpty()) {
            for (int i = 0; i < pointUsageHistory.size(); i++) {
                Point usagePoint = pointUsageHistory.get(i);

                // Order ID가 null인 상태로 생성된 Point를 다시 생성 (orderId 포함)
                Point usedPoint = Point.use(
                    command.userId(),
                    savedOrder.getId(),  // 이제 orderId 설정 가능
                    usagePoint.getAmount(),
                    "주문 결제"
                );
                pointRepository.save(usedPoint);

                // 기존 포인트는 사용됨 표시
                Point originalPoint = pointsToMarkAsUsed.get(i);
                originalPoint.markAsUsed();
                pointRepository.save(originalPoint);
            }
        }

        // 11. OrderItem 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);
            Product product = productList.get(i);

            OrderItem orderItem = OrderItem.createOrderItem(
                    savedOrder.getId(),
                    product.getId(),
                    product.getName(),
                    cart.getQuantity(),
                    product.getPrice()
            );
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        // 12. 장바구니 삭제 (물리 삭제)
        for (Cart cart : cartList) {
            cartRepository.deleteById((cart.getId()));
        }

        // 13. 결제 정보 생성
        Payment payment = Payment.createPayment(
                savedOrder.getId(),
                savedOrder.getFinalAmount(),
                PaymentMethod.CARD
        );

        // 14. 결제 완료 처리 (내부에서 바로 완료)
        payment.complete();
        Payment savedPayment = paymentRepository.save(payment);

        // 15. 주문 상태를 PAID로 변경
        savedOrder.paid();
        orderRepository.save(savedOrder);

        return CreateOrderFromCartResponse.from(savedOrder, savedPayment, orderItems);
    }

    // 주문 금액 계산 헬퍼 메서드
    private BigDecimal calculateTotalAmount(List<Cart> cartList, List<Product> productList) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);
            Product product = productList.get(i);

            BigDecimal itemTotalAmount = product.getPrice()
                    .multiply(BigDecimal.valueOf(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotalAmount);
        }

        return totalAmount;
    }
}
