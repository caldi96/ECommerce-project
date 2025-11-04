package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 상품 도메인에서 발생하는 예외
 */
public class ProductException extends BusinessException {

    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProductException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}