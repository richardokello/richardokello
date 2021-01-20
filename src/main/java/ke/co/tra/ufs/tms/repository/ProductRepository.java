/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface ProductRepository extends CrudRepository<UfsProduct, BigDecimal> {

    /**
     *
     * @param productName
     * @param intrash
     * @return
     */
    public UfsProduct findByproductNameIgnoreCaseAndIntrash(String productName, String intrash);

    /**
     *
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.status like ?1% AND u.actionStatus like ?2% "
            + "AND u.creationDate BETWEEN ?3 AND ?4 AND "
            + " lower(u.intrash) = lower(?5)")
    public Page<UfsProduct> findAll(String status, String actionStatus, Date from, Date to, String intrash, Pageable pg);

    /**
     * Used to fetch all users for given intrash specification and pageable
     * object
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<UfsProduct> findByintrash(String intrash, Pageable pg);
}
