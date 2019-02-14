package pico.erp.outsourcing.request.material;

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
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourcing.request.OutsourcingRequest;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutsourcingRequestMaterial implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  OutsourcingRequestMaterialId id;

  OutsourcingRequest request;

  ItemId itemId;

  ItemSpecCode itemSpecCode;

  BigDecimal quantity;

  String remark;

  CompanyId supplierId;

  OffsetDateTime estimatedSupplyDate;

  public OutsourcingRequestMaterial() {

  }

  public OutsourcingRequestMaterialMessages.Create.Response apply(
    OutsourcingRequestMaterialMessages.Create.Request request) {
    if (!request.getRequest().isUpdatable()) {
      throw new OutsourcingRequestMaterialExceptions.CannotCreateException();
    }
    this.id = request.getId();
    this.request = request.getRequest();
    this.itemId = request.getItemId();
    this.itemSpecCode = request.getItemSpecCode();
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();
    this.supplierId = request.getSupplierId();
    this.estimatedSupplyDate = request.getEstimatedSupplyDate();

    return new OutsourcingRequestMaterialMessages.Create.Response(
      Arrays.asList(new OutsourcingRequestMaterialEvents.CreatedEvent(this.id))
    );
  }

  public OutsourcingRequestMaterialMessages.Update.Response apply(
    OutsourcingRequestMaterialMessages.Update.Request request) {
    if (!this.request.isUpdatable()) {
      throw new OutsourcingRequestMaterialExceptions.CannotUpdateException();
    }
    this.quantity = request.getQuantity();
    this.remark = request.getRemark();
    this.supplierId = request.getSupplierId();
    this.estimatedSupplyDate = request.getEstimatedSupplyDate();
    return new OutsourcingRequestMaterialMessages.Update.Response(
      Arrays.asList(new OutsourcingRequestMaterialEvents.UpdatedEvent(this.id))
    );
  }

  public OutsourcingRequestMaterialMessages.Delete.Response apply(
    OutsourcingRequestMaterialMessages.Delete.Request request) {
    if (!this.request.isUpdatable()) {
      throw new OutsourcingRequestMaterialExceptions.CannotDeleteException();
    }
    return new OutsourcingRequestMaterialMessages.Delete.Response(
      Arrays.asList(new OutsourcingRequestMaterialEvents.DeletedEvent(this.id))
    );
  }

}
