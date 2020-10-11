package app.netlify.winikim.pay.application.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DistributingAmountRequest {
  private Long amount;
  private Integer numberOfPeople;

  @Builder
  public DistributingAmountRequest(Long amount, Integer numberOfPeople) {
    this.amount = amount;
    this.numberOfPeople = numberOfPeople;
  }
}
