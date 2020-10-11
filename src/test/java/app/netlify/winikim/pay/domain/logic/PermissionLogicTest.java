package app.netlify.winikim.pay.domain.logic;

import static org.junit.jupiter.api.Assertions.*;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributedTest;
import app.netlify.winikim.pay.domain.exception.CustomAccessDeniedException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PermissionLogicTest {

  private final PermissionLogic permissionLogic = new PermissionLogic();

  @Test
  @DisplayName("뿌린지_10분_지난_후_뿌린_돈_받아_가기")
  void validateExpiredReceiveDistributedAmount() {
    // given
    final Long receiveUserId = 2L;
    AmountToBeDistributed amountToBeDistributed = new AmountToBeDistributedTest()
        .createDefaultWithCreatedDate(LocalDateTime.now().minusMinutes(10));
    // when
    CustomAccessDeniedException thrown = Assertions.assertThrows(CustomAccessDeniedException.class,
        () -> permissionLogic
            .validateReceiveDistributedAmount(amountToBeDistributed, receiveUserId));
    // then
    Assertions.assertEquals(ErrorCode.EXPIRED_RECEIVED_DISTRIBUTED_AMOUNT.getCode(), thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌린_돈을_뿌린_당사자가_받기")
  void validateReceiveDistributedAmountByOwner() {
    // given
    final Long receiveUserId = 1L;
    final String roomId = "room";
    AmountToBeDistributed amountToBeDistributed = new AmountToBeDistributedTest()
        .createDefaultWithCreatedDate(LocalDateTime.now());
    amountToBeDistributed.distributeAmount();
    amountToBeDistributed.receiveDistributedAmount(receiveUserId);

    // when
    CustomAccessDeniedException thrown = Assertions.assertThrows(CustomAccessDeniedException.class,
        () -> permissionLogic
            .validateReceiveDistributedAmount(amountToBeDistributed, receiveUserId));
    // then
    Assertions.assertEquals(
        ErrorCode.NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_OWNER.getCode(),
        thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌린_돈을_이미_받아간_사용자가_다시_해당_뿌린_돈을_받아감")
  void validateRetryReceiveDistributedAmount() {
    // given
    final Long receiveUserId = 2L;
    final String roomId = "room";
    AmountToBeDistributed amountToBeDistributed = new AmountToBeDistributedTest()
        .createDefaultWithCreatedDate(LocalDateTime.now());
    amountToBeDistributed.distributeAmount();
    amountToBeDistributed.receiveDistributedAmount(receiveUserId);

    // when
    CustomAccessDeniedException thrown = Assertions.assertThrows(CustomAccessDeniedException.class,
        () -> permissionLogic
            .validateReceiveDistributedAmount(amountToBeDistributed, receiveUserId));
    // then
    Assertions.assertEquals(
        ErrorCode.NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_ALREADY_RECEIVED.getCode(),
        thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌린_당사자가_아닌_경우_뿌리기건_조회")
  void validateGetAmountToBeDistributedNotOwner() {
    // given
    AmountToBeDistributed amountToBeDistributed = new AmountToBeDistributedTest()
        .createDefaultWithCreatedDate(LocalDateTime.now());
    // when
    CustomAccessDeniedException thrown = Assertions.assertThrows(CustomAccessDeniedException.class,
        () -> permissionLogic
            .validateGetAmountToBeDistributed(amountToBeDistributed, 1234L));
    // then
    Assertions.assertEquals(
        ErrorCode.NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED.getCode(),
        thrown.getErrorCode().getCode());
  }


  @Test
  @DisplayName("7일_지난_후_뿌리기건_조회")
  void validateGetAmountToBeDistributedAfterSevenDays() {
    // given
    AmountToBeDistributed amountToBeDistributed = new AmountToBeDistributedTest()
        .createDefaultWithCreatedDate(LocalDateTime.now().minusDays(8));
    // when
    CustomAccessDeniedException thrown = Assertions.assertThrows(CustomAccessDeniedException.class,
        () -> permissionLogic
            .validateGetAmountToBeDistributed(amountToBeDistributed, 1L));
    // then
    Assertions.assertEquals(
        ErrorCode.NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED_AFTER_SEVEN_DAYS.getCode(),
        thrown.getErrorCode().getCode());
  }

}