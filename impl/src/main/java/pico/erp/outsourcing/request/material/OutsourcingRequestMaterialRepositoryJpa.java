package pico.erp.outsourcing.request.material;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.outsourcing.request.OutsourcingRequestId;

@Repository
interface OutsourcingRequestItemEntityRepository extends
  CrudRepository<OutsourcingRequestMaterialEntity, OutsourcingRequestMaterialId> {

  @Query("SELECT m FROM OutsourcingRequestMaterial m WHERE m.requestId = :requestId ORDER BY m.createdDate")
  Stream<OutsourcingRequestMaterialEntity> findAllBy(@Param("requestId") OutsourcingRequestId planId);

}

@Repository
@Transactional
public class OutsourcingRequestMaterialRepositoryJpa implements
  OutsourcingRequestMaterialRepository {

  @Autowired
  private OutsourcingRequestItemEntityRepository repository;

  @Autowired
  private OutsourcingRequestMaterialMapper mapper;

  @Override
  public OutsourcingRequestMaterial create(OutsourcingRequestMaterial planItem) {
    val entity = mapper.jpa(planItem);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(OutsourcingRequestMaterialId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(OutsourcingRequestMaterialId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<OutsourcingRequestMaterial> findAllBy(OutsourcingRequestId requestId) {
    return repository.findAllBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<OutsourcingRequestMaterial> findBy(OutsourcingRequestMaterialId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(OutsourcingRequestMaterial material) {
    val entity = repository.findOne(material.getId());
    mapper.pass(mapper.jpa(material), entity);
    repository.save(entity);
  }
}
