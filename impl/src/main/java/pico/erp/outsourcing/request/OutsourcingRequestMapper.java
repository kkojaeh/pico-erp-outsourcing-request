package pico.erp.outsourcing.request;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.project.ProjectData;
import pico.erp.project.ProjectId;
import pico.erp.project.ProjectService;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;
import pico.erp.warehouse.location.site.SiteData;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.site.SiteService;
import pico.erp.warehouse.location.station.StationData;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.location.station.StationService;

@Mapper
public abstract class OutsourcingRequestMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Lazy
  @Autowired
  protected ItemSpecService itemSpecService;

  @Autowired
  protected OutsourcingRequestCodeGenerator outsourcingRequestCodeGenerator;

  @Lazy
  @Autowired
  private CompanyService companyService;

  @Lazy
  @Autowired
  private UserService userService;

  @Lazy
  @Autowired
  private OutsourcingRequestRepository outsourcingRequestRepository;

  @Lazy
  @Autowired
  private ProjectService projectService;

  @Lazy
  @Autowired
  private SiteService siteService;

  @Lazy
  @Autowired
  private StationService stationService;

  @Mappings({
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract OutsourcingRequestEntity jpa(OutsourcingRequest data);

  public OutsourcingRequest jpa(OutsourcingRequestEntity entity) {
    return OutsourcingRequest.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .itemId(entity.getItemId())
      .itemSpecCode(entity.getItemSpecCode())
      .processId(entity.getProcessId())
      .quantity(entity.getQuantity())
      .spareQuantity(entity.getSpareQuantity())
      .projectId(entity.getProjectId())
      .dueDate(entity.getDueDate())
      .supplierId(entity.getSupplierId())
      .receiverId(entity.getReceiverId())
      .receiveSiteId(entity.getReceiveSiteId())
      .receiveStationId(entity.getReceiveStationId())
      .remark(entity.getRemark())
      .requesterId(entity.getRequesterId())
      .accepterId(entity.getAccepterId())
      .committedDate(entity.getCommittedDate())
      .acceptedDate(entity.getAcceptedDate())
      .completedDate(entity.getCompletedDate())
      .rejectedDate(entity.getRejectedDate())
      .canceledDate(entity.getCanceledDate())
      .status(entity.getStatus())
      .rejectedReason(entity.getRejectedReason())
      .build();
  }

  protected UserData map(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected ProjectData map(ProjectId projectId) {
    return Optional.ofNullable(projectId)
      .map(projectService::get)
      .orElse(null);
  }

  public OutsourcingRequest map(OutsourcingRequestId outsourcingRequestId) {
    return Optional.ofNullable(outsourcingRequestId)
      .map(id -> outsourcingRequestRepository.findBy(id)
        .orElseThrow(OutsourcingRequestExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }

  protected StationData map(StationId stationId) {
    return Optional.ofNullable(stationId)
      .map(stationService::get)
      .orElse(null);
  }

  protected SiteData map(SiteId siteId) {
    return Optional.ofNullable(siteId)
      .map(siteService::get)
      .orElse(null);
  }

  public abstract OutsourcingRequestData map(OutsourcingRequest outsourcingRequest);

  @Mappings({
    @Mapping(target = "codeGenerator", expression = "java(outsourcingRequestCodeGenerator)")
  })
  public abstract OutsourcingRequestMessages.Create.Request map(
    OutsourcingRequestRequests.CreateRequest request);

  public abstract OutsourcingRequestMessages.Update.Request map(
    OutsourcingRequestRequests.UpdateRequest request);


  public abstract OutsourcingRequestMessages.Accept.Request map(
    OutsourcingRequestRequests.AcceptRequest request);

  public abstract OutsourcingRequestMessages.Commit.Request map(
    OutsourcingRequestRequests.CommitRequest request);

  public abstract OutsourcingRequestMessages.Complete.Request map(
    OutsourcingRequestRequests.CompleteRequest request);

  public abstract OutsourcingRequestMessages.Cancel.Request map(
    OutsourcingRequestRequests.CancelRequest request);

  public abstract OutsourcingRequestMessages.Reject.Request map(
    OutsourcingRequestRequests.RejectRequest request);

  public abstract OutsourcingRequestMessages.Progress.Request map(
    OutsourcingRequestRequests.ProgressRequest request);

  public abstract void pass(
    OutsourcingRequestEntity from, @MappingTarget OutsourcingRequestEntity to);


}


