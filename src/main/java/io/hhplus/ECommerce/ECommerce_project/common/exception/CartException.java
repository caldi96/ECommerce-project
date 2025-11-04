package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 장바구니 도메인에서 발생하는 예외
 */
public class CartException extends BusinessException {

    public CartException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CartException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}