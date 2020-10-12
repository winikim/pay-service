package app.netlify.winikim.pay.application.dto;

import app.netlify.winikim.pay.domain.entity.ReceivedUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class AmountToBeDistributedResponse {
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private final LocalDateTime dateTimeOfDistributedAmount;
  private final Long amountToBeDistributed;
  private final Long completedToDistributedAmount;
  private final List<CompletedToReceive> completedToReceives = new ArrayList<>();

  public AmountToBeDistributedResponse(
      app.netlify.winikim.pay.domain.entity.AmountToBeDistributed amountToBeDistributed) {
    this.dateTimeOfDistributedAmount = amountToBeDistributed.getCreatedDate();
    this.amountToBeDistributed = amountToBeDistributed.getAmount();
    this.completedToDistributedAmount = amountToBeDistributed.getCompletedToDistributedAmount();
    for(ReceivedUser receivedUser : amountToBeDistributed.getReceivedUserList()) {
      this.completedToReceives.add(new CompletedToReceive(receivedUser));
    }
  }
}
