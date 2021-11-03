/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsDeviceHeartbeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface TmsDeviceHeartbeatRepository extends CrudRepository<TmsDeviceHeartbeat, BigDecimal> {

    /**
     *
     * @param pg
     * @return
     */
    Page<TmsDeviceHeartbeat> findAll(Pageable pg);

    /**
     *
     * @param serialNo
     * @param serialNoCon
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.serialNo LIKE ?1% OR u.serialNo LIKE ?2%")
    Page<TmsDeviceHeartbeat> findBySerialNo(String serialNo, String serialNoCon, Pageable pg);

    /**
     *
     * @param today
     * @return
     */
    @Query("SELECT COUNT(*) FROM #{#entityName} u WHERE u.creationDate > ?1")
    Integer findTodaysHeartBeats(Date today);
    
    
    @Query("SELECT COUNT(*) FROM #{#entityName} u WHERE u.creationDate BETWEEN ?1 and ?2")
    Integer findAdaysHeartBeats(Date from, Date to);

    /**
     *
     * @param applicationVersion
     * @param chargingStatus
     * @param osVersion
     * @param serialNo
     * @param needle
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.applicationVersion LIKE ?1% AND "
            + "u.chargingStatus LIKE ?2% AND u.osVersion LIKE ?3% AND u.serialNo LIKE ?4% AND u.creationDate BETWEEN ?5 AND ?6 "
            + "AND (u.applicationVersion LIKE %?7% OR u.chargingStatus LIKE %?7% OR u.osVersion LIKE %?7% OR u.serialNo LIKE %?7% )")
    Page<TmsDeviceHeartbeat> findAll(String applicationVersion, String chargingStatus, String osVersion, String serialNo,Date from, Date to,String needle, Pageable pg);
}
