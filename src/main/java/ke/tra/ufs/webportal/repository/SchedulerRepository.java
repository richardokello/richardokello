/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsScheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
public interface SchedulerRepository extends CrudRepository<TmsScheduler, BigDecimal> {

    /**
     *
     * @param actionStatus
     * @param needle
     * @param scheduleType
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% "
            + "AND u.downloadType LIKE %?2% AND u.scheduleType LIKE %?3% AND lower(u.intrash) = lower(?4)")
    Page<TmsScheduler> findAll(String actionStatus, String needle, String scheduleType, String intrash, Pageable pg);

    /**
     *
     * @param actionStatus
     * @param status
     * @param action
     * @param downloadType
     * @param scheduleType
     * @param needle
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND u.status LIKE ?2% AND u.action LIKE ?3% "
            + "AND u.downloadType LIKE %?4% AND u.scheduleType LIKE %?5% AND (u.downloadType LIKE %?6% OR u.scheduleType LIKE %?6% OR u.appId LIKE %?6%) AND lower(u.intrash) = lower(?7)")
    Page<TmsScheduler> findAll(String actionStatus, String status, String action, String downloadType, String scheduleType,String needle, String intrash, Pageable pg);

    /**
     *
     * @param actionStatus
     * @param status
     * @param action
     * @param downloadType
     * @param scheduleType
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1% AND u.status LIKE ?2% AND u.action LIKE ?3% "
            + "AND u.downloadType LIKE %?4% AND u.scheduleType LIKE %?5% AND lower(u.intrash) = lower(?6) AND u.dateTime BETWEEN ?7 AND ?8  AND " +
            " (u.downloadType LIKE %?9% OR u.scheduleType LIKE %?9% OR u.appId LIKE %?9%)")
    Page<TmsScheduler> findAllByDateTime(String actionStatus, String status, String action, String downloadType, String scheduleType, String intrash,Date from, Date to,String needle, Pageable pg);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM TmsScheduler u, TmsDeviceTask t WHERE u.scheduleId = t.scheduleId AND t.downloadStatus='PENDING' AND u.scheduleType='Auto' AND lower(u.intrash) = lower(?1)")
    Page<TmsScheduler> findPendingSchedule(String intrash, Pageable pg);

    @Query("SELECT count(DISTINCT u.scheduleId) AS pendingSchedules FROM TmsScheduler u INNER JOIN TmsDeviceTask t ON u.scheduleId = t.scheduleId AND t.downloadStatus='PENDING' AND u.scheduleType='Auto' AND u.intrash='NO'")
    Integer pendingSchedules();


}
