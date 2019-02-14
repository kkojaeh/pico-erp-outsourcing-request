package pico.erp.outsourcing.request.material;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.outsourcing.request.OutsourcingRequestId;

@Repository
public interface OutsourcingRequestMaterialRepository {

  OutsourcingRequestMaterial create(@NotNull OutsourcingRequestMaterial item);

  void deleteBy(@NotNull OutsourcingRequestMaterialId id);

  boolean exists(@NotNull OutsourcingRequestMaterialId id);

  Stream<OutsourcingRequestMaterial> findAllBy(@NotNull OutsourcingRequestId requestId);

  Optional<OutsourcingRequestMaterial> findBy(@NotNull OutsourcingRequestMaterialId id);

  void update(@NotNull OutsourcingRequestMaterial item);

}
