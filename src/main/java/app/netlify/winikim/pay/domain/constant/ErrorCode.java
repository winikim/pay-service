package app.netlify.winikim.pay.domain.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
  HTTP_MESSAGE_NOT_READABLE_EXCEPTION(400, "요청 값을 확인하여주세요."),

  FAIL_TO_GENERATE_TOKEN(1000, "토큰 생성에 실패 하였습니다."),
  NUMBER_OF_PEOPLE_LESS_THAN_OR_EQUAL_TO_ZERO(1001, "뿌릴 인원은 0보다 커야합니다."),
  AMOUNT_TO_BE_DISTRIBUTED_LESS_THAN_OR_EQUAL_TO_ZERO(1002, "뿌릴 금액은 0보다 커야합니다."),
  EXPIRED_RECEIVED_DISTRIBUTED_AMOUNT(1003, "금액을 뿌린지 10분이 지나 더 이상 금액을 받아 갈 수 없습니다."),
  NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_OWNER(1004, "금액을 뿌린 사람은 뿌린 금액을 가질 수 없습니다."),
  NON_HAS_RECEIVE_DISTRIBUTED_MONEY_BECAUSE_OF_ALREADY_RECEIVED(1005, "이미 뿌린 금액을 받았습니다."),
  INVALID_TOKEN_OR_ROOM(1006, "토큰 또는 ROOM ID가 유효하지 않습니다."),
  NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED(1007, "금액을 뿌린 사람만 조회를 할 수 있습니다."),
  NON_HAS_GET_AMOUNT_TO_BE_DISTRIBUTED_AFTER_SEVEN_DAYS(1008, "금액을 뿌린지 7일 이후에는 조회가 불가능 합니다."),
  NON_EXISTENCE_DISTRIBUTED_AMOUNT(1009, "뿌린 금액이 존재하지 않습니다."),
  ALL_RECEIVED_DISTRIBUTED_AMOUNT(1010, "뿌린 금액이 전부 소진 되었습니다."),

  ;

  private final int code;
  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
