package app.netlify.winikim.pay.domain.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DistributedAmount extends BaseTimeEntity {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "amount_to_be_distributed_id")
  private AmountToBeDistributed amountToBeDistributed;

  @Getter
  private Long amount;

  @Getter
  private boolean isReceived;

  // 연관 관계 메서드
  public void setAmountToBeDistributed(AmountToBeDistributed amountToBeDistributed) {
    this.amountToBeDistributed = amountToBeDistributed;
  }

  public DistributedAmount(Long amount) {
    this.amount = amount;
  }


  // 뿌린 금액 받기
  public Long receive() {
    this.isReceived = true;
    return this.amount;
  }
}
