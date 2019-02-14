package pico.erp.outsourcing.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface OutsourcingRequestService {

  void cancel(@Valid @NotNull OutsourcingRequestRequests.CancelRequest request);

  OutsourcingRequestData create(@Valid @NotNull OutsourcingRequestRequests.CreateRequest request);

  boolean exists(@Valid @NotNull OutsourcingRequestId id);

  OutsourcingRequestData get(@Valid @NotNull OutsourcingRequestId id);

  void update(@Valid @NotNull OutsourcingRequestRequests.UpdateRequest request);

  void accept(@Valid @NotNull OutsourcingRequestRequests.AcceptRequest request);

  void commit(@Valid @NotNull OutsourcingRequestRequests.CommitRequest request);

  void complete(@Valid @NotNull OutsourcingRequestRequests.CompleteRequest request);

  void reject(@Valid @NotNull OutsourcingRequestRequests.RejectRequest request);

  void progress(@Valid @NotNull OutsourcingRequestRequests.ProgressRequest request);

  void plan(@Valid @NotNull OutsourcingRequestRequests.PlanRequest request);

  void cancelProgress(@Valid @NotNull OutsourcingRequestRequests.CancelProgressRequest request);

}
