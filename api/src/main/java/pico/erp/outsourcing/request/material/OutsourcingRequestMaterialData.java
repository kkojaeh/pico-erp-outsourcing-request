package pico.erp.outsourcing.request.material;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.outsourcing.request.OutsourcingRequestId;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutsourcingRequestMaterialData {

  OutsourcingRequestMaterialId id;

  OutsourcingRequestId requestId;

  ItemId itemId;

  ItemSpecCode itemSpecCode;

  BigDecimal quantity;

  String remark;

  CompanyId supplierId;

  OffsetDateTime estimatedSupplyDate;


}
