package app.netlify.winikim.pay.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.exception.CustomAccessDeniedException;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import app.netlify.winikim.pay.domain.exception.NotEnoughAmountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    log.error("[handleMethodArgumentTypeMismatchException] message = {}",
        e.getMessage());
    return new ErrorResponse(400, e.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.error("[handleHttpMessageNotReadableException] message = {}",
        e.getMessage());
    return new ErrorResponse(ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode(),
        ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION
            .getMessage());
  }

  @ExceptionHandler(CustomIllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleCustomIllegalArgumentException(CustomIllegalArgumentException e) {
    log.error("[handleCustomIllegalArgumentException] message = {}",
        e.getMessage());
    return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
  }

  @ExceptionHandler(CustomAccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  ErrorResponse handleCustomAccessDeniedException(CustomAccessDeniedException e) {
    log.error("[handleCustomAccessDeniedException] message = {}",
        e.getMessage());
    return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
  }

  @ExceptionHandler(NotEnoughAmountException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleNotEnoughAmountException(NotEnoughAmountException e) {
    log.error("[handleNotEnoughAmountException] message = {}",
        e.getMessage());
    return new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
  }
}
