/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsEntity;
import ke.co.tra.ufs.tms.entities.UfsEntityPermission;
import ke.co.tra.ufs.tms.entities.UfsRolePermissionMap;
import ke.co.tra.ufs.tms.entities.UfsUserRole;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface RolesService {

    /**
     * Used to fetch entities using specified pagination
     *
     * @param pg pagination
     * @return
     */
    public Page<UfsEntity> fetchEntities(Pageable pg);

    /**
     * Used to fetch entity permissions using entity
     *
     * @param entity
     * @param pg
     * @return
     */
    public Page<UfsEntityPermission> fetchEntityPermission(UfsEntity entity, Pageable pg);

    /**
     * Used to fetch role by name
     *
     * @param roleName role name
     * @return
     */
    public UfsUserRole fetchRole(String roleName);

    /**
     * Used to fetch User Role with a given name excluding role with current id
     *
     * @param roleName
     * @param roleId
     * @return
     */
    public UfsUserRole fetchRoleButNot(String roleName, BigDecimal roleId);

    /**
     * Used to fetch role using roleId
     *
     * @param roleId
     * @return
     */
    public UfsUserRole fetchRole(BigDecimal roleId);

    /**
     * Used to save role in the database
     *
     * @param userRole
     * @return
     */
    public UfsUserRole saveRole(UfsUserRole userRole);

    /**
     * Filter user roles
     *
     * @param actionStatus
     * @param from
     * @param to
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsUserRole> fetchRoles(String actionStatus, Date from, Date to, String needle, Pageable pg);

    /**
     * Used to fetch all roles
     *
     * @param pg
     * @return
     */
    public Page<UfsUserRole> fetchRoles(Pageable pg);

    /**
     * Fetch roles by action status
     *
     * @param actionStatus
     * @param pg
     * @return
     */
    public Page<UfsUserRole> fetchRoles(String actionStatus, Pageable pg);

    /**
     * used to move to trash role with specified id
     *
     * @param roleId
     * @throws ke.co.tra.ufs.tms.utils.exceptions.NotFoundException
     */
    public void trashRole(BigDecimal roleId) throws NotFoundException;

    /**
     * Used to fetch default user roles
     *
     * @return
     */
    public List<UfsUserRole> fetchDefaultRoles();

    /**
     * Used to search roles
     *
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsUserRole> searchRoles(String needle, Pageable pg);

    /**
     *
     * @param id
     */
    public void deletePermissionMapPermanently(BigDecimal id);

    /**
     * Delete role permission permanently
     *
     * @param rolePerm
     */
    public void deletePermissionMapPermanently(UfsRolePermissionMap rolePerm);

    /**
     * Save role permissions
     *
     * @param permissions
     * @return
     */
    public Iterable<UfsRolePermissionMap> saveRolePermissions(List<UfsRolePermissionMap> permissions);
}
