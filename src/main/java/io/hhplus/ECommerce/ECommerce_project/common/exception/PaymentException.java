package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 결제 도메인에서 발생하는 예외
 */
public class PaymentException extends BusinessException {

    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}