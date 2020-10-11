package app.netlify.winikim.pay.application.service;

import app.netlify.winikim.pay.application.permission.PermissionService;
import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.entity.DistributedAmount;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import app.netlify.winikim.pay.infrastructure.AmountToBeDistributedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DistributedAmountService {

  private final AmountToBeDistributedRepository amountToBeDistributedRepository;
  private final PermissionService permissionService;

  /**
   * 뿌린 금액 받아가기
   * 해당 기능은 X-USER-ID가 X-ROOM-ID에 속해 있다고 가정한다.
   * -> 동일한 대화방에 속한 사용자라고 가정한다.
   * 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
   * @param token: 금액 뿌리기 시 생성 된 토큰
   * @param roomId:                   요청한 사용자가 속한 대화방의 식별값
   * @param requestUserId:            요청한 사용자의 식별값
   */
  @Transactional
  public DistributedAmount receiveDistributedAmount(String token, String roomId,
      Long requestUserId) {
    AmountToBeDistributed amountToBeDistributed = this.amountToBeDistributedRepository
        .findByTokenAndRoomId(token, roomId)
        .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.INVALID_TOKEN_OR_ROOM));
    // 유효성 검사
    permissionService.validateReceiveDistributedAmount(amountToBeDistributed, requestUserId);

    // 뿌린 돈 받아가기
    DistributedAmount distributedAmount = amountToBeDistributed.receiveDistributedAmount(requestUserId);

    this.amountToBeDistributedRepository.save(amountToBeDistributed);

    return distributedAmount;
  }


}
