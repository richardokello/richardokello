package ke.tracom.ufs.services.template;

import ke.tracom.ufs.entities.UfsUserWorkgroup;
import ke.tracom.ufs.repositories.UfsUserWorkgroupRepository;
import ke.tracom.ufs.services.UserWorkGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserWorkGroupServiceTemplate implements UserWorkGroupService {

    private final UfsUserWorkgroupRepository ufsUserWorkgroupRepository;

    public UserWorkGroupServiceTemplate(UfsUserWorkgroupRepository ufsUserWorkgroupRepository) {
        this.ufsUserWorkgroupRepository = ufsUserWorkgroupRepository;
    }

    @Override
    public List<UfsUserWorkgroup> findAllByUserId(Long userid) {
        return ufsUserWorkgroupRepository.findAllByUserId(userid);
    }
}
