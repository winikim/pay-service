package app.netlify.winikim.pay.presentation.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.dto.TokenResponse;
import app.netlify.winikim.pay.application.service.AmountToBeDistributedService;
import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributedTest;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AmountToBeDistributedController.class)
class AmountToBeDistributedControllerTest {

  @MockBean
  private AmountToBeDistributedService amountToBeDistributedService;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("뿌리기_API")
  void distributingAmount() throws Exception {
    // given
    final String token = "tok";
    final String roomId = "room";
    final Long userId = 1L;
    final DistributingAmountRequest distributingAmountRequest = this
        .buildDefaultDistributingRequest();

    given(amountToBeDistributedService.distributeAmount(any(), any(), any()))
        .willReturn(new TokenResponse(token));

    // when
    final ResultActions resultActions = requestDistributingAmount(distributingAmountRequest, roomId,
        userId);
    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(jsonPath("token").value(token));
  }

  @Test
  @DisplayName("USER_ID 없이 뿌리기 API")
  void distributingMoneyWithoutUserId() throws Exception {
    // given
    final String token = "test";
    final String roomId = "room";
    final DistributingAmountRequest distributingAmountRequest
        = DistributingAmountRequest.builder()
        .amount(5000L)
        .numberOfPeople(5)
        .build();
    given(amountToBeDistributedService.distributeAmount(any(), any(), any()))
        .willReturn(new TokenResponse(token));

    // when
    final ResultActions resultActions = requestDistributingAmountWithoutUserId(
        distributingAmountRequest, roomId);
    // then
    resultActions
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("뿌릴 인원 수 0으로 설정 후 뿌리기_API")
  void distributingAmountWithZeroPeople() throws Exception {
    // given
    final String roomId = "room";
    final Long userId = 1L;
    final DistributingAmountRequest distributingAmountRequest
        = DistributingAmountRequest.builder()
        .amount(5000L)
        .numberOfPeople(0)
        .build();
    given(amountToBeDistributedService.distributeAmount(any(), any(), any()))
        .willThrow(new CustomIllegalArgumentException(
            ErrorCode.NUMBER_OF_PEOPLE_LESS_THAN_OR_EQUAL_TO_ZERO));

    // when
    final ResultActions resultActions = requestDistributingAmount(
        distributingAmountRequest, roomId, userId);
    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code")
            .value(ErrorCode.NUMBER_OF_PEOPLE_LESS_THAN_OR_EQUAL_TO_ZERO.getCode()));
  }

  @Test
  @DisplayName("뿌릴 돈 0으로 뿌리기_API")
  void distributingAmountWithZeroAmount() throws Exception {
    // given
    final String roomId = "room";
    final Long userId = 1L;
    final DistributingAmountRequest distributingAmountRequest
        = DistributingAmountRequest.builder()
        .amount(0L)
        .numberOfPeople(1)
        .build();
    given(amountToBeDistributedService.distributeAmount(any(), any(), any()))
        .willThrow(new CustomIllegalArgumentException(
            ErrorCode.AMOUNT_TO_BE_DISTRIBUTED_LESS_THAN_OR_EQUAL_TO_ZERO));

    // when
    final ResultActions resultActions = requestDistributingAmount(
        distributingAmountRequest, roomId, userId);
    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code")
            .value(ErrorCode.AMOUNT_TO_BE_DISTRIBUTED_LESS_THAN_OR_EQUAL_TO_ZERO.getCode()));
  }

  @Test
  @DisplayName("ROOM_ID 없이 뿌리기_API")
  void distributingMoneyWithoutRoomId() throws Exception {
    // given
    final String token = "tok";
    final Long userId = 1L;
    final DistributingAmountRequest distributingAmountRequest
        = DistributingAmountRequest.builder()
        .amount(5000L)
        .numberOfPeople(5)
        .build();
    given(amountToBeDistributedService.distributeAmount(any(), any(), any()))
        .willReturn(new TokenResponse(token));

    // when
    final ResultActions resultActions = requestDistributingAmountWithoutRoomId(
        distributingAmountRequest, userId);
    // then
    resultActions
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("뿌리기 건 상태 조회 API")
  void retrieveAmountToBeDistributed() throws Exception {
    // given
    final AmountToBeDistributed givenAmountToBeDistributed =
        new AmountToBeDistributedTest().createDefaultWithCreatedDate(LocalDateTime.now());
    givenAmountToBeDistributed.distributeAmount();
    givenAmountToBeDistributed.receiveDistributedAmount(123123L);
    given(amountToBeDistributedService
        .retrieveAmountToBeDistributed(givenAmountToBeDistributed.getToken(),
            givenAmountToBeDistributed.getRoomId(), givenAmountToBeDistributed.getCreatedBy()))
        .willReturn(givenAmountToBeDistributed);

    // when
    final ResultActions resultActions = requestRetrieveAmountToBeDistributed(
        givenAmountToBeDistributed.getToken(), givenAmountToBeDistributed.getRoomId(),
        givenAmountToBeDistributed.getCreatedBy());
    // then
    resultActions
        .andExpect(jsonPath("completedToDistributedAmount").exists())
        .andExpect(jsonPath("dateTimeOfDistributedAmount").exists())
        .andExpect(jsonPath("amountToBeDistributed").exists())
        .andExpect(jsonPath("completedToReceives").exists())
        .andExpect(jsonPath("completedToReceives[0].amount").exists())
        .andExpect(jsonPath("completedToReceives[0].userId").exists());
  }

  private DistributingAmountRequest buildDefaultDistributingRequest() {
    return DistributingAmountRequest.builder()
        .amount(5000L)
        .numberOfPeople(5)
        .build();
  }

  private ResultActions requestDistributingAmount(DistributingAmountRequest dto, String roomId,
      Long userId) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    return mockMvc.perform(post("/amount-to-be-distributed")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-ROOM-ID", roomId)
        .header("X-USER-ID", userId)
        .content(objectMapper.writeValueAsString(dto)))
        .andDo(print());

  }

  private ResultActions requestDistributingAmountWithoutUserId(DistributingAmountRequest dto,
      String roomId) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    return mockMvc.perform(post("/amount-to-be-distributed")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-ROOM-ID", roomId)
        .content(objectMapper.writeValueAsString(dto)))
        .andDo(print());

  }

  private ResultActions requestRetrieveAmountToBeDistributed(String token, String roomId,
      Long userId) throws Exception {
    return mockMvc.perform(get("/amount-to-be-distributed")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-TOKEN-ID", token)
        .header("X-ROOM-ID", roomId)
        .header("X-USER-ID", userId))
        .andDo(print());

  }

  private ResultActions requestDistributingAmountWithoutRoomId(DistributingAmountRequest dto,
      Long userId) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    return mockMvc.perform(post("/amount-to-be-distributed")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-USER-ID", userId)
        .content(objectMapper.writeValueAsString(dto)))
        .andDo(print());

  }

}