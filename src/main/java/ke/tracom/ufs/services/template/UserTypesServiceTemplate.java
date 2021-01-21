package ke.tracom.ufs.services.template;

import ke.tracom.ufs.entities.UfsUserAgentSupervisor;
import ke.tracom.ufs.entities.UfsUserBranchManagers;
import ke.tracom.ufs.entities.UfsUserRegionMap;
import ke.tracom.ufs.repositories.*;
import ke.tracom.ufs.services.UserTypesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserTypesServiceTemplate implements UserTypesService {
    private final UfsUserRegionMapRepository regionMapRepository;
    private final UfsUserAgentSupervisorRepository supervisorRepository;
    private final UfsUserBranchManagersRepository branchManagersRepository;

    public UserTypesServiceTemplate(UfsUserRegionMapRepository regionMapRepository, UfsUserAgentSupervisorRepository supervisorRepository, UfsUserBranchManagersRepository branchManagersRepository) {
        this.regionMapRepository = regionMapRepository;
        this.supervisorRepository = supervisorRepository;
        this.branchManagersRepository = branchManagersRepository;
    }

    @Override
    public UfsUserAgentSupervisor saveSupervisor(UfsUserAgentSupervisor ufsUserAgentSupervisor) {
        return supervisorRepository.save(ufsUserAgentSupervisor);
    }

    @Override
    public UfsUserRegionMap saveRegionhead(UfsUserRegionMap userRegionMap) {
        return regionMapRepository.save(userRegionMap);
    }

    @Override
    public UfsUserBranchManagers saveBranchManagers(UfsUserBranchManagers branchManagers) {
        return branchManagersRepository.save(branchManagers);
    }
}
