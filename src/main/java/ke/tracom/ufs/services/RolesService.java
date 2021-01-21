package ke.tracom.ufs.services;


import ke.tracom.ufs.entities.UfsRole;
import ke.tracom.ufs.entities.UfsRolePermission;

import java.util.List;

public interface RolesService {
    List<UfsRolePermission> findByRole(UfsRole role);

    void deleteRolePermissions(List<UfsRolePermission> mlkRolePermissions);

    public UfsRole findRoleById(Long roleId);

    public UfsRole saveRole(UfsRole ufsRole);
}
