package io.hhplus.ECommerce.ECommerce_project.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직에서 발생하는 기본 예외 클래스
 * 모든 커스텀 예외는 이 클래스를 상속받아 사용
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}