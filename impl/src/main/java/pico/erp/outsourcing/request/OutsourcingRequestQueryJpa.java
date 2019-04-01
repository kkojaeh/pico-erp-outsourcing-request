package pico.erp.outsourcing.request;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.outsourcing.request.OutsourcingRequestView.Filter;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Give
@Transactional(readOnly = true)
@Validated
public class OutsourcingRequestQueryJpa implements OutsourcingRequestQuery {


  private final QOutsourcingRequestEntity request = QOutsourcingRequestEntity.outsourcingRequestEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<OutsourcingRequestView> retrieve(Filter filter, Pageable pageable) {
    val query = new JPAQuery<OutsourcingRequestView>(entityManager);
    val select = Projections.bean(OutsourcingRequestView.class,
      request.id,
      request.code,
      request.itemId,
      request.itemSpecCode,
      request.processId,
      request.quantity,
      request.spareQuantity,
      request.progressedQuantity,
      request.unit,
      request.requesterId,
      request.accepterId,
      request.projectId,
      request.supplierId,
      request.receiverId,
      request.receiveSiteId,
      request.receiveStationId,
      request.dueDate,
      request.committedDate,
      request.completedDate,
      request.acceptedDate,
      request.rejectedDate,
      request.canceledDate,
      request.status,
      request.createdBy,
      request.createdDate
    );
    query.select(select);
    query.from(request);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getCode())) {
      builder.and(request.code.value
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")));
    }

    if (filter.getReceiverId() != null) {
      builder.and(request.receiverId.eq(filter.getReceiverId()));
    }

    if (filter.getRequesterId() != null) {
      builder.and(request.requesterId.eq(filter.getRequesterId()));
    }

    if (filter.getAccepterId() != null) {
      builder.and(request.accepterId.eq(filter.getAccepterId()));
    }

    if (filter.getProjectId() != null) {
      builder.and(request.projectId.eq(filter.getProjectId()));
    }

    if (filter.getItemId() != null) {
      builder.and(request.itemId.eq(filter.getItemId()));
    }

    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(request.status.in(filter.getStatuses()));
    }

    if (filter.getStartDueDate() != null) {
      builder.and(request.dueDate.goe(filter.getStartDueDate()));
    }
    if (filter.getEndDueDate() != null) {
      builder.and(request.dueDate.loe(filter.getEndDueDate()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }

  @Override
  public Page<OutsourcingRequestAwaitOrderView> retrieve(
    OutsourcingRequestAwaitOrderView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<OutsourcingRequestAwaitOrderView>(entityManager);
    val select = Projections.bean(OutsourcingRequestAwaitOrderView.class,
      request.id,
      request.code,
      request.itemId,
      request.itemSpecCode,
      request.processId,
      request.quantity,
      request.spareQuantity,
      request.unit,
      request.requesterId,
      request.projectId,
      request.supplierId,
      request.receiverId,
      request.receiveSiteId,
      request.receiveStationId,
      request.committedDate,
      request.acceptedDate,
      request.dueDate,
      request.createdDate
    );
    query.select(select);
    query.from(request);

    val builder = new BooleanBuilder();

    builder.and(request.status.eq(OutsourcingRequestStatusKind.ACCEPTED));

    if (filter.getReceiverId() != null) {
      builder.and(request.receiverId.eq(filter.getReceiverId()));
    }

    if (filter.getRequesterId() != null) {
      builder.and(request.requesterId.eq(filter.getRequesterId()));
    }

    if (filter.getProjectId() != null) {
      builder.and(request.projectId.eq(filter.getProjectId()));
    }

    if (filter.getItemId() != null) {
      builder.and(
        request.itemId.eq(filter.getItemId())
      );
    }

    if (filter.getStartDueDate() != null) {
      builder.and(request.dueDate.goe(filter.getStartDueDate()));
    }
    if (filter.getEndDueDate() != null) {
      builder.and(request.dueDate.loe(filter.getEndDueDate()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }

  @Override
  public Page<OutsourcingRequestAwaitAcceptView> retrieve(
    OutsourcingRequestAwaitAcceptView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<OutsourcingRequestAwaitAcceptView>(entityManager);
    val select = Projections.bean(OutsourcingRequestAwaitAcceptView.class,
      request.id,
      request.code,
      request.itemId,
      request.itemSpecCode,
      request.processId,
      request.quantity,
      request.spareQuantity,
      request.unit,
      request.requesterId,
      request.projectId,
      request.supplierId,
      request.receiverId,
      request.receiveSiteId,
      request.receiveStationId,
      request.committedDate,
      request.dueDate,
      request.createdDate
    );
    query.select(select);
    query.from(request);

    val builder = new BooleanBuilder();

    builder.and(request.status.eq(OutsourcingRequestStatusKind.COMMITTED));

    if (!isEmpty(filter.getCode())) {
      builder.and(request.code.value
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")));
    }

    if (filter.getReceiverId() != null) {
      builder.and(request.receiverId.eq(filter.getReceiverId()));
    }

    if (filter.getRequesterId() != null) {
      builder.and(request.requesterId.eq(filter.getRequesterId()));
    }

    if (filter.getProjectId() != null) {
      builder.and(request.projectId.eq(filter.getProjectId()));
    }

    if (filter.getItemId() != null) {
      builder.and(request.itemId.eq(filter.getItemId()));
    }

    if (filter.getStartDueDate() != null) {
      builder.and(request.dueDate.goe(filter.getStartDueDate()));
    }
    if (filter.getEndDueDate() != null) {
      builder.and(request.dueDate.loe(filter.getEndDueDate()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
