package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsUserWorkgroup;
import ke.tracom.ufs.entities.UfsWorkgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

public interface WorkGroupService {
    void deleteAllByUserId(Long userId);

    void saveAll(List<UfsUserWorkgroup> wkgroups);

    public UfsWorkgroup saveWorkgroup(UfsWorkgroup ufsWorkgroup);

    public UfsWorkgroup findWorkgroupById(Long groupId);
    UfsWorkgroup findByUserId(long userid);
}
