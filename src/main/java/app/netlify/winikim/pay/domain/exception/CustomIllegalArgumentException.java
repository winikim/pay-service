package app.netlify.winikim.pay.domain.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;

public class CustomIllegalArgumentException extends PayException{

  public CustomIllegalArgumentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public CustomIllegalArgumentException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public CustomIllegalArgumentException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public CustomIllegalArgumentException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
