package pico.erp.outsourcing.request.material;

import java.util.List;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.outsourcing.request.OutsourcingRequestId;
import pico.erp.outsourcing.request.material.OutsourcingRequestMaterialRequests.CreateRequest;
import pico.erp.outsourcing.request.material.OutsourcingRequestMaterialRequests.DeleteRequest;
import pico.erp.outsourcing.request.material.OutsourcingRequestMaterialRequests.UpdateRequest;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class OutsourcingRequestMaterialServiceLogic implements OutsourcingRequestMaterialService {

  @Autowired
  private OutsourcingRequestMaterialRepository outsourcingRequestMaterialRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private OutsourcingRequestMaterialMapper mapper;

  @Override
  public OutsourcingRequestMaterialData create(CreateRequest request) {
    val material = new OutsourcingRequestMaterial();
    val response = material.apply(mapper.map(request));
    if (outsourcingRequestMaterialRepository.exists(material.getId())) {
      throw new OutsourcingRequestMaterialExceptions.AlreadyExistsException();
    }
    val created = outsourcingRequestMaterialRepository.create(material);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val material = outsourcingRequestMaterialRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestMaterialExceptions.NotFoundException::new);
    val response = material.apply(mapper.map(request));
    outsourcingRequestMaterialRepository.deleteBy(material.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(OutsourcingRequestMaterialId id) {
    return outsourcingRequestMaterialRepository.exists(id);
  }

  @Override
  public OutsourcingRequestMaterialData get(OutsourcingRequestMaterialId id) {
    return outsourcingRequestMaterialRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(OutsourcingRequestMaterialExceptions.NotFoundException::new);
  }

  @Override
  public List<OutsourcingRequestMaterialData> getAll(OutsourcingRequestId requestId) {
    return outsourcingRequestMaterialRepository.findAllBy(requestId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val material = outsourcingRequestMaterialRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestMaterialExceptions.NotFoundException::new);
    val response = material.apply(mapper.map(request));
    outsourcingRequestMaterialRepository.update(material);
    eventPublisher.publishEvents(response.getEvents());
  }

}
