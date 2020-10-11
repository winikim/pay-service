package app.netlify.winikim.pay.domain.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;

public class CustomAccessDeniedException extends PayException{

  public CustomAccessDeniedException(ErrorCode errorCode) {
    super(errorCode);
  }

  public CustomAccessDeniedException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public CustomAccessDeniedException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public CustomAccessDeniedException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
