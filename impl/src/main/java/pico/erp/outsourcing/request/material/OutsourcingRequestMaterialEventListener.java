package pico.erp.outsourcing.request.material;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.bom.BomService;
import pico.erp.bom.material.BomMaterialService;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourcing.request.OutsourcingRequestEvents;
import pico.erp.outsourcing.request.OutsourcingRequestService;
import pico.erp.process.ProcessService;
import pico.erp.process.type.ProcessTypeEvents.CostChangedEvent;

@SuppressWarnings("unused")
@Component
public class OutsourcingRequestMaterialEventListener {

  private static final String LISTENER_NAME = "listener.outsourcing-request-material-event-listener";

  @Lazy
  @Autowired
  private ProcessService processService;

  @Lazy
  @Autowired
  private BomService bomService;

  @Lazy
  @Autowired
  private BomMaterialService bomMaterialService;

  @Lazy
  @Autowired
  private OutsourcingRequestService outsourcingRequestService;

  @Lazy
  @Autowired
  private OutsourcingRequestMaterialService outsourcingRequestMaterialService;

  @Lazy
  @Autowired
  private ItemSpecService itemSpecService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + CostChangedEvent.CHANNEL)
  public void onRequestCreated(OutsourcingRequestEvents.CreatedEvent event) {
    val request = outsourcingRequestService.get(event.getId());
    val processes = processService.getAll(request.getItemId());

    val process = processes.stream().filter(p -> p.getId().equals(request.getProcessId())).findAny()
      .get();
    val index = processes.indexOf(process);
    val isFirst = index == 0;
    if (isFirst) {
      val exists = bomService.exists(request.getItemId());
      if (exists) {
        val bom = bomService.get(request.getItemId());
        val materials = bomMaterialService.getAll(bom.getId());
        materials.forEach(material -> {
          val itemId = material.getItemId();
          val itemSpecCode = Optional.ofNullable(material.getItemSpecId())
            .map(specId -> itemSpecService.get(specId).getCode())
            .orElse(ItemSpecCode.NOT_APPLICABLE);
          val quantity = material.getQuantity().multiply(request.getQuantity()).multiply(
            BigDecimal.ONE.add(process.getLossRate())
          );
          outsourcingRequestMaterialService.create(
            OutsourcingRequestMaterialRequests.CreateRequest.builder()
              .id(OutsourcingRequestMaterialId.generate())
              .requestId(request.getId())
              .itemId(material.getItemId())
              .itemSpecCode(itemSpecCode)
              .quantity(quantity)
              .build()
          );
        });
      }
    } else {
      val previous = processes.get(index - 1);
      val quantity = request.getQuantity().multiply(
        BigDecimal.ONE.add(process.getLossRate())
      );
      outsourcingRequestMaterialService.create(
        OutsourcingRequestMaterialRequests.CreateRequest.builder()
          .id(OutsourcingRequestMaterialId.generate())
          .requestId(request.getId())
          .itemId(request.getItemId())
          .itemSpecCode(previous.getItemSpecCode())
          .quantity(quantity)
          .build()
      );
    }

  }

}
