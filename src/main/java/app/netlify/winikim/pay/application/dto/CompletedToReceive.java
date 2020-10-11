package app.netlify.winikim.pay.application.dto;

import app.netlify.winikim.pay.domain.entity.ReceivedUser;
import lombok.Getter;

@Getter
public class CompletedToReceive {
  private final Long amount;
  private final Long userId;

  public CompletedToReceive(ReceivedUser receivedUser) {
    this.amount = receivedUser.getAmount();
    this.userId = receivedUser.getUserId();
  }
}
