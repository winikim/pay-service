package app.netlify.winikim.pay.domain.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import lombok.Getter;

@Getter
public class PayException extends RuntimeException {
  private final ErrorCode errorCode;

  public PayException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
  public PayException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
  public PayException(ErrorCode errorCode,String message) {
    super(message);
    this.errorCode = errorCode;
  }
  public PayException(ErrorCode errorCode, Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
  }
}
