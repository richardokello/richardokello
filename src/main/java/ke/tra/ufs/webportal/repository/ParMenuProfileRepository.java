package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParMenuProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParMenuProfileRepository extends CrudRepository<ParMenuProfile, BigDecimal> {
    Optional<ParMenuProfile> findByIdAndActionStatusAndIntrash(BigDecimal id, String actionStatus, String intrash);
}
