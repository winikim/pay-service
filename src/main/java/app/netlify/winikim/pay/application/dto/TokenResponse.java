package app.netlify.winikim.pay.application.dto;

import java.beans.ConstructorProperties;
import lombok.Getter;

@Getter
public class TokenResponse {

  private final String token;

  @ConstructorProperties({"token"})
  public TokenResponse(String token) {
    this.token = token;
  }
}
