package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 카테고리 도메인에서 발생하는 예외
 */
public class CategoryException extends BusinessException {

    public CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CategoryException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}