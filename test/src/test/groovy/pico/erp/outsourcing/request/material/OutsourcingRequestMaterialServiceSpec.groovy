package pico.erp.outsourcing.request.material

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.ItemId
import pico.erp.item.spec.ItemSpecCode
import pico.erp.outsourcing.request.OutsourcingRequestId
import pico.erp.outsourcing.request.OutsourcingRequestRequests
import pico.erp.outsourcing.request.OutsourcingRequestService
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.data.UnitKind
import pico.erp.user.UserId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class OutsourcingRequestMaterialServiceSpec extends Specification {

  @Autowired
  OutsourcingRequestService requestService

  @Autowired
  OutsourcingRequestMaterialService requestItemService

  def requestId = OutsourcingRequestId.from("outsourcing-request-1")

  def id = OutsourcingRequestMaterialId.from("outsourcing-request-material-1")

  def unknownId = OutsourcingRequestMaterialId.from("unknown")

  def itemId = ItemId.from("item-1")

  def requesterId = UserId.from("kjh")

  def accepterId = UserId.from("kjh")

  def itemSpecCode = ItemSpecCode.NOT_APPLICABLE

  def unit = UnitKind.EA

  def setup() {

  }

  def cancelRequest() {
    requestService.cancel(
      new OutsourcingRequestRequests.CancelRequest(
        id: requestId
      )
    )
  }

  def commitRequest() {
    requestService.commit(
      new OutsourcingRequestRequests.CommitRequest(
        id: requestId,
        committerId: requesterId
      )
    )
  }

  def rejectRequest() {
    requestService.reject(
      new OutsourcingRequestRequests.RejectRequest(
        id: requestId,
        rejectedReason: "필요 없는것 같음"
      )
    )
  }

  def progressRequest() {
    requestService.progress(
      new OutsourcingRequestRequests.ProgressRequest(
        id: requestId
      )
    )
  }

  def planRequest() {
    requestService.plan(
      new OutsourcingRequestRequests.PlanRequest(
        id: requestId
      )
    )
  }

  def completeRequest() {
    requestService.complete(
      new OutsourcingRequestRequests.CompleteRequest(
        id: requestId
      )
    )
  }

  def acceptRequest() {
    requestService.accept(
      new OutsourcingRequestRequests.AcceptRequest(
        id: requestId,
        accepterId: accepterId
      )
    )
  }

  def createMaterial() {
    requestItemService.create(
      new OutsourcingRequestMaterialRequests.CreateRequest(
        id: id,
        requestId: requestId,
        itemId: itemId,
        itemSpecCode: itemSpecCode,
        quantity: 100,
        unit: unit,
        remark: "품목 비고"
      )
    )
  }

  def createMaterial2() {
    requestItemService.create(
      new OutsourcingRequestMaterialRequests.CreateRequest(
        id: OutsourcingRequestMaterialId.from("outsourcing-request-material-2"),
        requestId: requestId,
        itemId: itemId,
        itemSpecCode: itemSpecCode,
        quantity: 100,
        unit: unit,
        remark: "품목 비고"
      )
    )
  }

  def updateMaterial() {
    requestItemService.update(
      new OutsourcingRequestMaterialRequests.UpdateRequest(
        id: id,
        quantity: 200,
        unit: unit,
        remark: "품목 비고2"
      )
    )
  }

  def deleteMaterial() {
    requestItemService.delete(
      new OutsourcingRequestMaterialRequests.DeleteRequest(
        id: id
      )
    )
  }


  def "존재 - 아이디로 확인"() {
    when:
    createMaterial()
    def exists = requestItemService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = requestItemService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    createMaterial()
    def item = requestItemService.get(id)
    then:
    item.id == id
    item.itemId == itemId
    item.requestId == requestId
    item.quantity == 100
    item.unit == unit
    item.remark == "품목 비고"

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    requestItemService.get(unknownId)

    then:
    thrown(OutsourcingRequestMaterialExceptions.NotFoundException)
  }

  def "생성 - 작성 후 생성"() {
    when:
    createMaterial()
    def items = requestItemService.getAll(requestId)
    then:
    items.size() > 0
  }

  def "생성 - 제출 후 생성"() {
    when:
    createMaterial()
    commitRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)

  }

  def "생성 - 접수 후 생성"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)
  }

  def "생성 - 진행 중 생성"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)

  }

  def "생성 - 취소 후 생성"() {
    when:
    createMaterial()
    cancelRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)
  }

  def "생성 - 반려 후 생성"() {
    when:
    createMaterial()
    commitRequest()
    rejectRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)
  }

  def "생성 - 완료 후 생성"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    createMaterial2()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotCreateException)
  }

  def "수정 - 작성 후 수정"() {
    when:
    createMaterial()
    updateMaterial()
    def items = requestItemService.getAll(requestId)
    then:
    items.size() > 0
  }

  def "수정 - 제출 후 수정"() {
    when:
    createMaterial()
    commitRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)

  }

  def "수정 - 접수 후 수정"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)
  }

  def "수정 - 진행 중 수정"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)

  }

  def "수정 - 취소 후 수정"() {
    when:
    createMaterial()
    cancelRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)
  }

  def "수정 - 반려 후 수정"() {
    when:
    createMaterial()
    commitRequest()
    rejectRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)
  }

  def "수정 - 완료 후 수정"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    updateMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotUpdateException)
  }


  def "삭제 - 작성 후 삭제"() {
    when:
    createMaterial()
    def previous = requestItemService.getAll(requestId).size()
    deleteMaterial()
    def size = requestItemService.getAll(requestId).size()
    then:
    previous - 1 == size
  }

  def "삭제 - 제출 후 삭제"() {
    when:
    createMaterial()
    commitRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)

  }

  def "삭제 - 접수 후 삭제"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)
  }

  def "삭제 - 진행 중 삭제"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)

  }

  def "삭제 - 취소 후 삭제"() {
    when:
    createMaterial()
    cancelRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)
  }

  def "삭제 - 반려 후 삭제"() {
    when:
    createMaterial()
    commitRequest()
    rejectRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)
  }

  def "삭제 - 완료 후 삭제"() {
    when:
    createMaterial()
    commitRequest()
    acceptRequest()
    planRequest()
    progressRequest()
    completeRequest()
    deleteMaterial()
    then:
    thrown(OutsourcingRequestMaterialExceptions.CannotDeleteException)
  }


}
