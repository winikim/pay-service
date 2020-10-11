package app.netlify.winikim.pay.presentation.web;

import app.netlify.winikim.pay.application.dto.DistributedAmountResponse;
import app.netlify.winikim.pay.application.service.DistributedAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DistributedAmountController {

  private final DistributedAmountService distributedAmountService;

  @GetMapping("distributed-amount")
  public DistributedAmountResponse receiveDistributedAmount(
      @RequestHeader(value = "X-ROOM-ID") String roomId,
      @RequestHeader(value = "X-USER-ID") Long userId,
      @RequestHeader(value = "X-TOKEN-ID") String token) {
    return new DistributedAmountResponse(distributedAmountService
        .receiveDistributedAmount(token, roomId, userId));
  }
}
