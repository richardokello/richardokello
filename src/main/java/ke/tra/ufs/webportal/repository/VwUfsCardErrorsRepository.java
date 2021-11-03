package ke.tra.ufs.webportal.repository;
import ke.tra.ufs.webportal.entities.views.VwUfsCardErrors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwUfsCardErrorsRepository extends JpaRepository<VwUfsCardErrors, String> {

    List<VwUfsCardErrors> findAllByTerminalId(String id);
}
