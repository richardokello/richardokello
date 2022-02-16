package ke.tracom.ufs.services.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tracom.ufs.entities.UfsUserWorkgroup;
import ke.tracom.ufs.entities.UfsWorkgroup;
import ke.tracom.ufs.repositories.UfsUserWorkgroupRepository;
import ke.tracom.ufs.repositories.WorkgroupRepository;
import ke.tracom.ufs.services.WorkGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WorkGroupServiceTemplate implements WorkGroupService {
    private final UfsUserWorkgroupRepository ufsUserWorkgroupRepository;
    private final WorkgroupRepository workgroupRepository;

    public WorkGroupServiceTemplate(UfsUserWorkgroupRepository ufsUserWorkgroupRepository,WorkgroupRepository workgroupRepository) {
        this.ufsUserWorkgroupRepository = ufsUserWorkgroupRepository;
        this.workgroupRepository = workgroupRepository;
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        ufsUserWorkgroupRepository.deleteAllByUserId(userId);
    }


    @Override
    public void saveAll(List<UfsUserWorkgroup> wkgroups) {
        ufsUserWorkgroupRepository.saveAll(wkgroups);
    }

    @Override
    public UfsWorkgroup saveWorkgroup(UfsWorkgroup ufsWorkgroup) {
        return workgroupRepository.save(ufsWorkgroup);
    }

    @Override
    public UfsWorkgroup findWorkgroupById(Long groupId) {
        return workgroupRepository.findByGroupIdAndIntrash(groupId, AppConstants.NO);
    }

    @Override
    public UfsWorkgroup findByUserId(long userid) {
        return workgroupRepository.findByUserId(userid);
    }
}
