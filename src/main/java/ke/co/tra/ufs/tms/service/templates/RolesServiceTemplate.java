/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsEntity;
import ke.co.tra.ufs.tms.entities.UfsEntityPermission;
import ke.co.tra.ufs.tms.entities.UfsRolePermissionMap;
import ke.co.tra.ufs.tms.entities.UfsUserRole;
import ke.co.tra.ufs.tms.repository.EntityPermissionRepository;
import ke.co.tra.ufs.tms.repository.EntityRepository;
import ke.co.tra.ufs.tms.repository.RolePermissionsRepository;
import ke.co.tra.ufs.tms.repository.RoleRepository;
import ke.co.tra.ufs.tms.service.RolesService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class RolesServiceTemplate implements RolesService {

    private final EntityRepository entityRepo;
    private final EntityPermissionRepository permissionRepo;
    private final RoleRepository roleRepo;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RolePermissionsRepository rolePermissionsRepo;

    public RolesServiceTemplate(EntityRepository entityRepo, EntityPermissionRepository permissionRepo,
            RoleRepository roleRepo, RolePermissionsRepository rolePermissionsRepo) {
        this.entityRepo = entityRepo;
        this.permissionRepo = permissionRepo;
        this.roleRepo = roleRepo;
        this.rolePermissionsRepo = rolePermissionsRepo;
    }

    @Override
    public Page<UfsEntity> fetchEntities(Pageable pg) {
        return entityRepo.findAll(pg);
    }

    @Override
    public Page<UfsEntityPermission> fetchEntityPermission(UfsEntity entity, Pageable pg) {
        return permissionRepo.findByentityId(entity, pg);
    }

    @Override
    public UfsUserRole fetchRole(String roleName) {
        return roleRepo.findByroleNameIgnoreCaseAndIntrash(roleName, AppConstants.NO);
    }

    @Override
    public UfsUserRole fetchRoleButNot(String roleName, BigDecimal roleId) {
        return roleRepo.findByroleNameIgnoreCaseAndIntrashAndRoleIdNot(roleName, AppConstants.NO, roleId);
    }

    @Override
    public UfsUserRole fetchRole(BigDecimal roleId) {
        return roleRepo.findByroleIdAndIntrash(roleId, AppConstants.NO);
    }

    @Override
    public UfsUserRole saveRole(UfsUserRole userRole) {
        log.info(AppConstants.AUDIT_LOG, "Persisting user role in the database: " + userRole);
        userRole = roleRepo.save(userRole);
        log.info(AppConstants.AUDIT_LOG, "Done persisting user role in the database: " + userRole);
        return userRole;
    }

    @Override
    public Page<UfsUserRole> fetchRoles(String actionStatus, Date from, Date to, String needle, Pageable pg) {
        return roleRepo.findAll(actionStatus, from, to, needle.toLowerCase(), AppConstants.NO, pg);
    }

    @Override
    public Page<UfsUserRole> fetchRoles(Pageable pg) {
        return roleRepo.findByintrashIgnoreCase(AppConstants.NO, pg);
    }

    @Override
    public Page<UfsUserRole> fetchRoles(String actionStatus, Pageable pg) {
        return roleRepo.findByactionStatusIgnoreCaseAndIntrashIgnoreCase(actionStatus, AppConstants.NO, pg);
    }

    @Override
    public void trashRole(BigDecimal roleId) throws NotFoundException {
        UfsUserRole role = this.fetchRole(roleId);
        if (role == null) {
            throw new NotFoundException("Sorry Role Doesn't exist");
        }
        role.setAction(AppConstants.ACTIVITY_DELETE);
        role.setActionStatus(AppConstants.STATUS_APPROVED);
        role.setIntrash(AppConstants.YES);
    }

    @Override
    public List<UfsUserRole> fetchDefaultRoles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<UfsUserRole> searchRoles(String needle, Pageable pg) {
        return roleRepo.searchRoles(needle.toLowerCase(), AppConstants.NO, pg);
    }

    @Override
    public void deletePermissionMapPermanently(BigDecimal id) {
        rolePermissionsRepo.deleteById(id);
    }

    @Override
    public void deletePermissionMapPermanently(UfsRolePermissionMap rolePerm) {
        rolePermissionsRepo.delete(rolePerm);
    }

    @Override
    public Iterable<UfsRolePermissionMap> saveRolePermissions(List<UfsRolePermissionMap> permissions) {
        return rolePermissionsRepo.saveAll(permissions);
    }

}
