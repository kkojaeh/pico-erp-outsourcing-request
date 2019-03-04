package pico.erp.outsourcing.request.material;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourcing.request.OutsourcingRequestId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.UnitKind;

public interface OutsourcingRequestMaterialRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    OutsourcingRequestMaterialId id;

    @Valid
    @NotNull
    OutsourcingRequestId requestId;

    @Valid
    @NotNull
    ItemId itemId;

    @Valid
    @NotNull
    ItemSpecCode itemSpecCode;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @NotNull
    UnitKind unit;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

    @Valid
    CompanyId supplierId;

    @Future
    OffsetDateTime estimatedSupplyDate;

    public static CreateRequest from(OutsourcingRequestMaterialData data) {
      return CreateRequest.builder()
        .id(data.getId())
        .itemId(data.getItemId())
        .itemSpecCode(data.getItemSpecCode())
        .quantity(data.getQuantity())
        .unit(data.getUnit())
        .supplierId(data.getSupplierId())
        .remark(data.getRemark())
        .estimatedSupplyDate(data.getEstimatedSupplyDate())
        .build();
    }

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    OutsourcingRequestMaterialId id;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @NotNull
    UnitKind unit;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

    @Valid
    CompanyId supplierId;

    @Future
    OffsetDateTime estimatedSupplyDate;

    public static UpdateRequest from(OutsourcingRequestMaterialData data) {
      return UpdateRequest.builder()
        .id(data.getId())
        .quantity(data.getQuantity())
        .unit(data.getUnit())
        .supplierId(data.getSupplierId())
        .remark(data.getRemark())
        .estimatedSupplyDate(data.getEstimatedSupplyDate())
        .build();
    }


  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    OutsourcingRequestMaterialId id;

  }

}
