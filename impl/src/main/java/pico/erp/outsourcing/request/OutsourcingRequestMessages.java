package pico.erp.outsourcing.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.process.ProcessId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.station.StationId;

public interface OutsourcingRequestMessages {

  interface Create {

    @Data
    class Request {

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

      @Valid
      @NotNull
      ProjectId projectId;

      @Future
      @NotNull
      OffsetDateTime dueDate;

      CompanyId supplierId;

      @NotNull
      CompanyId receiverId;

      @Valid
      SiteId receiveSiteId;

      @Valid
      StationId receiveStationId;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

      @NotNull
      UserId requesterId;

      @NotNull
      OutsourcingRequestCodeGenerator codeGenerator;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Update {

    @Data
    class Request {

      @NotNull
      @Min(0)
      BigDecimal quantity;

      @NotNull
      @Min(0)
      BigDecimal spareQuantity;


      @Valid
      @NotNull
      ProjectId projectId;

      @Future
      @NotNull
      OffsetDateTime dueDate;

      CompanyId supplierId;

      @NotNull
      CompanyId receiverId;

      @Valid
      SiteId receiveSiteId;

      @Valid
      StationId receiveStationId;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Accept {

    @Data
    class Request {

      @NotNull
      UserId accepterId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Commit {

    @Data
    class Request {

      @NotNull
      UserId committerId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Complete {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Reject {

    @Data
    class Request {

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String rejectedReason;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Cancel {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Progress {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Plan {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface CancelProgress {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }


}
