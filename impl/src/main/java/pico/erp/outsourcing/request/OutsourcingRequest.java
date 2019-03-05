package pico.erp.outsourcing.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.process.ProcessId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.UnitKind;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.station.StationId;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Audit(alias = "outsourcing-request")
public class OutsourcingRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  OutsourcingRequestId id;

  OutsourcingRequestCode code;

  ItemId itemId;

  ItemSpecCode itemSpecCode;

  ProcessId processId;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  BigDecimal progressedQuantity;

  UnitKind unit;

  ProjectId projectId;

  OffsetDateTime dueDate;

  CompanyId supplierId;

  CompanyId receiverId;

  SiteId receiveSiteId;

  StationId receiveStationId;

  String remark;

  UserId requesterId;

  UserId accepterId;

  OffsetDateTime committedDate;

  OffsetDateTime acceptedDate;

  OffsetDateTime completedDate;

  OffsetDateTime rejectedDate;

  OffsetDateTime canceledDate;

  OutsourcingRequestStatusKind status;

  String rejectedReason;


  public OutsourcingRequest() {

  }

  public OutsourcingRequestMessages.Create.Response apply(
    OutsourcingRequestMessages.Create.Request request) {
    this.id = request.getId();
    this.itemId = request.getItemId();
    this.itemSpecCode = request.getItemSpecCode();
    this.processId = request.getProcessId();
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.unit = request.getUnit();
    this.projectId = request.getProjectId();
    this.dueDate = request.getDueDate();
    this.supplierId = request.getSupplierId();
    this.receiverId = request.getReceiverId();
    this.receiveSiteId = request.getReceiveSiteId();
    this.receiveStationId = request.getReceiveStationId();
    this.remark = request.getRemark();
    this.status = OutsourcingRequestStatusKind.DRAFT;
    this.requesterId = request.getRequesterId();
    this.progressedQuantity = BigDecimal.ZERO;
    this.code = request.getCodeGenerator().generate(this);
    return new OutsourcingRequestMessages.Create.Response(
      Arrays.asList(new OutsourcingRequestEvents.CreatedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Update.Response apply(
    OutsourcingRequestMessages.Update.Request request) {
    if (!isUpdatable()) {
      throw new OutsourcingRequestExceptions.CannotUpdateException();
    }
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.unit = request.getUnit();
    this.projectId = request.getProjectId();
    this.dueDate = request.getDueDate();
    this.supplierId = request.getSupplierId();
    this.receiverId = request.getReceiverId();
    this.receiveSiteId = request.getReceiveSiteId();
    this.receiveStationId = request.getReceiveStationId();
    this.remark = request.getRemark();
    return new OutsourcingRequestMessages.Update.Response(
      Arrays.asList(new OutsourcingRequestEvents.UpdatedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Accept.Response apply(
    OutsourcingRequestMessages.Accept.Request request) {
    if (!isAcceptable()) {
      throw new OutsourcingRequestExceptions.CannotAcceptException();
    }
    this.status = OutsourcingRequestStatusKind.ACCEPTED;
    this.accepterId = request.getAccepterId();
    this.acceptedDate = OffsetDateTime.now();
    return new OutsourcingRequestMessages.Accept.Response(
      Arrays.asList(new OutsourcingRequestEvents.AcceptedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Cancel.Response apply(
    OutsourcingRequestMessages.Cancel.Request request) {
    if (!isCancelable()) {
      throw new OutsourcingRequestExceptions.CannotCancelException();
    }
    this.status = OutsourcingRequestStatusKind.CANCELED;
    this.canceledDate = OffsetDateTime.now();
    return new OutsourcingRequestMessages.Cancel.Response(
      Arrays.asList(new OutsourcingRequestEvents.CanceledEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Complete.Response apply(
    OutsourcingRequestMessages.Complete.Request request) {
    if (!isCompletable()) {
      throw new OutsourcingRequestExceptions.CannotCompleteException();
    }
    this.status = OutsourcingRequestStatusKind.COMPLETED;
    this.completedDate = OffsetDateTime.now();
    return new OutsourcingRequestMessages.Complete.Response(
      Arrays.asList(new OutsourcingRequestEvents.CompletedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Commit.Response apply(
    OutsourcingRequestMessages.Commit.Request request) {
    if (!isCommittable() || !requesterId.equals(request.getCommitterId())) {
      throw new OutsourcingRequestExceptions.CannotCommitException();
    }
    this.status = OutsourcingRequestStatusKind.COMMITTED;
    this.committedDate = OffsetDateTime.now();
    return new OutsourcingRequestMessages.Commit.Response(
      Arrays.asList(new OutsourcingRequestEvents.CommittedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Progress.Response apply(
    OutsourcingRequestMessages.Progress.Request request) {
    if (!isProgressable()) {
      throw new OutsourcingRequestExceptions.CannotProgressException();
    }
    this.progressedQuantity = this.progressedQuantity.add(request.getProgressedQuantity());
    this.status = OutsourcingRequestStatusKind.IN_PROGRESS;
    return new OutsourcingRequestMessages.Progress.Response(
      Arrays.asList(
        new OutsourcingRequestEvents.ProgressedEvent(this.id, request.getProgressedQuantity()))
    );
  }

  public OutsourcingRequestMessages.Reject.Response apply(
    OutsourcingRequestMessages.Reject.Request request) {
    if (!isRejectable()) {
      throw new OutsourcingRequestExceptions.CannotRejectException();
    }
    this.status = OutsourcingRequestStatusKind.REJECTED;
    this.rejectedDate = OffsetDateTime.now();
    this.rejectedReason = request.getRejectedReason();
    return new OutsourcingRequestMessages.Reject.Response(
      Arrays.asList(new OutsourcingRequestEvents.RejectedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.Plan.Response apply(
    OutsourcingRequestMessages.Plan.Request request) {
    if (!this.isPlannable()) {
      throw new OutsourcingRequestExceptions.CannotPlanException();
    }
    this.status = OutsourcingRequestStatusKind.IN_PLANNING;
    return new OutsourcingRequestMessages.Plan.Response(
      Arrays.asList(new OutsourcingRequestEvents.PlannedEvent(this.id))
    );
  }

  public OutsourcingRequestMessages.CancelProgress.Response apply(
    OutsourcingRequestMessages.CancelProgress.Request request) {
    if (!this.isProgressCancelable()) {
      throw new OutsourcingRequestExceptions.CannotCancelProgressException();
    }
    this.status = OutsourcingRequestStatusKind.ACCEPTED;
    return new OutsourcingRequestMessages.CancelProgress.Response(
      Arrays.asList(new OutsourcingRequestEvents.ProgressCanceledEvent(this.id))
    );
  }

  public boolean isAcceptable() {
    return status.isAcceptable();
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isCommittable() {
    return status.isCommittable();
  }

  public boolean isCompletable() {
    return status.isCompletable();
  }

  public boolean isPlannable() {
    return status.isPlannable();
  }

  public boolean isProgressCancelable() {
    return status.isProgressCancelable();
  }

  public boolean isProgressable() {
    return status.isProgressable();
  }

  public boolean isRejectable() {
    return status.isRejectable();
  }

  public boolean isUpdatable() {
    return status.isUpdatable();
  }


}
