/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface BusinessUnitItemRepository extends CrudRepository<TmsEstateItem, BigDecimal> {

    /**
     *
     * @param unitId
     * @param name
     * @param intrash
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.unitId= ?1 AND  u.name=?2 AND "
            + " lower(u.intrash) = lower(?3)")
    TmsEstateItem findByLevelAndName(TmsEstateHierarchy unitId, String name, String intrash);

    /**
     *
     * @param unitId
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.unitId= ?1 AND "
            + " lower(u.intrash) = lower(?2)")
    Page<TmsEstateItem> findByunitIdAndintrash(TmsEstateHierarchy unitId, String intrash, Pageable pg);
    
    /**
     *
     * @param unitId
     * @param intrash
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.unitId= ?1 AND "
            + " lower(u.intrash) = lower(?2)")
    List<TmsEstateItem> findbyunitId(TmsEstateHierarchy unitId, String intrash);
    
    /**
     *
     * @param parentId
     * @param intrash
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.parentId= ?1 AND "
            + " lower(u.intrash) = lower(?2)")
    List<TmsEstateItem> findbyparentId(TmsEstateItem parentId, String intrash);
    
    /**
     *
     * @param intrash
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.parentId IS NULL AND "
            + " lower(u.intrash) = lower(?1)")
    List<TmsEstateItem> findbyAllRootParents(String intrash);
}
