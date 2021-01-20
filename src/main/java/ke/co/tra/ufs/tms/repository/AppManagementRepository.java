/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsApp;
import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface AppManagementRepository extends CrudRepository<TmsApp, BigDecimal> {

    /**
     * @param appName
     * @return
     */
    TmsApp findTmsAppByAppNameAndIntrash(String appName,String intrash);

    /**
     *
     * @param modelId
     * @param intrash
     * @param pg
     * @return
     */
    public Page<TmsApp> findBymodelIdAndIntrash(UfsDeviceModel modelId, String intrash, Pageable pg);
    /**
     * 
     * @param actionStatus
     * @param from
     * @param to
     * @param productId
     * @param modelId
     * @param needle
     * @param intrash should be lower case
     * @param pg
     * @return 
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND u.releaseDate BETWEEN ?2 AND ?3 AND STR(COALESCE(u.productId, -1)) LIKE ?4% AND STR(COALESCE(u.modelId, -1)) LIKE ?5% AND "
            + "(lower(u.appName) LIKE %?6% OR lower(u.appVersion) LIKE %?6% OR "
            + "lower(u.description) LIKE %?6% OR lower(u.notesFilepath) LIKE %?6%) AND lower(u.intrash) = lower(?7)")
    public Page<TmsApp> findAll(String actionStatus, Date from, Date to, String productId, String modelId, String needle, String intrash, Pageable pg);


    /**
     * Find By Id
     * @param appId
     * @return
     */
    TmsApp findByAppId(BigDecimal appId);
}
