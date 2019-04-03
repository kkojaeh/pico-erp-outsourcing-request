package pico.erp.outsourcing.request.material;

import java.math.BigDecimal;
import java.util.Optional;
import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.bom.BomService;
import pico.erp.bom.material.BomMaterialService;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.outsourcing.request.OutsourcingRequestEvents;
import pico.erp.outsourcing.request.OutsourcingRequestService;
import pico.erp.process.ProcessService;

@SuppressWarnings("unused")
@Component
public class OutsourcingRequestMaterialEventListener {

  private static final String LISTENER_NAME = "listener.outsourcing-request-material-event-listener";

  @ComponentAutowired
  private ProcessService processService;

  @ComponentAutowired
  private BomService bomService;

  @ComponentAutowired
  private BomMaterialService bomMaterialService;

  @Lazy
  @Autowired
  private OutsourcingRequestService outsourcingRequestService;

  @Lazy
  @Autowired
  private OutsourcingRequestMaterialService outsourcingRequestMaterialService;

  @ComponentAutowired
  private ItemSpecService itemSpecService;

  @ComponentAutowired
  private ItemService itemService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + OutsourcingRequestEvents.CreatedEvent.CHANNEL)
  public void onRequestCreated(OutsourcingRequestEvents.CreatedEvent event) {
    val requestId = event.getId();
    if (event.isMaterialsManually()) {
      return;
    }
    val request = outsourcingRequestService.get(requestId);
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
          val item = itemService.get(itemId);
          val unit = item.getUnit();
          val itemSpecCode = Optional.ofNullable(material.getItemSpecId())
            .map(specId -> itemSpecService.get(specId).getCode())
            .orElse(ItemSpecCode.NOT_APPLICABLE);
          val quantity = material.getQuantity().multiply(request.getQuantity()).multiply(
            BigDecimal.ONE.add(process.getLossRate())
          );

          outsourcingRequestMaterialService.create(
            OutsourcingRequestMaterialRequests.CreateRequest.builder()
              .id(OutsourcingRequestMaterialId.generate())
              .requestId(requestId)
              .itemId(material.getItemId())
              .itemSpecCode(itemSpecCode)
              .quantity(quantity)
              .unit(unit)
              .build()
          );
        });
      }
    } else {
      val previous = processes.get(index - 1);
      val quantity = request.getQuantity().multiply(
        BigDecimal.ONE.add(process.getLossRate())
      );
      val itemId = request.getItemId();
      val item = itemService.get(itemId);
      val itemSpecCode = previous.getItemSpecCode();
      val unit = item.getUnit();
      outsourcingRequestMaterialService.create(
        OutsourcingRequestMaterialRequests.CreateRequest.builder()
          .id(OutsourcingRequestMaterialId.generate())
          .requestId(requestId)
          .itemId(itemId)
          .itemSpecCode(itemSpecCode)
          .quantity(quantity)
          .unit(unit)
          .build()
      );
    }

  }

}
