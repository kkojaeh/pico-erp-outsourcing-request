package pico.erp.outsourcing.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.process.ProcessId;
import pico.erp.project.ProjectId;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.station.StationId;

@Data
public class OutsourcingRequestAwaitAcceptView {

  OutsourcingRequestId id;

  OutsourcingRequestCode code;

  ItemId itemId;

  ItemSpecCode itemSpecCode;

  ProcessId processId;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  UserId requesterId;

  ProjectId projectId;

  CompanyId receiverId;

  SiteId receiveSiteId;

  StationId receiveStationId;

  OffsetDateTime committedDate;

  OffsetDateTime dueDate;

  OffsetDateTime createdDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    CompanyId receiverId;

    UserId requesterId;

    ProjectId projectId;

    ItemId itemId;

    OffsetDateTime startDueDate;

    OffsetDateTime endDueDate;

  }

}
