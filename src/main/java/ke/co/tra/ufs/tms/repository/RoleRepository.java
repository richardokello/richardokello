/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface RoleRepository extends CrudRepository<UfsUserRole, BigDecimal> {
    /**
     * Used to fetch role with specified id and intrash
     * @param id
     * @param intrash
     * @return 
     */
    UfsUserRole findByroleIdAndIntrash(BigDecimal id, String intrash);
    /**
     * Used to fetch role using role name
     * @param name
     * @param intrash
     * @return 
     */
    UfsUserRole findByroleNameIgnoreCaseAndIntrash(String name, String intrash);
    /**
     * Used to fetch role with name excluding role with the give id
     * @param name
     * @param intrash
     * @param id
     * @return 
     */
    UfsUserRole findByroleNameIgnoreCaseAndIntrashAndRoleIdNot(String name, String intrash, BigDecimal id);
    /**
     * Used to find all roles using Pagination
     * @param intrash
     * @param pg
     * @return 
     */
    Page<UfsUserRole> findByintrashIgnoreCase(String intrash, Pageable pg);
    
//    @Query("")
//    List<UfsUserRole> findDefaultRoles();
    /**
     * Used to search roles using the supplied needle
     * @param needle should be in lowercase
     * @param intrash
     * @param pg
     * @return 
     */
    @Query("SELECT r FROM UfsUserRole r WHERE (lower(r.roleName) like %?1% "
            + "OR lower(r.description) like %?1% OR r.action like %?1% OR r.actionStatus like %?1%) AND r.intrash = ?2")
    Page<UfsUserRole> searchRoles(String needle, String intrash, Pageable pg);
    /**
     * Fetch by action status and intrash
     * @param actionStatus
     * @param intrash
     * @param pg
     * @return 
     */
    Page<UfsUserRole> findByactionStatusIgnoreCaseAndIntrashIgnoreCase(String actionStatus, String intrash, Pageable pg);
    /**
     * Filter user roles
     * @param actionStatus
     * @param from
     * @param to
     * @param needle
     * @param intrash
     * @param pg
     * @return 
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND u.creationDate "
            + "BETWEEN ?2 AND ?3 AND (lower(u.roleName) LIKE %?4% OR lower(u.description) LIKE %?4%) AND lower(u.intrash) = lower(?5)")
    Page<UfsUserRole> findAll(String actionStatus, Date from, Date to, String needle, String intrash, Pageable pg);
}
