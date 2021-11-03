package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsWorkgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsWorkgroupRepository extends JpaRepository<UfsWorkgroup,Long> {
    UfsWorkgroup findByGroupName(String groupName);
}
