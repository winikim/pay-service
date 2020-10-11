package app.netlify.winikim.pay.application.dto;

import app.netlify.winikim.pay.domain.entity.DistributedAmount;
import lombok.Getter;

@Getter
public class DistributedAmountResponse {
  private final Long amount;

  public DistributedAmountResponse(DistributedAmount distributedAmount) {
    this.amount = distributedAmount.getAmount();
  }
}
