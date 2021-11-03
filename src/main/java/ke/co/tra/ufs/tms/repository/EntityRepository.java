/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.UfsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface EntityRepository extends CrudRepository<UfsEntity, BigDecimal>{
    /**
     * Used to fetch all entities using the specified Pagination
     * @param pg
     * @return 
     */
    Page<UfsEntity> findAll(Pageable pg);
}
