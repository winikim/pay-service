package app.netlify.winikim.pay.domain.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import app.netlify.winikim.pay.domain.exception.NotEnoughAmountException;
import app.netlify.winikim.pay.share.util.Utils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AmountToBeDistributedTest {

  @Test
  @DisplayName("뿌리기")
  void distributeAmount() {
    // given
    final Long userId = 1L;
    final String roomId = "room";
    final long amount = 1000;
    final String token = "tok";
    final int numberOfPeople = 3;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(userId)
        .token(token)
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    // when
    amountToBeDistributed.distributeAmount();

    // then
    for (int i = 0; i < amountToBeDistributed.getNumberOfPeople(); i++) {
      if (i == amountToBeDistributed.getNumberOfPeople() - 1) {
        Assertions.assertEquals(334,
            amountToBeDistributed.getDistributedAmountList().get(i).getAmount());
      } else {
        Assertions.assertEquals(333,
            (amountToBeDistributed.getDistributedAmountList().get(i).getAmount()));
      }
    }
  }

  @Test
  @DisplayName("뿌릴 인원이 0이하 경우 뿌리기")
  void distributeAmountWithZeroNumberOfPeople() {
    // given
    final Long userId = 1L;
    final String roomId = "room";
    final long amount = 1000;
    final String token = "tok";
    final int numberOfPeople = 0;

    // when
    CustomIllegalArgumentException thrown = assertThrows(CustomIllegalArgumentException.class,
        () -> AmountToBeDistributed.builder()
            .createdBy(userId)
            .token(token)
            .amount(amount)
            .numberOfPeople(numberOfPeople)
            .roomId(roomId)
            .build());
    // then
    Assertions.assertEquals(ErrorCode.NUMBER_OF_PEOPLE_LESS_THAN_OR_EQUAL_TO_ZERO.getCode(),
        thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌릴 금액이 0이하 경우 뿌리기")
  void distributeAmountWithZeroAmount() {
    // given
    final Long userId = 1L;
    final String roomId = "room";
    final long amount = 0;
    final String token = "tok";
    final int numberOfPeople = 10;

    // when
    CustomIllegalArgumentException thrown = assertThrows(CustomIllegalArgumentException.class,
        () -> AmountToBeDistributed.builder()
            .createdBy(userId)
            .token(token)
            .amount(amount)
            .numberOfPeople(numberOfPeople)
            .roomId(roomId)
            .build());
    // then
    Assertions.assertEquals(ErrorCode.AMOUNT_TO_BE_DISTRIBUTED_LESS_THAN_OR_EQUAL_TO_ZERO.getCode(),
        thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌리기를 한 사용자 인지 확인")
  void isOwner() {
    // given
    Long userId = 1L;
    String roomId = "room";
    long amount = 1000;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(userId)
        .token(Utils.generateToken(3))
        .amount(amount)
        .numberOfPeople(3)
        .roomId(roomId)
        .build();
    // when
    boolean result = amountToBeDistributed.isOwner(userId);
    // then
    Assertions.assertTrue(result);
  }

  @Test
  @DisplayName("뿌리기 건에 대하여 이미 받은 사용자 인지 확인")
  void isAlreadyReceivedDistributedAmount() {
    // given
    Long requestUserId = 1L;
    Long receiveUserId = 2L;
    String roomId = "room";
    long amount = 1000;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(Utils.generateToken(3))
        .amount(amount)
        .numberOfPeople(3)
        .roomId(roomId)
        .build();
    amountToBeDistributed.distributeAmount();
    amountToBeDistributed.receiveDistributedAmount(receiveUserId);

    // when
    boolean result = amountToBeDistributed.isAlreadyReceivedDistributedAmount(receiveUserId);

    // then
    Assertions.assertTrue(result);
  }

  @Test
  @DisplayName("뿌린 금액 받기")
  void receiveDistributedAmount() {
    // given
    Long requestUserId = 1L;
    Long receiveUserId = 2L;
    String roomId = "room";
    int numberOfPeople = 1;
    long amount = 1000;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(Utils.generateToken(3))
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    amountToBeDistributed.distributeAmount();
    // when
    DistributedAmount distributedAmount = amountToBeDistributed
        .receiveDistributedAmount(receiveUserId);

    // then
    Assertions.assertEquals(1000, distributedAmount.getAmount());

  }

  @Test
  @DisplayName("뿌린 금액 받기 시 금액이 전부 소진 됨")
  void allAlreadyReceiveDistributedAmount() {
    // given
    Long requestUserId = 1L;
    Long receiveUserId = 2L;
    String roomId = "room";
    int numberOfPeople = 1;
    long amount = 1000;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(Utils.generateToken(3))
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    amountToBeDistributed.distributeAmount();
    amountToBeDistributed.receiveDistributedAmount(receiveUserId);
    // when
    NotEnoughAmountException thrown =
        assertThrows(NotEnoughAmountException.class,
            () -> amountToBeDistributed.receiveDistributedAmount(receiveUserId));
    // then
    Assertions.assertEquals(ErrorCode.ALL_RECEIVED_DISTRIBUTED_AMOUNT.getCode(),
        thrown.getErrorCode().getCode());
  }

  @Test
  @DisplayName("뿌리기 건에 대하여 받아 간 총 금액 현황")
  void getCompletedToDistributedAmount() {
    // given
    Long requestUserId = 1L;
    Long receiveUserId = 2L;
    String roomId = "room";
    int numberOfPeople = 3;
    long amount = 3000;
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(Utils.generateToken(3))
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    amountToBeDistributed.distributeAmount();
    amountToBeDistributed.receiveDistributedAmount(requestUserId);
    amountToBeDistributed.receiveDistributedAmount(requestUserId);

    // when
    long completedToDistributedAmount = amountToBeDistributed.getCompletedToDistributedAmount();
    // then
    Assertions.assertEquals(2000, completedToDistributedAmount);
  }

  public AmountToBeDistributed createDefaultWithCreatedDate(LocalDateTime localDateTime) {
    final Long requestUserId = 1L;
    final String roomId = "room";
    final int numberOfPeople = 3;
    final long amount = 3000;
    final String token = "tok";
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(token)
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    amountToBeDistributed.setCreatedDateForTest(localDateTime);
    return amountToBeDistributed;
  }

  public AmountToBeDistributed createWithCreatedDate(Long requestUserId, String roomId,
      int numberOfPeople, Long amount, String token, LocalDateTime localDateTime) {
    AmountToBeDistributed amountToBeDistributed = AmountToBeDistributed.builder()
        .createdBy(requestUserId)
        .token(token)
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .roomId(roomId)
        .build();
    amountToBeDistributed.setCreatedDateForTest(localDateTime);
    return amountToBeDistributed;
  }

}