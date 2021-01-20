package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsUserWorkgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsUserWorkgroupRepository extends JpaRepository<UfsUserWorkgroup,Long> {
    UfsUserWorkgroup findByUserId(Long userIds);
}
