package io.hhplus.ECommerce.ECommerce_project.common.exception;

/**
 * 사용자 도메인에서 발생하는 예외
 */
public class UserException extends BusinessException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}