package app.netlify.winikim.pay.application.service;

import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.dto.TokenResponse;
import app.netlify.winikim.pay.application.permission.PermissionService;
import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import app.netlify.winikim.pay.domain.exception.GenerateTokenException;
import app.netlify.winikim.pay.infrastructure.AmountToBeDistributedRepository;
import app.netlify.winikim.pay.share.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmountToBeDistributedService {

  private final AmountToBeDistributedRepository amountToBeDistributedRepository;
  private final PermissionService permissionService;
  private final static int RETRY_COUNT_GENERATE_TOKEN = 5;

  /**
   * 머니 뿌리기
   *
   * @param distributingAmountRequest 뿌리기 요청 객체
   * @param roomId:                   요청한 사용자가 속한 대화방의 식별값
   * @param requestUserId:            요청한 사용자의 식별값
   */
  @Transactional
  public TokenResponse distributeAmount(DistributingAmountRequest distributingAmountRequest,
      String roomId,
      Long requestUserId) {
    String generatedToken = this.generateTokenWithRoomId(roomId);
    AmountToBeDistributed amountToBeDistributed
        = AmountToBeDistributed.builder()
        .roomId(roomId)
        .numberOfPeople(distributingAmountRequest.getNumberOfPeople())
        .amount(distributingAmountRequest.getAmount())
        .createdBy(requestUserId)
        .token(generatedToken).build();

    // 금액 분배
    amountToBeDistributed.distributeAmount();

    this.amountToBeDistributedRepository.save(amountToBeDistributed);

    return new TokenResponse(amountToBeDistributed.getToken());
  }

  /**
   * 뿌린 돈의 현재 상태 조회
   *
   * @param token:         금액 뿌리기 시 생성 된 토큰
   * @param roomId:        요청한 사용자가 속한 대화방의 식별값
   * @param requestUserId: 요청한 사용자의 식별값
   */
  @Transactional(readOnly = true)
  public AmountToBeDistributed retrieveAmountToBeDistributed(String token, String roomId,
      Long requestUserId) {
    AmountToBeDistributed amountToBeDistributed = amountToBeDistributedRepository
        .findByTokenAndRoomId(token, roomId)
        .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.INVALID_TOKEN_OR_ROOM));
    // 유효성 검사
    permissionService.validateGetAmountToBeDistributed(amountToBeDistributed, requestUserId);

    return amountToBeDistributed;
  }

  // 같은 대화방에 중복 된 토큰 값이 존재 하지 않도록 생성 된 토큰 확인 후 재생성 진행
  private String generateTokenWithRoomId(String roomId) {
    // 재시도 횟 수
    int tryCount = RETRY_COUNT_GENERATE_TOKEN;
    String generatedToken = null;
    while (tryCount > 0) {
      // 3자리문자로 구성 된 토큰 생성
      generatedToken = Utils.generateToken(3);
      log.debug("[토큰 생성] 토큰 = {}, 시도 횟수 ={}", generatedToken, tryCount);
      if (!amountToBeDistributedRepository.existsByTokenAndRoomId(generatedToken, roomId)) {
        break;
      }
      tryCount--;
      if (tryCount == 0) {
        log.error("[토큰 생성 실패] 총 재시도 횟수 = {}", tryCount);
        throw new GenerateTokenException(ErrorCode.FAIL_TO_GENERATE_TOKEN);
      }
    }
    return generatedToken;
  }
}
