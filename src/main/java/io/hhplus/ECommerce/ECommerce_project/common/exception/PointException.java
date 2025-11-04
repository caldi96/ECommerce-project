package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 포인트 도메인에서 발생하는 예외
 */
public class PointException extends BusinessException {

    public PointException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PointException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}