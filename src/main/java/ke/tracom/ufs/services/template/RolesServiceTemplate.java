package ke.tracom.ufs.services.template;


import ke.axle.chassis.utils.AppConstants;
import ke.tracom.ufs.entities.UfsRole;
import ke.tracom.ufs.entities.UfsRolePermission;
import ke.tracom.ufs.repositories.RolePermissionRepository;
import ke.tracom.ufs.repositories.UfsRoleRepository;
import ke.tracom.ufs.services.RolesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolesServiceTemplate implements RolesService {

    private final RolePermissionRepository permRepository;
    private final UfsRoleRepository ufsRoleRepository;

    public RolesServiceTemplate(RolePermissionRepository permRepository,UfsRoleRepository ufsRoleRepository) {
        this.permRepository = permRepository;
        this.ufsRoleRepository = ufsRoleRepository;
    }

    @Override
    public List<UfsRolePermission> findByRole(UfsRole role) {
        return permRepository.findByRole(role);
    }

    @Override
    public void deleteRolePermissions(List<UfsRolePermission> mlkRolePermissions) {
        permRepository.deleteAll(mlkRolePermissions);
    }

    @Override
    public UfsRole findRoleById(Long roleId) {
        return ufsRoleRepository.findByRoleIdAndIntrash(roleId, AppConstants.NO);
    }

    @Override
    public UfsRole saveRole(UfsRole ufsRole) {
        return ufsRoleRepository.save(ufsRole);
    }
}
