package pico.erp.outsourcing.request.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface OutsourcingRequestMaterialEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request-material.created";

    private OutsourcingRequestMaterialId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request-material.updated";

    private OutsourcingRequestMaterialId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request-material.deleted";

    private OutsourcingRequestMaterialId id;

    public String channel() {
      return CHANNEL;
    }

  }

}
