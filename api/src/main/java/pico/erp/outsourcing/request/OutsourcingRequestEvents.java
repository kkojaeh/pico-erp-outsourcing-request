package pico.erp.outsourcing.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface OutsourcingRequestEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.created";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ProgressedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.progressed";

    private OutsourcingRequestId id;

    private BigDecimal progressedQuantity;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AcceptedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.accepted";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.updated";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.canceled";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CompletedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.completed";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.committed";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class RejectedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.rejected";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class PlannedEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.planned";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ProgressCanceledEvent implements Event {

    public final static String CHANNEL = "event.outsourcing-request.progress-canceled";

    private OutsourcingRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }


}
