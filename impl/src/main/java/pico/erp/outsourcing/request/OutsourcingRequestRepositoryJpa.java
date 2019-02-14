package pico.erp.outsourcing.request;

import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface OutsourcingRequestEntityRepository extends
  CrudRepository<OutsourcingRequestEntity, OutsourcingRequestId> {

  @Query("SELECT COUNT(r) FROM OutsourcingRequest r WHERE r.createdDate >= :begin AND r.createdDate <= :end")
  long countCreatedBetween(@Param("begin") OffsetDateTime begin, @Param("end") OffsetDateTime end);

}

@Repository
@Transactional
public class OutsourcingRequestRepositoryJpa implements OutsourcingRequestRepository {

  @Autowired
  private OutsourcingRequestEntityRepository repository;

  @Autowired
  private OutsourcingRequestMapper mapper;

  @Override
  public long countCreatedBetween(OffsetDateTime begin, OffsetDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public OutsourcingRequest create(OutsourcingRequest plan) {
    val entity = mapper.jpa(plan);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(OutsourcingRequestId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(OutsourcingRequestId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<OutsourcingRequest> findBy(OutsourcingRequestId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(OutsourcingRequest plan) {
    val entity = repository.findOne(plan.getId());
    mapper.pass(mapper.jpa(plan), entity);
    repository.save(entity);
  }
}
