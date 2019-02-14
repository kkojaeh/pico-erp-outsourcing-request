package pico.erp.outsourcing.request.material;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourcing.request.OutsourcingRequest;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface OutsourcingRequestMaterialMessages {

  interface Create {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    class Request {

      @Valid
      @NotNull
      OutsourcingRequestMaterialId id;

      @NotNull
      OutsourcingRequest request;

      @NotNull
      ItemId itemId;

      @Valid
      @NotNull
      ItemSpecCode itemSpecCode;

      @NotNull
      @Min(0)
      BigDecimal quantity;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

      @Valid
      CompanyId supplierId;

      @Future
      OffsetDateTime estimatedSupplyDate;

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

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

      @Valid
      CompanyId supplierId;

      @Future
      OffsetDateTime estimatedSupplyDate;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Delete {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

}
