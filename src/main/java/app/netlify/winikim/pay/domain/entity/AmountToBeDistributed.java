package app.netlify.winikim.pay.domain.entity;

import app.netlify.winikim.pay.domain.constant.ErrorCode;
import app.netlify.winikim.pay.domain.exception.CustomIllegalArgumentException;
import app.netlify.winikim.pay.domain.exception.NotEnoughAmountException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"roomId", "token"}
        )
    }
)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmountToBeDistributed extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  private String token;
  @Getter
  private Integer numberOfPeople;
  @Getter
  private Long amount;
  @Getter
  private String roomId;
  @Getter
  private Long createdBy;

  @OneToMany(mappedBy = "amountToBeDistributed", cascade = CascadeType.ALL)
  private final List<DistributedAmount> distributedAmountList = new ArrayList<>();

  @OneToMany(mappedBy = "amountToBeDistributed", cascade = CascadeType.ALL)
  private final List<ReceivedUser> receivedUserList = new ArrayList<>();


  @Builder
  public AmountToBeDistributed(@NonNull Integer numberOfPeople, @NonNull Long amount,
      @NonNull String roomId,
      @NonNull String token, @NonNull Long createdBy) {
    if (numberOfPeople <= 0) {
      throw new CustomIllegalArgumentException(
          ErrorCode.NUMBER_OF_PEOPLE_LESS_THAN_OR_EQUAL_TO_ZERO);
    }
    if (amount <= 0) {
      throw new CustomIllegalArgumentException(
          ErrorCode.AMOUNT_TO_BE_DISTRIBUTED_LESS_THAN_OR_EQUAL_TO_ZERO);
    }
    this.numberOfPeople = numberOfPeople;
    this.amount = amount;
    this.roomId = roomId;
    this.token = token;
    this.createdBy = createdBy;
  }

  // 객체 외부에서 List 조작 방지
  public List<DistributedAmount> getDistributedAmountList() {
    return Collections.unmodifiableList(this.distributedAmountList);
  }

  // 객체 외부에서 List 조작 방지
  public List<ReceivedUser> getReceivedUserList() {
    return Collections.unmodifiableList(this.receivedUserList);
  }

  // 뿌린 금액 추가
  private void addDistributedAmount(DistributedAmount distributedAmount) {
    this.distributedAmountList.add(distributedAmount);
    distributedAmount.setAmountToBeDistributed(this);
  }

  // 뿌린 금액을 받은 사용자 추가
  private void addReceivedUser(ReceivedUser receivedUser) {
    this.receivedUserList.add(receivedUser);
    receivedUser.setAmountToBeDistributed(this);
  }


  // 금액 분배
  public void distributeAmount() {
    // 별
    long quotientOfAmount = this.amount.intValue() / this.numberOfPeople;
    long remainOfAmount = this.amount.intValue() % this.numberOfPeople;
    // 분배 로직: 사람 수 만큼 나눈 후, 남은 돈은 한 사람에게 몰아줌.
    for (int i = 0; i < this.numberOfPeople; i++) {
      if (i == this.numberOfPeople - 1) {
        this.addDistributedAmount(
            new DistributedAmount(quotientOfAmount + remainOfAmount));
      } else {
        this.addDistributedAmount(new DistributedAmount(quotientOfAmount));
      }
    }
  }

  // 금액을 뿌린 사용자 인지 확인
  public boolean isOwner(Long requestUserId) {
    return this.createdBy.equals(requestUserId);
  }

  // 뿌린 금액을 받아 간 사용자 인지 확인
  public boolean isAlreadyReceivedDistributedAmount(Long requestUserId) {
    for (ReceivedUser receivedUser : this.receivedUserList) {
      log.debug("[뿌린 돈을 받았는지 여부 점검] source userId = {}, target userId = {}",
          receivedUser.getUserId(), requestUserId);
      if (receivedUser.getUserId().equals(requestUserId)) {
        return true;
      }
    }
    return false;
  }


  // 뿌린 금액 받아가기
  public DistributedAmount receiveDistributedAmount(Long requestUserId) {
    if (this.distributedAmountList.size() == 0) {
      throw new NotEnoughAmountException(ErrorCode.NON_EXISTENCE_DISTRIBUTED_AMOUNT);
    }
    for (DistributedAmount distributedAmount : this.distributedAmountList) {
      if (!distributedAmount.isReceived()) {
        this.addReceivedUser(new ReceivedUser(distributedAmount.receive(), requestUserId));
        return distributedAmount;
      }
    }
    throw new NotEnoughAmountException(ErrorCode.ALL_RECEIVED_DISTRIBUTED_AMOUNT);
  }


  // 뿌린 돈 중 받아 간 총 금액
  public Long getCompletedToDistributedAmount() {
    long totalAmount = 0;
    for (ReceivedUser receivedUser : this.receivedUserList) {
      log.debug("받은 금액 ={}, 사용자 아이디 = {}", receivedUser.getAmount(), receivedUser.getUserId());
      totalAmount = totalAmount + receivedUser.getAmount();
    }
    return totalAmount;
  }
}
