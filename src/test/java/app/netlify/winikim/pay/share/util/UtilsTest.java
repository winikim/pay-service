package app.netlify.winikim.pay.share.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UtilsTest {

  @Test
  @DisplayName("3자리 문자열 토큰 발급")
  void generateTokenTest() {
    String token = Utils.generateToken(3);
    Assertions.assertEquals(3, token.length());
  }

}