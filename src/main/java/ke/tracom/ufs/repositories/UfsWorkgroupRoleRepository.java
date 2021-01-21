package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsWorkgroupRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UfsWorkgroupRoleRepository extends CrudRepository<UfsWorkgroupRole, Long> {
    List<UfsWorkgroupRole> findAllByGroupId(Long groupId);

    List<UfsWorkgroupRole> findAllByGroupIdAndAndRoleIdIn(Long groupId, List<Long> roleIds);

}
