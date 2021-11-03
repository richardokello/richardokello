/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.UfsMno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface MNORepository extends CrudRepository<UfsMno, BigDecimal> {

    /**
     *
     * @param actionStatus
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + " AND  lower(u.intrash) = lower(?2)")
    public Page<UfsMno> findAll(String actionStatus, String intrash, Pageable pg);

    /**
     *
     * @param mnoName
     * @return
     */
    public UfsMno findBymnoName(String mnoName);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<UfsMno> findByintrash(String intrash, Pageable pg);

}
