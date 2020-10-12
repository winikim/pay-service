package app.netlify.winikim.pay.application.service;


import static org.mockito.BDDMockito.given;

import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.dto.TokenResponse;
import app.netlify.winikim.pay.application.permission.PermissionService;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributedTest;
import app.netlify.winikim.pay.infrastructure.AmountToBeDistributedRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AmountToBeDistributedServiceTest {

  @Mock
  private AmountToBeDistributedRepository amountToBeDistributedRepository;
  private AmountToBeDistributedService amountToBeDistributedService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    amountToBeDistributedService = new AmountToBeDistributedService(amountToBeDistributedRepository,
        new PermissionService());
  }

  @Test
  @DisplayName("금액 뿌리기 서비스")
  void distributeAmount() {
    // given
    final String roomId = "room";
    final Long userId = 1L;
    final int numberOfPeople = 10;
    final long amount = 5000;
    final DistributingAmountRequest distributingAmountRequest = DistributingAmountRequest.builder()
        .amount(amount)
        .numberOfPeople(numberOfPeople)
        .build();
    given(amountToBeDistributedRepository.existsByTokenAndRoomId("token", roomId))
        .willReturn(false);

    // when
    TokenResponse tokenResponse = amountToBeDistributedService
        .distributeAmount(distributingAmountRequest, roomId, userId);

    // then
    Assertions.assertEquals(3, tokenResponse.getToken().length());
  }

  @Test
  @DisplayName("뿌리기 건 조회 서비스")
  void retrieveAmountToBeDistributed() {
    // given
    final AmountToBeDistributed givenAmountToBeDistributed
        = new AmountToBeDistributedTest().createDefaultWithCreatedDate(LocalDateTime.now());

    given(this.amountToBeDistributedRepository
        .findByTokenAndRoomId(givenAmountToBeDistributed.getToken(),
            givenAmountToBeDistributed.getRoomId())).willReturn(
        Optional.of(givenAmountToBeDistributed));
    // when
    AmountToBeDistributed whenAmountToBeDistributed = amountToBeDistributedService
        .retrieveAmountToBeDistributed(givenAmountToBeDistributed.getToken(),
            givenAmountToBeDistributed.getRoomId(), givenAmountToBeDistributed.getCreatedBy());
    // then
    Assertions.assertEquals(givenAmountToBeDistributed, whenAmountToBeDistributed);
  }

}