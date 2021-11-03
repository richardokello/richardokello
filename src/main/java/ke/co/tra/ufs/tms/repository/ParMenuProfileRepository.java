package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParMenuProfile;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

public interface ParMenuProfileRepository extends CrudRepository<ParMenuProfile, BigDecimal> {
    Optional<ParMenuProfile> findByIdAndActionStatusAndIntrash(BigDecimal id, String actionStatus, String intrash);
}
