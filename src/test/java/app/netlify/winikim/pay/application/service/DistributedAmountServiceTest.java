package app.netlify.winikim.pay.application.service;


import static org.mockito.BDDMockito.given;

import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.dto.TokenResponse;
import app.netlify.winikim.pay.application.permission.PermissionService;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributedTest;
import app.netlify.winikim.pay.domain.entity.DistributedAmount;
import app.netlify.winikim.pay.infrastructure.AmountToBeDistributedRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DistributedAmountServiceTest {

  @Mock
  private AmountToBeDistributedRepository amountToBeDistributedRepository;
  private DistributedAmountService distributedAmountService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    distributedAmountService = new DistributedAmountService(amountToBeDistributedRepository,
        new PermissionService());
  }

  @Test
  @DisplayName("뿌린 금액 받아가기 서비스")
  void receiveDistributedAmount() {
    // given
    final String token = "abc";
    final String roomId = "room";
    final Long requestUserId = 1L;
    final int numberOfPeople = 5;
    final Long amount = 5000L;
    final AmountToBeDistributed givenAmountToBeDistributed
        = new AmountToBeDistributedTest()
        .createWithCreatedDate(requestUserId, roomId, numberOfPeople, amount,
            token, LocalDateTime.now());
    // 금액 분배
    givenAmountToBeDistributed.distributeAmount();
    given(this.amountToBeDistributedRepository
        .findByTokenAndRoomId(givenAmountToBeDistributed.getToken(),
            givenAmountToBeDistributed.getRoomId())).willReturn(
        Optional.of(givenAmountToBeDistributed));

    // when
    DistributedAmount distributedAmount = distributedAmountService
        .receiveDistributedAmount(givenAmountToBeDistributed.getToken(),
            givenAmountToBeDistributed.getRoomId(), 123123L);

    // then
    Assertions.assertEquals(1000, distributedAmount.getAmount());
    Assertions.assertTrue(distributedAmount.isReceived());

  }
}