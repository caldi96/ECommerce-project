package io.hhplus.ECommerce.ECommerce_project.common.handler;

import io.hhplus.ECommerce.ECommerce_project.common.dto.ErrorResponse;
import io.hhplus.ECommerce.ECommerce_project.common.exception.BusinessException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 핸들러
 * 애플리케이션 전체에서 발생하는 예외를 통합 관리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     * 커스텀 예외(BusinessException)를 처리하고 적절한 HTTP 상태 코드와 함께 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(
            errorCode.name(),
            e.getMessage()
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(errorResponse);
    }

    /**
     * Validation 예외 처리 (@Valid, @Validated 검증 실패)
     * RequestBody 검증 실패 시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        log.warn("MethodArgumentNotValidException: {}", e.getMessage());

        String errorMessage = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse(ErrorCode.INVALID_INPUT_VALUE.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE.name(),
            errorMessage
        );

        return ResponseEntity
            .badRequest()
            .body(errorResponse);
    }

    /**
     * Validation 예외 처리 (ModelAttribute 검증 실패)
     * ModelAttribute 검증 실패 시 발생
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.warn("BindException: {}", e.getMessage());

        String errorMessage = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse(ErrorCode.INVALID_INPUT_VALUE.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE.name(),
            errorMessage
        );

        return ResponseEntity
            .badRequest()
            .body(errorResponse);
    }

    /**
     * IllegalArgumentException 처리
     * 이전 코드와의 호환성을 위해 유지
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e
    ) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            "INVALID_ARGUMENT",
            e.getMessage()
        );

        return ResponseEntity
            .badRequest()
            .body(errorResponse);
    }

    /**
     * IllegalStateException 처리
     * 이전 코드와의 호환성을 위해 유지
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
        IllegalStateException e
    ) {
        log.warn("IllegalStateException: {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            "INVALID_STATE",
            e.getMessage()
        );

        return ResponseEntity
            .status(409) // CONFLICT
            .body(errorResponse);
    }

    /**
     * 그 외 모든 예외 처리
     * 예상하지 못한 예외가 발생했을 때의 최종 방어선
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INTERNAL_SERVER_ERROR.name(),
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );

        return ResponseEntity
            .internalServerError()
            .body(errorResponse);
    }
}