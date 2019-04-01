package pico.erp.outsourcing.request;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface OutsourcingRequestRepository {

  long countCreatedBetween(LocalDateTime begin, LocalDateTime end);

  OutsourcingRequest create(@NotNull OutsourcingRequest orderAcceptance);

  void deleteBy(@NotNull OutsourcingRequestId id);

  boolean exists(@NotNull OutsourcingRequestId id);

  Optional<OutsourcingRequest> findBy(@NotNull OutsourcingRequestId id);

  void update(@NotNull OutsourcingRequest orderAcceptance);

}
