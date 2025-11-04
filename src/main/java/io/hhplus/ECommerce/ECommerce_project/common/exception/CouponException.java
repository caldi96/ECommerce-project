package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 쿠폰 도메인에서 발생하는 예외
 */
public class CouponException extends BusinessException {

    public CouponException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CouponException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}