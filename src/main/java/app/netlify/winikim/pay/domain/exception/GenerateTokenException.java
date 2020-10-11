package app.netlify.winikim.pay.domain.exception;

import app.netlify.winikim.pay.domain.constant.ErrorCode;

public class GenerateTokenException extends PayException{

  public GenerateTokenException(ErrorCode errorCode) {
    super(errorCode);
  }

  public GenerateTokenException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public GenerateTokenException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public GenerateTokenException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
