package app.netlify.winikim.pay.presentation.web;

import app.netlify.winikim.pay.application.dto.AmountToBeDistributedResponse;
import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.dto.TokenResponse;
import app.netlify.winikim.pay.application.service.AmountToBeDistributedService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AmountToBeDistributedController {

  private final AmountToBeDistributedService amountToBeDistributedService;

  public AmountToBeDistributedController(
      AmountToBeDistributedService amountToBeDistributedService) {
    this.amountToBeDistributedService = amountToBeDistributedService;
  }

  @PostMapping("amount-to-be-distributed")
  @ResponseStatus(HttpStatus.CREATED)
  public TokenResponse distributingAmount(
      @RequestHeader(value = "X-ROOM-ID") String roomId,
      @RequestHeader(value = "X-USER-ID") Long userId,
      @RequestBody DistributingAmountRequest distributingAmountRequest) {
    return this.amountToBeDistributedService
        .distributeAmount(distributingAmountRequest, roomId, userId);
  }

  @GetMapping("amount-to-be-distributed")
  public AmountToBeDistributedResponse retrieveAmountToBeDistributed(
      @RequestHeader(value = "X-ROOM-ID") String roomId,
      @RequestHeader(value = "X-USER-ID") Long userId,
      @RequestHeader(value = "X-TOKEN-ID") String token) {
    return new AmountToBeDistributedResponse(
        amountToBeDistributedService.retrieveAmountToBeDistributed(token, roomId, userId));
  }


}
