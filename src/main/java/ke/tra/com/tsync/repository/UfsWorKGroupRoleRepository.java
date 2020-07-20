package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsWorkgroupRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



import java.util.List;

@Repository
public interface UfsWorKGroupRoleRepository extends CrudRepository<UfsWorkgroupRole, Long>{
    List<UfsWorkgroupRole>findAllByGroupIdIn(List<Long> groupId);
}

