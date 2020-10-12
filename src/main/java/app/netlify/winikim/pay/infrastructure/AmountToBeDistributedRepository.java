package app.netlify.winikim.pay.infrastructure;

import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface AmountToBeDistributedRepository extends JpaRepository<AmountToBeDistributed, Long> {

  boolean existsByTokenAndRoomId(String token, String roomId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<AmountToBeDistributed>  findByTokenAndRoomId(String token, String roomId);
}
