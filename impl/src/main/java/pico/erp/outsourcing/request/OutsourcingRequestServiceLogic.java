package pico.erp.outsourcing.request;

import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.AcceptRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.CancelProgressRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.CancelRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.CommitRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.CompleteRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.PlanRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.ProgressRequest;
import pico.erp.outsourcing.request.OutsourcingRequestRequests.RejectRequest;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class OutsourcingRequestServiceLogic implements OutsourcingRequestService {

  @Autowired
  private OutsourcingRequestRepository outsourcingRequestRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private OutsourcingRequestMapper mapper;

  @Override
  public void accept(AcceptRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(CancelRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancelProgress(CancelProgressRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void commit(CommitRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public OutsourcingRequestData create(OutsourcingRequestRequests.CreateRequest request) {
    val outsourcingRequest = new OutsourcingRequest();
    val response = outsourcingRequest.apply(mapper.map(request));
    if (outsourcingRequestRepository.exists(outsourcingRequest.getId())) {
      throw new OutsourcingRequestExceptions.AlreadyExistsException();
    }
    val created = outsourcingRequestRepository.create(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(OutsourcingRequestId id) {
    return outsourcingRequestRepository.exists(id);
  }

  @Override
  public OutsourcingRequestData get(OutsourcingRequestId id) {
    return outsourcingRequestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
  }

  @Override
  public void plan(PlanRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void progress(ProgressRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void reject(RejectRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(OutsourcingRequestRequests.UpdateRequest request) {
    val outsourcingRequest = outsourcingRequestRepository.findBy(request.getId())
      .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new);
    val response = outsourcingRequest.apply(mapper.map(request));
    outsourcingRequestRepository.update(outsourcingRequest);
    eventPublisher.publishEvents(response.getEvents());
  }
}
