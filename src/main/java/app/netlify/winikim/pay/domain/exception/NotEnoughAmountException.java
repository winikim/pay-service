package app.netlify.winikim.pay.domain.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;

public class NotEnoughAmountException extends PayException{

  public NotEnoughAmountException(ErrorCode errorCode) {
    super(errorCode);
  }

  public NotEnoughAmountException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public NotEnoughAmountException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public NotEnoughAmountException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
