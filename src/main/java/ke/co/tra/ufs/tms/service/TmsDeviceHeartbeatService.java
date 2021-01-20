/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsDeviceHeartbeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface TmsDeviceHeartbeatService {

    /**
     *
     * @param logId
     * @return
     */
    Optional<TmsDeviceHeartbeat> findById(BigDecimal logId);
    
    /**
     *
     * @param pg
     * @return
     */
    Page<TmsDeviceHeartbeat> findAll(Pageable pg);

    /**
     *
     * @param serialNo
     * @param pg
     * @return
     */
    Page<TmsDeviceHeartbeat> findBySerialNo(String serialNo, Pageable pg);
    
    /**
     *
     * @return
     */
    Integer findTodaysHeartbeats();
    
    /**
     *
     * @param dateTime
     * @return
     */
    Integer findAdaysHeartbeats(LocalDateTime dateTime);

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
    Page<TmsDeviceHeartbeat> findAll(String applicationVersion, String chargingStatus, String osVersion, String serialNo,Date from, Date to,String needle, Pageable pg);
    
}
