package app.netlify.winikim.pay.domain.logic;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.exception.CustomAccessDeniedException;
import java.time.Duration;
import java.time.LocalDateTime;

public class PermissionLogic {

  // 뿌린 돈 받기 유효성 검증
  public void validateReceiveDistributedAmount(AmountToBeDistributed amountToBeDistributed,
      Long requestUserId) {
    // 토큰 유효성 검증
    if ((Duration.between(amountToBeDistributed.getCreatedDate(), LocalDateTime.now()).getSeconds()
        / 60 >= 10)) {
      throw new CustomAccessDeniedException(ErrorCode.EXPIRED_RECEIVED_DISTRIBUTED_AMOUNT);
    }

    // 본인이 뿌린 돈인지 확인
    if (amountToBeDistributed.isOwner(requestUserId)) {
      throw new CustomAccessDeniedException(
          ErrorCode.NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_OWNER);
    }
    // 뿌린 돈에 대하여 이미 받아 갔는지 확인
    if (amountToBeDistributed.isAlreadyReceivedDistributedAmount(requestUserId)) {
      throw new CustomAccessDeniedException(
          ErrorCode.NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_ALREADY_RECEIVED);
    }
  }

  // 뿌리기 건 현재 상태 조회에 대한 유효성 검사
  public void validateGetAmountToBeDistributed(AmountToBeDistributed amountToBeDistributed,
      Long requestUserId) {
    // 뿌린 지 7일이 넘었는지 검증
    if (Duration.between(amountToBeDistributed.getCreatedDate(), LocalDateTime.now()).toDays()
        > 7) {
      throw new CustomAccessDeniedException(
          ErrorCode.NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED_AFTER_SEVEN_DAYS);
    }
    // 본인이 뿌린 건 인지에 대한 검증
    if (!amountToBeDistributed.isOwner(requestUserId)) {
      throw new CustomAccessDeniedException(ErrorCode.NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED);
    }
  }
}
