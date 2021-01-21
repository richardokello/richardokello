package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsUserAgentSupervisor;
import ke.tracom.ufs.entities.UfsUserBranchManagers;
import ke.tracom.ufs.entities.UfsUserRegionMap;

public interface UserTypesService {
    public UfsUserAgentSupervisor saveSupervisor(UfsUserAgentSupervisor ufsUserAgentSupervisor);

    public UfsUserRegionMap saveRegionhead(UfsUserRegionMap userRegionMap);

    public UfsUserBranchManagers saveBranchManagers(UfsUserBranchManagers branchManagers);
}
