package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDeviceTids;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface TmsDeviceTidRepository extends CrudRepository<TmsDeviceTids, Long> {

    List<TmsDeviceTids> findAllByDeviceIds(Long id);

    Set<TmsDeviceTids> findAllByDeviceIdsIn(Set<Long> ids);

}
