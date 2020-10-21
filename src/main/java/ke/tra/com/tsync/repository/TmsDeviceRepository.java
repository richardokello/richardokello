/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ke.tra.com.tsync.entities.TmsDevice;
import ke.tra.com.tsync.entities.TmsEstateItem;
import ke.tra.com.tsync.entities.UfsDeviceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * @author Owori Juma
 */
@Repository
public interface TmsDeviceRepository extends JpaRepository<TmsDevice, BigDecimal> {

    /**
     * @param serialNo
     * @param intrash
     * @return
     */
    public TmsDevice findBySerialNoAndIntrash(String serialNo, String intrash);

    /**
     * @param action
     * @param actionStatus
     * @param needle
     * @param from
     * @param to
     * @param intrash
     * @param status
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.action LIKE ?1% AND u.actionStatus LIKE ?2% "
            + "AND u.serialNo LIKE %?3%  AND u.creationDate BETWEEN ?4 and ?5 AND lower(u.intrash) = lower(?6) AND u.status LIKE ?7%")
    Page<TmsDevice> findAll(String action, String actionStatus, String needle, Date from, Date to, String intrash, String status, Pageable pg);

    /**
     * @param estateId
     * @return
     */
    public List<TmsDevice> findByestateId(TmsEstateItem estateId);

    /**
     * @param status
     * @param intrash
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.status LIKE ?1% AND lower(u.intrash) = lower(?2)")
    List<TmsDevice> findAllActive(String status, String intrash);

    /**
     * @param status
     * @param intrash
     * @return
     */
    @Query("SELECT COUNT(*) FROM TmsDevice u WHERE u.status LIKE ?1% AND lower(u.intrash) = lower(?2) AND u.action!='Release'")
    Integer findAllActiveDevices(String status, String intrash);

    /**
     * @param intrash
     * @return
     */
    @Query("SELECT COUNT(*) FROM TmsDevice u WHERE lower(u.intrash) = lower(?1)")
    Integer findActiveDevices(String intrash);

    /**
     * @param modelId
     * @param unitItemId
     * @param intrash
     * @return
     */
    List<TmsDevice> findByModelIdAndEstateIdAndIntrash(UfsDeviceModel modelId, TmsEstateItem unitItemId, String intrash);

    /**
     *
     * @param id
     * @return
     */
    List<TmsDevice> findAllByOutletIds(BigDecimal id);

    /**
     * @param deviceId
     * @param intrash
     * @return
     */
    public TmsDevice findByDeviceIdAndIntrash(BigDecimal deviceId,String intrash);

}
