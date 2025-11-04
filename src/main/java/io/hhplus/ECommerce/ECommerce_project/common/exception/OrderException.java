package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 주문 도메인에서 발생하는 예외
 */
public class OrderException extends BusinessException {

    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}