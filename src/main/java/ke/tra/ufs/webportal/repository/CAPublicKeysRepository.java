package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.CaPublicKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CAPublicKeysRepository extends JpaRepository<CaPublicKeys,Long> {
}
