/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;

import ke.co.tra.ufs.tms.entities.UfsDeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Cornelius M
 */
public interface DeviceTypeRepository extends CrudRepository<UfsDeviceType, BigDecimal> {
    /**
     * Filter device type by action status, search needle and intrash
     * @param actionStatus
     * @param needle
     * @param intrash
     * @param pg
     * @return 
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND (u.description LIKE %?2% OR u.type LIKE %?2%) "
            + "AND lower(u.intrash) = lower(?3)")
    Page<UfsDeviceType> findAll(String actionStatus, String needle, String intrash, Pageable pg);

    /**
     * Fetch device type
     * @param deviceType
     * @param intrash
     * @return
     */
    List<UfsDeviceType> findAllByTypeAndIntrash(String deviceType,String intrash);

}
