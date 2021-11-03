/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceTask;
import ke.co.tra.ufs.tms.entities.TmsScheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Owori Juma
 */
public interface DeviceTaskRepository extends CrudRepository<TmsDeviceTask, BigDecimal> {

    /**
     * @param scheduleId
     * @return
     */
    public List<TmsDeviceTask> findByscheduleId(TmsScheduler scheduleId);

    //new ke.co.tra.ufs.tms.entities.wrappers.SchedulerWrapper(u.scheduleId,u.dateTime,u.status,u.noFiles,u.appId,u.scheduleType,u.scheduledTime,u.downloadType,u.dirPath,u.action,u.actionStatus,u.intrash,p,d)

    /**
     * @param pg
     * @return
     */
    @Query("SELECT new ke.co.tra.ufs.tms.entities.wrappers.SchedulerWrapper(u.taskId,u.downloadStatus,u.startDownloadTime,"
            + "u.endDownloadTime,u.intrash, d,s) "
            + " FROM TmsDeviceTask u LEFT JOIN u.scheduleId s LEFT JOIN u.deviceId d "
            + " WHERE u.downloadStatus!='COMPLETED'"
            + "AND s.scheduleType='Manual'")
    public Page<TmsDeviceTask> findPendingManual(Pageable pg);

    /**
     * @param downloadStatus
     * @param from
     * @param to
     * @param deviceId
     * @param scheduleId
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.downloadStatus LIKE ?1% AND STR(COALESCE(u.deviceId, -1)) LIKE ?4% AND "
            + "STR(COALESCE(u.scheduleId, -1)) LIKE ?5% AND COALESCE(u.startDownloadTime, sysdate) BETWEEN ?2 AND ?3 AND lower(u.intrash) = lower(?6) AND "
            + "(u.downloadStatus LIKE %?7% OR u.deviceId LIKE %?7% OR u.scheduleId LIKE %?7% OR u.deviceId.serialNo LIKE %?7%)")
    public Page<TmsDeviceTask> findAll(String downloadStatus, Date from, Date to, String deviceId, String scheduleId, String intrash,String needle, Pageable pg);

    /**
     * @return
     */
    @Query("SELECT COUNT (DISTINCT t.scheduleId) "
            + "  FROM TmsDeviceTask t WHERE  t.downloadStatus!='COMPLETED'")
    Integer findPendingSchedule();

    /**
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT COUNT(*) FROM #{#entityName} u WHERE u.startDownloadTime BETWEEN ?1 and ?2")
    Integer findAllTaskByDay(Date from, Date to);

    /**
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT COUNT(*) FROM #{#entityName} u WHERE u.downloadStatus!='COMPLETED' AND u.startDownloadTime BETWEEN ?1 and ?2")
    Integer findPendingTasksByDay(Date from, Date to);

    @Query("SELECT COUNT(*) FROM #{#entityName} u WHERE u.downloadStatus='COMPLETED' AND u.startDownloadTime BETWEEN ?1 and ?2")
    Integer findDownloadedTasksByDay(Date from, Date to);


    /**
     * @param deviceId
     * @return
     */
    TmsDeviceTask findTopByDeviceIdOrderByTaskIdDesc(TmsDevice deviceId);


}
