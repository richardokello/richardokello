/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsRole;
import ke.tracom.ufs.entities.UfsRolePermission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author eli.muraya
 */
public interface RolePermissionRepository extends CrudRepository<UfsRolePermission, Long> {

    List<UfsRolePermission> findByRole(UfsRole role);

    List<UfsRolePermission> findAllByroleId(Long roleId);

    List<UfsRolePermission> findAllByroleIdAndPermissionIdIn(Long roleId, List<Long> permissionIds);

}
