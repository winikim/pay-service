package app.netlify.winikim.pay.infrastructure;

import app.netlify.winikim.pay.domain.entity.AmountToBeDistributed;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountToBeDistributedRepository extends JpaRepository<AmountToBeDistributed, Long> {

  boolean existsByTokenAndRoomId(String token, String roomId);

  Optional<AmountToBeDistributed>  findByTokenAndRoomId(String token, String roomId);
}
