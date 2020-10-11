package app.netlify.winikim.pay.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DistributedAmountTest {

  @Test
  @DisplayName("[성공]_뿌린_금액_받기")
  void receive() {
    // given
    final long amount = 1000;
    DistributedAmount distributedAmount = new DistributedAmount(amount);
    // when
    Long receiveAmount = distributedAmount.receive();
    // then
    Assertions.assertTrue(distributedAmount.isReceived());
    Assertions.assertEquals(amount, receiveAmount);
  }

  public DistributedAmount createDistributedAmount(Long amount) {
    return new DistributedAmount(amount);
  }
}