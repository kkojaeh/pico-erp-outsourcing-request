package pico.erp.outsourcing.request;

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
import pico.erp.process.ProcessId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.UnitKind;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.station.StationId;

public interface OutsourcingRequestRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

    @Valid
    @NotNull
    ItemId itemId;

    @Valid
    @NotNull
    ItemSpecCode itemSpecCode;

    @Valid
    @NotNull
    ProcessId processId;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @NotNull
    @Min(0)
    BigDecimal spareQuantity;

    @NotNull
    UnitKind unit;

    @Valid
    @NotNull
    ProjectId projectId;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Valid
    CompanyId supplierId;

    @Valid
    @NotNull
    CompanyId receiverId;

    @Valid
    SiteId receiveSiteId;

    @Valid
    StationId receiveStationId;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

    @Valid
    @NotNull
    UserId requesterId;

    public static CreateRequest from(OutsourcingRequestData data) {
      return CreateRequest.builder()
        .id(data.getId())
        .itemId(data.getItemId())
        .itemSpecCode(data.getItemSpecCode())
        .processId(data.getProcessId())
        .quantity(data.getQuantity())
        .spareQuantity(data.getSpareQuantity())
        .unit(data.getUnit())
        .projectId(data.getProjectId())
        .dueDate(data.getDueDate())
        .supplierId(data.getSupplierId())
        .receiverId(data.getReceiverId())
        .receiveSiteId(data.getReceiveSiteId())
        .receiveStationId(data.getReceiveStationId())
        .remark(data.getRemark())
        .requesterId(data.getRequesterId())
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
    OutsourcingRequestId id;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @NotNull
    @Min(0)
    BigDecimal spareQuantity;

    @NotNull
    UnitKind unit;

    @Valid
    @NotNull
    ProjectId projectId;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Valid
    CompanyId supplierId;

    @Valid
    @NotNull
    CompanyId receiverId;

    @Valid
    SiteId receiveSiteId;

    @Valid
    StationId receiveStationId;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class AcceptRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

    @Valid
    @NotNull
    UserId accepterId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CommitRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

    @Valid
    @NotNull
    UserId committerId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CompleteRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class RejectRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String rejectedReason;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ProgressRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

    @NotNull
    @Min(0)
    BigDecimal progressedQuantity;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class PlanRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelProgressRequest {

    @Valid
    @NotNull
    OutsourcingRequestId id;

  }

}
