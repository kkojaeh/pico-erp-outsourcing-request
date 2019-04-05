package pico.erp.outsourcing.request

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.ItemId
import pico.erp.process.ProcessId
import pico.erp.process.ProcessService
import pico.erp.project.ProjectId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.UnitKind
import pico.erp.user.UserId
import pico.erp.warehouse.location.site.SiteId
import pico.erp.warehouse.location.station.StationId
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [OutsourcingRequestApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class OutsourcingRequestServiceSpec extends Specification {

  @Autowired
  OutsourcingRequestService requestService

  def id = OutsourcingRequestId.from("request-1")

  def unknownId = OutsourcingRequestId.from("unknown")

  def projectId = ProjectId.from("sample-project1")

  def dueDate = OffsetDateTime.now().plusDays(7)

  def remark = "요청 비고"

  def receiverId = CompanyId.from("CUST1")

  def supplierId = CompanyId.from("SUPP1")

  def requesterId = UserId.from("kjh")

  def accepterId = UserId.from("kjh")

  def receiverId2 = CompanyId.from("CUST2")

  def projectId2 = ProjectId.from("sample-project2")

  def dueDate2 = OffsetDateTime.now().plusDays(8)

  def remark2 = "요청 비고2"

  def receiveSiteId = SiteId.from("A1")

  def receiveStationId = StationId.from("S1")

  def receiveStationId2 = StationId.from("S2")

  def itemId = ItemId.from("toothbrush-05")

  def processId = ProcessId.from("toothbrush-051")

  def quantity = 1000

  def spareQuantity = 50

  def itemSpecCode

  def unit = UnitKind.EA

  @Lazy
  @Autowired
  ProcessService processService

  def setup() {
    def process = processService.get(processId)
    itemSpecCode = process.itemSpecCode
    requestService.create(
      new OutsourcingRequestRequests.CreateRequest(
        id: id,
        itemId: itemId,
        itemSpecCode: itemSpecCode,
        processId: processId,
        quantity: quantity,
        spareQuantity: spareQuantity,
        unit: unit,
        projectId: projectId,
        supplierId: supplierId,
        receiverId: receiverId,
        receiveSiteId: receiveSiteId,
        receiveStationId: receiveStationId,
        requesterId: requesterId,
        dueDate: dueDate,
        remark: remark,
      )
    )
  }

  def cancelRequest() {
    requestService.cancel(
      new OutsourcingRequestRequests.CancelRequest(
        id: id
      )
    )
  }

  def commitRequest() {
    requestService.commit(
      new OutsourcingRequestRequests.CommitRequest(
        id: id,
        committerId: requesterId
      )
    )
  }

  def rejectRequest() {
    requestService.reject(
      new OutsourcingRequestRequests.RejectRequest(
        id: id,
        rejectedReason: "필요 없는것 같음"
      )
    )
  }

  def progressRequest() {
    requestService.progress(
      new OutsourcingRequestRequests.ProgressRequest(
        id: id,
        progressedQuantity: 50
      )
    )
  }

  def planRequest() {
    requestService.plan(
      new OutsourcingRequestRequests.PlanRequest(
        id: id
      )
    )
  }

  def completeRequest() {
    requestService.complete(
      new OutsourcingRequestRequests.CompleteRequest(
        id: id
      )
    )
  }

  def acceptRequest() {
    requestService.accept(
      new OutsourcingRequestRequests.AcceptRequest(
        id: id,
        accepterId: accepterId
      )
    )
  }

  def updateRequest() {
    requestService.update(
      new OutsourcingRequestRequests.UpdateRequest(
        id: id,
        quantity: quantity,
        spareQuantity: spareQuantity,
        unit: unit,
        projectId: projectId2,
        supplierId: supplierId,
        receiverId: receiverId2,
        receiveStationId: receiveStationId2,
        dueDate: dueDate2,
        remark: remark2,
      )
    )
  }


  def "존재 - 아이디로 존재 확인"() {
    when:
    def exists = requestService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = requestService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def request = requestService.get(id)

    then:
    request.id == id
    request.itemId == itemId
    request.itemSpecCode == itemSpecCode
    request.processId == processId
    request.quantity == quantity
    request.spareQuantity == spareQuantity
    request.unit == unit
    request.supplierId == supplierId
    request.receiverId == receiverId
    request.receiveSiteId == receiveSiteId
    request.receiveStationId == receiveStationId
    request.remark == remark
    request.projectId == projectId
    request.requesterId == requesterId
    request.dueDate == dueDate
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    requestService.get(unknownId)

    then:
    thrown(OutsourcingRequestExceptions.NotFoundException)
  }


  def "수정 - 취소 후 수정"() {
    when:
    cancelRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }

  def "수정 - 접수 후 수정"() {
    when:
    commitRequest()
    acceptRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }

  def "수정 - 제출 후 수정"() {
    when:
    commitRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }

  def "수정 - 완료 후 수정"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }

  def "수정 - 반려 후 수정"() {
    when:
    commitRequest()
    rejectRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }

  def "수정 - 진행 후 수정"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    updateRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotUpdateException)
  }


  def "수정 - 작성 후 수정"() {
    when:
    updateRequest()
    def request = requestService.get(id)

    then:
    request.projectId == projectId2
    request.receiverId == receiverId2
    request.receiveStationId == receiveStationId2
    request.dueDate == dueDate2
    request.remark == remark2
  }

  def "제출 - 작성 후 제출"() {
    when:
    commitRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.COMMITTED
    request.committedDate != null
  }

  def "제출 - 제출 후 제출"() {
    when:
    commitRequest()
    commitRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCommitException)
  }

  def "제출 - 진행 중 제출"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    commitRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCommitException)
  }

  def "제출 - 취소 후 제출"() {
    when:
    cancelRequest()
    commitRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCommitException)
  }

  def "제출 - 반려 후 제출"() {
    when:
    commitRequest()
    rejectRequest()
    commitRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCommitException)
  }

  def "제출 - 완료 후 제출"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    commitRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCommitException)
  }

  def "접수 - 작성 후 접수"() {
    when:
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "접수 - 접수 후 접수"() {
    when:
    commitRequest()
    acceptRequest()
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "접수 - 제출 후 접수"() {
    when:
    commitRequest()
    acceptRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.ACCEPTED
  }

  def "접수 - 진행 중 접수"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "접수 - 취소 후 접수"() {
    when:
    cancelRequest()
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "접수 - 반려 후 접수"() {
    when:
    commitRequest()
    rejectRequest()
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "접수 - 완료 후 접수"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    acceptRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotAcceptException)
  }

  def "진행 - 작성 후 진행"() {
    when:
    progressRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotProgressException)
  }

  def "진행 - 제출 후 진행"() {
    when:
    commitRequest()
    progressRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotProgressException)
  }

  def "진행 - 접수 후 진행"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.IN_PROGRESS
  }

  def "진행 - 진행 중 진행"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    progressRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.IN_PROGRESS
  }

  def "진행 - 취소 후 진행"() {
    when:
    cancelRequest()
    progressRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotProgressException)
  }

  def "진행 - 반려 후 진행"() {
    when:
    commitRequest()
    rejectRequest()
    progressRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotProgressException)
  }

  def "진행 - 완료 후 진행"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    progressRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotProgressException)
  }

  def "취소 - 취소 후에는 취소"() {
    when:
    cancelRequest()
    cancelRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCancelException)
  }

  def "취소 - 제출 후 취소"() {
    when:
    commitRequest()
    cancelRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.CANCELED
  }

  def "취소 - 접수 후 취소"() {
    when:
    commitRequest()
    acceptRequest()
    cancelRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.CANCELED
  }

  def "취소 - 진행 후 취소"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    cancelRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCancelException)
  }

  def "취소 - 반려 후 취소"() {
    when:
    commitRequest()
    rejectRequest()
    cancelRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCancelException)
  }

  def "취소 - 완료 후 취소"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    cancelRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCancelException)
  }

  def "반려 - 작성 후 반려"() {
    when:
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "반려 - 제출 후 반려"() {
    when:
    commitRequest()
    rejectRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.REJECTED

  }

  def "반려 - 접수 후 반려"() {
    when:
    commitRequest()
    acceptRequest()
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "반려 - 진행 중 반려"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "반려 - 취소 후 반려"() {
    when:
    cancelRequest()
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "반려 - 반려 후 반려"() {
    when:
    commitRequest()
    rejectRequest()
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "반려 - 완료 후 반려"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    rejectRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotRejectException)
  }

  def "완료 - 작성 후 완료"() {
    when:
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)
  }

  def "완료 - 제출 후 완료"() {
    when:
    commitRequest()
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)

  }

  def "완료 - 접수 후 완료"() {
    when:
    commitRequest()
    acceptRequest()
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)
  }

  def "완료 - 진행 중 완료"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    def request = requestService.get(id)
    then:
    request.status == OutsourcingRequestStatusKind.COMPLETED

  }

  def "완료 - 취소 후 완료"() {
    when:
    cancelRequest()
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)
  }

  def "완료 - 반려 후 완료"() {
    when:
    commitRequest()
    rejectRequest()
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)
  }

  def "완료 - 완료 후 완료"() {
    when:
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    completeRequest()
    then:
    thrown(OutsourcingRequestExceptions.CannotCompleteException)
  }


}
