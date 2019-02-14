package pico.erp.outsourcing.request.material;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourcing.request.OutsourcingRequest;
import pico.erp.outsourcing.request.OutsourcingRequestId;
import pico.erp.outsourcing.request.OutsourcingRequestMapper;
import pico.erp.shared.data.Auditor;

@Mapper
public abstract class OutsourcingRequestMaterialMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Lazy
  @Autowired
  protected ItemSpecService itemSpecService;

  @Lazy
  @Autowired
  private OutsourcingRequestMaterialRepository outsourcingRequestMaterialRepository;


  @Autowired
  private OutsourcingRequestMapper requestMapper;

  protected OutsourcingRequestMaterialId id(OutsourcingRequestMaterial outsourcingRequestMaterial) {
    return outsourcingRequestMaterial != null ? outsourcingRequestMaterial.getId() : null;
  }

  @Mappings({
    @Mapping(target = "requestId", source = "request.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract OutsourcingRequestMaterialEntity jpa(OutsourcingRequestMaterial data);

  public OutsourcingRequestMaterial jpa(OutsourcingRequestMaterialEntity entity) {
    return OutsourcingRequestMaterial.builder()
      .id(entity.getId())
      .request(map(entity.getRequestId()))
      .itemId(entity.getItemId())
      .itemSpecCode(entity.getItemSpecCode())
      .quantity(entity.getQuantity())
      .remark(entity.getRemark())
      .supplierId(entity.getSupplierId())
      .estimatedSupplyDate(entity.getEstimatedSupplyDate())
      .build();
  }

  public OutsourcingRequestMaterial map(OutsourcingRequestMaterialId outsourcingRequestMaterialId) {
    return Optional.ofNullable(outsourcingRequestMaterialId)
      .map(id -> outsourcingRequestMaterialRepository.findBy(id)
        .orElseThrow(OutsourcingRequestMaterialExceptions.NotFoundException::new)
      )
      .orElse(null);
  }


  protected OutsourcingRequest map(OutsourcingRequestId outsourcingRequestId) {
    return requestMapper.map(outsourcingRequestId);
  }

  @Mappings({
    @Mapping(target = "requestId", source = "request.id")
  })
  public abstract OutsourcingRequestMaterialData map(OutsourcingRequestMaterial item);

  @Mappings({
    @Mapping(target = "request", source = "requestId")
  })
  public abstract OutsourcingRequestMaterialMessages.Create.Request map(
    OutsourcingRequestMaterialRequests.CreateRequest request);

  public abstract OutsourcingRequestMaterialMessages.Update.Request map(
    OutsourcingRequestMaterialRequests.UpdateRequest request);

  public abstract OutsourcingRequestMaterialMessages.Delete.Request map(
    OutsourcingRequestMaterialRequests.DeleteRequest request);


  public abstract void pass(
    OutsourcingRequestMaterialEntity from, @MappingTarget OutsourcingRequestMaterialEntity to);


}



