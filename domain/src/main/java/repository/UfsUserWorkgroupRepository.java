package repository;

import entities.UfsUserWorkgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UfsUserWorkgroupRepository extends JpaRepository<UfsUserWorkgroup,Long> {
    UfsUserWorkgroup findByUserId(Long userIds);
    List<UfsUserWorkgroup> findAllByUserId(Long userId);
}
