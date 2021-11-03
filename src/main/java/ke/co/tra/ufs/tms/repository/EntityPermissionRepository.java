/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.UfsEntity;
import ke.co.tra.ufs.tms.entities.UfsEntityPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author Cornelius M
 */
public interface EntityPermissionRepository extends CrudRepository<UfsEntityPermission, BigDecimal> {
    /**
     * Used to fetch entity permissions
     * @param entity 
     * @param pg
     * @return 
     */
    Page<UfsEntityPermission> findByentityId(UfsEntity entity, Pageable pg);
}
