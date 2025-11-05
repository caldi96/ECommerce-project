
Table users {
    id bigint [pk]
    password varchar [not null]
    username varchar [not null, unique] // 실제 로그인에 사용할 Id
    point_balance decimal(10,2) [not null, default: 0]  // 현재 포인트 잔액
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
}

Table categories {
    id bigint [pk]
    category_name varchar [not null, unique]
    display_order int [default: 0]
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
}

Table products {
    id bigint [pk]
    name varchar [not null]
    description text
    price decimal(10,2) [not null]
    stock int [not null, default: 0]
    category_id bigint [ref: > categories.id]
    is_active boolean [default: true]
    is_soldOut boolean [default: false]
    view_count int [default: 0]  // 조회수 (인기순 정렬용)
    sold_count int [default: 0]  // 판매량 (인기순 정렬용)
    min_order_quantity int [default: 1]  // 1인당 최소 구매 수량
    max_order_quantity int  // 1인당 최대 구매 수량
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
    deleted_at timestamp
    indexes {
        (category_id)
        (is_active)
        (created_at)
        (sold_count)  // 판매량순 정렬용
    }
}

Table orders {
    id bigint [pk]
    user_id bigint [ref: > users.id, not null]
    total_amount decimal(10,2) [not null]  // 상품 금액 합계
    discount_amount decimal(10,2) [default: 0]  // 할인 금액 (쿠폰)
    shipping_fee decimal(10,2) [not null, default: 0]  // 배송비
    final_amount decimal(10,2) [not null]  // 최종 결제 금액 = total_amount - discount_amount + shipping_fee - point_amount
    status varchar [not null, default: 'PENDING'] // PENDING, PAID, PAYMENT_FAILED, CANCELED
    coupon_id bigint [ref: > coupons.id]  // 사용된 쿠폰
    point_amount decimal(10,2) [default: 0]  // 사용된 포인트
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
    paid_at timestamp
    canceled_at timestamp
    indexes {
        (user_id)
        (status)
        (created_at)
    }
}

Table payments {
    id bigint [pk]
    order_id bigint [ref: > orders.id, not null]
    amount decimal(10,2) [not null]
    payment_type varchar [not null]  // PAYMENT, REFUND
    payment_method varchar [not null]  // CARD, BANK_TRANSFER, KAKAO_PAY, TOSS 등
    payment_status varchar [not null, default: 'PENDING']  // PENDING, COMPLETED, FAILED, REFUNDED
    // 실패 사유
    failure_reason text
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
    completed_at timestamp
    failed_at timestamp
    indexes {
        (order_id)
        (payment_status)
    }
}

Table order_items {
    id bigint [pk]
    product_id bigint [ref: > products.id, not null]
    order_id bigint [ref: > orders.id, not null]
    product_name varchar [not null]  // 주문 당시 상품명
    quantity int [not null]
    unit_price decimal(10,2) [not null]
    subtotal decimal(10,2) [not null] // quantity * unit_price
    status varchar  // 주문완료, 주문취소, 반품, 환불, 구매확정
    confirmed_at timestamp
    cancelled_at timestamp
    returned_at timestamp
    refunded_at timestamp
    created_at timestamp [not null, default: `now()`]
    indexes {
        (order_id)
        (product_id)
    }
}

Table carts {
    id bigint [pk]
    user_id bigint [ref: > users.id, not null]
    product_id bigint [ref: > products.id, not null]
    quantity int [not null, default: 1]
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
    indexes {
        (user_id, product_id) [unique]
    }
}

Table points {
    id bigint [pk]
    user_id bigint [ref: > users.id, not null]
    order_id bigint [ref: > orders.id]  // 주문 관련 포인트일 경우
    amount decimal(10,2) [not null]
    point_type varchar [not null]  // EARNED, USED, EXPIRED, REFUNDED
    description varchar  // 적립/사용 사유
    expires_at timestamp  // 포인트 만료일
    created_at timestamp [not null, default: `now()`]
    indexes {
        (user_id)
        (order_id)
        (created_at)
    }
}

Table coupons {
    id bigint [pk]
    name varchar [not null] // "신규가입 쿠폰", "5월 할인 이벤트"
    code varchar [unique] // "WELCOME2025" (입력형 쿠폰)
    // 할인 정보
    discount_type varchar [not null] // PERCENTAGE(% 정률), FIXED(정액)
    discount_value decimal(10,2) [not null] // 10 (10% 또는 10,000원)
    max_discount_amount decimal(10,2) // 최대 할인 금액 (% 쿠폰일 때)
    min_order_amount decimal(10,2) // 최소 주문 금액
    // 수량 관리
    total_quantity int // 총 발급 가능 수량 (null이면 무제한)
    issued_quantity int [default: 0] // 발급된 수량
    usage_count int [default: 0] // 현재까지 사용된 횟수
    per_user_limit int [default: 1] // 1인당 사용 가능 횟수
    // 유효 기간
    start_date timestamp [not null] // 시작 시점
    end_date timestamp [not null] // 종료 시점
    is_active boolean [default: true] // 사용 가능 여부
    created_at timestamp [not null, default: `now()`]
    updated_at timestamp [not null, default: `now()`]
    indexes {
        (code)
        (is_active)
    }
}

Table user_coupons {
    id bigint [pk]
    coupon_id bigint [ref: > coupons.id, not null]
    user_id bigint [ref: > users.id, not null]
    status varchar [not null, default: 'AVAILABLE'] // AVAILABLE, USED, EXPIRED
    usedCount int
    used_at timestamp  // 사용 시간
    expired_at timestamp [not null]  // 만료 시간
    issued_at timestamp [not null, default: `now()`]  // 발급 시간
    indexes {
        (user_id, coupon_id)
        (user_id, status)
        (expired_at)
    }
}

Table coupon_queues {
    id bigint [pk]
    coupon_id bigint [ref: > coupons.id, not null]
    user_id bigint [ref: > users.id, not null]
    position int [not null]  // 대기 순번
    status varchar [not null, default: 'WAITING']  // WAITING, PROCESSING, ISSUED, FAILED (쿠폰 소진), EXPIRED(만료)
    session_id varchar [not null]  // WebSocket 세션 관리용
    last_heartbeat timestamp  // 연결 상태 체크
    entered_at timestamp [not null, default: `now()`]  // 대기열 진입 시간
    processing_started_at timestamp  // 처리 시작 시간
    completed_at timestamp  // 완료 시간
    indexes {
        (coupon_id, user_id) [unique]
        (coupon_id, position)
        (status)
    }
}

Table queue_events {
    id bigint [pk]
    coupon_id bigint [ref: > coupons.id, not null]
    user_id bigint [ref: > users.id]
    event_type varchar [not null]  // USER_JOINED, USER_LEFT, POSITION_UPDATED, COUPON_ISSUED, QUEUE_COMPLETED
    position_change int  // 순번 변경값
    metadata json  // 추가 데이터
    created_at timestamp [not null, default: `now()`]
    indexes {
        (coupon_id, created_at)
        (event_type)
    }
}