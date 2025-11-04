package io.hhplus.ECommerce.ECommerce_project.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 전체에서 사용하는 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== Common =====
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== User =====
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ID_REQUIRED("사용자 ID는 필수입니다.", HttpStatus.BAD_REQUEST),

    // ===== Product =====
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_REQUIRED("상품명은 필수입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_REQUIRED("가격은 필수입니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_INVALID("가격은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_INVALID("재고는 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK("재고가 부족합니다.", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_ACTIVE("이미 활성화된 상품입니다.", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_INACTIVE("이미 비활성화된 상품입니다.", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_SOLD_OUT("이미 품절 상태입니다.", HttpStatus.CONFLICT),
    PRODUCT_ALREADY_AVAILABLE("이미 판매 가능 상태입니다.", HttpStatus.CONFLICT),
    PRODUCT_CANNOT_BE_AVAILABLE_WITH_ZERO_STOCK("재고가 0인 상품은 판매 가능 상태로 변경할 수 없습니다.", HttpStatus.CONFLICT),
    PRODUCT_MIN_ORDER_QUANTITY_INVALID("최소 주문량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_MAX_ORDER_QUANTITY_INVALID("최대 주문량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_MIN_ORDER_QUANTITY_EXCEEDS_MAX("최소 주문량은 최대 주문량보다 클 수 없습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_MAX_ORDER_QUANTITY_LESS_THAN_MIN("최대 주문량은 최소 주문량보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_INVALID("수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_DECREASE_QUANTITY_INVALID("차감할 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INCREASE_QUANTITY_INVALID("증가할 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_SOLD_COUNT_LESS_THAN_CANCEL("판매량이 취소량보다 작습니다.", HttpStatus.CONFLICT),
    PRODUCT_DECREASE_SOLD_COUNT_INVALID("감소할 판매량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INCREASE_SOLD_COUNT_INVALID("증가할 판매량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    // ===== Point =====
    POINT_NOT_FOUND("포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POINT_AMOUNT_REQUIRED("포인트 금액은 필수입니다.", HttpStatus.BAD_REQUEST),
    POINT_AMOUNT_INVALID("포인트 금액은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    POINT_DESCRIPTION_REQUIRED("포인트 설명은 필수입니다.", HttpStatus.BAD_REQUEST),
    POINT_ORDER_ID_REQUIRED("주문 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    POINT_CANNOT_EXPIRE_USED_POINT("사용된 포인트는 만료 처리할 수 없습니다.", HttpStatus.CONFLICT),
    POINT_ALREADY_EXPIRED("이미 만료된 포인트입니다.", HttpStatus.CONFLICT),
    POINT_NO_EXPIRATION_DATE("만료일이 없는 포인트입니다.", HttpStatus.CONFLICT),
    POINT_ONLY_CHARGE_OR_REFUND_CAN_BE_USED("충전 또는 환불 포인트만 사용 가능합니다.", HttpStatus.CONFLICT),
    POINT_ALREADY_USED("이미 사용된 포인트입니다.", HttpStatus.CONFLICT),
    POINT_EXPIRED_CANNOT_USE("만료된 포인트는 사용할 수 없습니다.", HttpStatus.CONFLICT),
    POINT_EXPIRATION_DATE_PASSED("유효기간이 지난 포인트입니다.", HttpStatus.CONFLICT),
    INSUFFICIENT_POINT("포인트가 부족합니다.", HttpStatus.BAD_REQUEST),

    // ===== Cart =====
    CART_NOT_FOUND("장바구니 아이템을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_PRODUCT_ID_REQUIRED("상품 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    CART_QUANTITY_INVALID("수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    CART_QUANTITY_CANNOT_BE_LESS_THAN_ONE("수량은 1 미만이 될 수 없습니다. 삭제하려면 장바구니에서 제거하세요.", HttpStatus.CONFLICT),
    CART_INCREASE_AMOUNT_INVALID("증가량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    CART_DECREASE_AMOUNT_INVALID("감소량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    // ===== Order =====
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_USER_ID_REQUIRED("사용자 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_AMOUNT_REQUIRED("주문 금액은 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_AMOUNT_INVALID("주문 금액은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_DISCOUNT_AMOUNT_INVALID("할인 금액은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_POINT_AMOUNT_INVALID("포인트 사용 금액은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_FINAL_AMOUNT_NEGATIVE("최종 결제 금액은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_INVALID_STATUS_FOR_PAYMENT("결제 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_INVALID_STATUS_FOR_CANCEL("취소 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_INVALID_STATUS_FOR_COMPLETE("완료 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_ITEM_NOT_FOUND("주문 아이템을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_ITEM_ORDER_ID_REQUIRED("주문 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_PRODUCT_ID_REQUIRED("상품 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_PRODUCT_NAME_REQUIRED("상품명은 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_QUANTITY_INVALID("수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_UNIT_PRICE_REQUIRED("단가는 필수입니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_UNIT_PRICE_INVALID("단가는 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_INVALID_STATUS_FOR_COMPLETE("완료 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_ITEM_INVALID_STATUS_FOR_CANCEL("취소 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_ITEM_INVALID_STATUS_FOR_RETURN("반품 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_ITEM_INVALID_STATUS_FOR_REFUND("환불 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    ORDER_ITEM_INVALID_STATUS_FOR_CONFIRM("구매확정 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),

    // ===== Payment =====
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PAYMENT_ORDER_ID_REQUIRED("주문 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_AMOUNT_REQUIRED("결제 금액은 필수입니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_AMOUNT_INVALID("결제 금액은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_ALREADY_COMPLETED("이미 완료된 결제입니다.", HttpStatus.CONFLICT),
    PAYMENT_ALREADY_FAILED("이미 실패한 결제입니다.", HttpStatus.CONFLICT),
    PAYMENT_ALREADY_REFUNDED("이미 환불된 결제입니다.", HttpStatus.CONFLICT),
    PAYMENT_CANNOT_COMPLETE_FAILED("실패한 결제는 완료 처리할 수 없습니다.", HttpStatus.CONFLICT),
    PAYMENT_CANNOT_COMPLETE_REFUNDED("환불된 결제는 완료 처리할 수 없습니다.", HttpStatus.CONFLICT),
    PAYMENT_CANNOT_FAIL_COMPLETED("완료된 결제는 실패 처리할 수 없습니다.", HttpStatus.CONFLICT),
    PAYMENT_FAILURE_REASON_REQUIRED("실패 사유는 필수입니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_ONLY_PAYMENT_TYPE_CAN_REFUND("일반 결제만 환불 가능합니다.", HttpStatus.CONFLICT),
    PAYMENT_ONLY_COMPLETED_CAN_REFUND("완료된 결제만 환불 가능합니다.", HttpStatus.CONFLICT),

    // ===== Coupon =====
    COUPON_NOT_FOUND("쿠폰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COUPON_ALL_ISSUED("모든 쿠폰이 이미 발급되었습니다.", HttpStatus.CONFLICT),
    COUPON_ALL_USED("모든 쿠폰이 이미 사용되었습니다.", HttpStatus.CONFLICT),
    COUPON_NOT_AVAILABLE("사용할 수 없는 쿠폰입니다.", HttpStatus.CONFLICT),
    COUPON_EXPIRED("만료된 쿠폰입니다.", HttpStatus.CONFLICT),
    COUPON_NOT_STARTED("쿠폰 사용 기간이 아닙니다.", HttpStatus.CONFLICT),
    COUPON_USAGE_LIMIT_EXCEEDED("쿠폰 사용 횟수를 초과했습니다.", HttpStatus.CONFLICT),
    COUPON_ALREADY_ACTIVE("이미 활성화된 쿠폰입니다.", HttpStatus.CONFLICT),
    COUPON_ALREADY_INACTIVE("이미 비활성화된 쿠폰입니다.", HttpStatus.CONFLICT),
    USER_COUPON_NOT_FOUND("사용자 쿠폰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_COUPON_ALREADY_USED("이미 사용된 쿠폰입니다.", HttpStatus.CONFLICT),
    USER_COUPON_NOT_AVAILABLE("사용 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    COUPON_MIN_ORDER_AMOUNT_NOT_MET("최소 주문 금액을 충족하지 못했습니다.", HttpStatus.BAD_REQUEST),
    COUPON_INVALID_DISCOUNT_TYPE("지원하지 않는 할인 타입입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== Category =====
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_REQUIRED("카테고리명은 필수입니다.", HttpStatus.BAD_REQUEST),
    DISPLAY_ORDER_INVALID("표시 순서는 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    DISPLAY_ORDER_DUPLICATED("이미 사용 중인 표시 순서입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}