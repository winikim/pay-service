package app.netlify.winikim.pay.presentation.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import app.netlify.winikim.pay.application.dto.DistributingAmountRequest;
import app.netlify.winikim.pay.application.service.AmountToBeDistributedService;
import app.netlify.winikim.pay.application.service.DistributedAmountService;
import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import app.netlify.winikim.pay.domain.entity.DistributedAmount;
import app.netlify.winikim.pay.domain.entity.DistributedAmountTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(DistributedAmountController.class)
class DistributedAmountControllerTest {

  @MockBean
  private DistributedAmountService distributedAmountService;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("뿌린 금액 받기 API")
  void receiveDistributedAmount() throws Exception {
    // given
    final Long receiveUserId = 3L;
    final String roomId = "room";
    final long amount = 1000;
    final String token = "tok";
    final int numberOfPeople = 3;
    DistributedAmount distributedAmount = new DistributedAmountTest()
        .createDistributedAmount(amount / numberOfPeople);

    given(distributedAmountService.receiveDistributedAmount(token, roomId, receiveUserId))
        .willReturn(distributedAmount);

    // when
    final ResultActions resultActions = requestReceiveDistributedAmount(
        token, roomId,
        receiveUserId);
    // then
    resultActions.andExpect(jsonPath("amount").value(amount / numberOfPeople));

  }

  private ResultActions requestReceiveDistributedAmount(String token, String roomId,
      Long userId) throws Exception {

    return mockMvc.perform(get("/distributed-amount")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-ROOM-ID", roomId)
        .header("X-USER-ID", userId)
        .header("X-TOKEN-ID", token))
        .andDo(print());

  }
}