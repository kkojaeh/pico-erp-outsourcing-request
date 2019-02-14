package pico.erp.outsourcing.request.material;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.outsourcing.request.OutsourcingRequestId;

public interface OutsourcingRequestMaterialService {

  OutsourcingRequestMaterialData create(
    @Valid @NotNull OutsourcingRequestMaterialRequests.CreateRequest request);

  void delete(@Valid @NotNull OutsourcingRequestMaterialRequests.DeleteRequest request);

  boolean exists(@Valid @NotNull OutsourcingRequestMaterialId id);

  OutsourcingRequestMaterialData get(@Valid @NotNull OutsourcingRequestMaterialId id);

  List<OutsourcingRequestMaterialData> getAll(@Valid @NotNull OutsourcingRequestId requestId);

  void update(@Valid @NotNull OutsourcingRequestMaterialRequests.UpdateRequest request);

}
