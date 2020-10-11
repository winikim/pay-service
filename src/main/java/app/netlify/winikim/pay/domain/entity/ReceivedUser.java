package app.netlify.winikim.pay.domain.entity;


import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ReceivedUser extends BaseTimeEntity {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  private Long userId;
  @Getter
  private Long amount;



  public ReceivedUser(Long amount, Long userId) {
    this.userId = userId;
    this.amount = amount;
  }

  @ManyToOne
  @JoinColumn(name = "amount_to_be_distributed_id")
  private AmountToBeDistributed amountToBeDistributed;

  // 연관 관계 메서드
  public void setAmountToBeDistributed(AmountToBeDistributed amountToBeDistributed) {
    this.amountToBeDistributed = amountToBeDistributed;
  }

}
