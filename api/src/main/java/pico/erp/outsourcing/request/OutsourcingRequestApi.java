package pico.erp.outsourcing.request;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

public final class OutsourcingRequestApi {

  @RequiredArgsConstructor
  public enum Roles implements Role {

    OUTSOURCING_REQUESTER,

    OUTSOURCING_REQUEST_ACCEPTER,

    OUTSOURCING_REQUEST_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }
}
