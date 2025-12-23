package com.pigeon3.ssamantle.adapter.in.http.common.exception;

import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainException;
import com.pigeon3.ssamantle.domain.model.game.exception.GameDomainExceptionType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * 전역 예외 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 비즈니스 예외: 명시적으로 던지는 ApplicationException
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException ex) {
        logger.warn("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        ApiResponse<Void> response = ApiResponse.fail(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatusCode()).body(response);
    }

    /**
     * Bean Validation 예외 처리: @Valid 바디/폼 바인딩
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = joinBindingErrors(ex.getBindingResult());
        logger.warn("Validation error: {}", message);
        return build(ExceptionType.VALIDATION_ERROR, message);
    }

    /**
     * Bean Validation 예외 처리: BindException
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
        String message = joinBindingErrors(ex.getBindingResult());
        logger.warn("Binding error: {}", message);
        return build(ExceptionType.VALIDATION_ERROR, message);
    }

    /**
     * 유효성 검증 실패: @Validated (메서드 파라미터/쿼리 등)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        logger.warn("Constraint violation: {}", message);
        return build(ExceptionType.VALIDATION_ERROR, message);
    }

    /**
     * JSON 파싱 오류 등 요청 바디 형식 오류
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Request body not readable: {}", ex.getMessage());
        return build(ExceptionType.BAD_REQUEST, "요청 본문을 읽을 수 없습니다.");
    }

    /**
     * 필수 파라미터 누락
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String message = "필수 파라미터가 누락되었습니다: " + ex.getParameterName();
        logger.warn("Missing parameter: {}", ex.getParameterName());
        return build(ExceptionType.BAD_REQUEST, message);
    }

    /**
     * 파라미터 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "파라미터 타입이 올바르지 않습니다: " + ex.getName();
        logger.warn("Type mismatch: {}", ex.getName());
        return build(ExceptionType.BAD_REQUEST, message);
    }

    /**
     * 지원하지 않는 HTTP 메서드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.warn("Method not supported: {}", ex.getMethod());
        return build(ExceptionType.BAD_REQUEST, "지원하지 않는 HTTP 메서드입니다.");
    }

    /**
     * 리소스를 찾을 수 없음 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return build(ExceptionType.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.");
    }

    /**
     * 그 외 모든 예외 (예상치 못한 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return build(ExceptionType.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    }

    // ------------------------------------------------------------------------------------

    private ResponseEntity<ApiResponse<Void>> build(ExceptionType type, String message) {
        ApiResponse<Void> response = ApiResponse.fail(
                type.getErrorCode(),
                (message != null && !message.isBlank()) ? message : type.getMessage()
        );
        return ResponseEntity.status(type.getHttpStatusCode()).body(response);
    }

    private String joinBindingErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
