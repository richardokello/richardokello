/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ke.tra.ufs.webportal.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface SchedulerService {

    /**
     *
     * @param tmsScheduler
     * @return
     */
    public TmsScheduler saveSchedule(TmsScheduler tmsScheduler);

    /**
     *
     * @param deviceTask
     * @return
     */
    public TmsDeviceTask saveDeviceTask(TmsDeviceTask deviceTask);

    /**
     *
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<TmsScheduler> getSchedules(String actionStatus, String needle, Pageable pg);

    /**
     *
     * @param pg
     * @return
     */
    public Page<TmsDeviceTask> findPendingManual(Pageable pg);

    /**
     *
     * @param scheduleFile
     * @return
     */
    public TmsScheduleFile saveScheduleFile(TmsScheduleFile scheduleFile);

    /**
     *
     * @param scheduleFile
     * @return
     */
    public void deleteScheduleFile(TmsScheduleFile scheduleFile);

    /**
     *
     * @param scheduleEstate
     * @return
     */
    public TmsScheduleEstate saveScheduleEstate(TmsScheduleEstate scheduleEstate);

    /**
     *
     * @param scheduleEstate
     * @return
     */
    public void deleteScheduleEstate(TmsScheduleEstate scheduleEstate);

    /**
     *
     * @param deviceTask
     */
    public void deleteScheduledTasks(TmsDeviceTask deviceTask);

    /**
     *
     * @param scheduleId
     * @return
     */
    public List<TmsDeviceTask> findScheduledTasks(TmsScheduler scheduleId);

    /**
     *
     * @param id
     * @return
     */
    public Optional<TmsScheduler> getSchedule(BigDecimal id);

    /**
     *
     * @param scheduleId
     * @return
     */
    public List<TmsScheduleEstate> findByscheduleId(TmsScheduler scheduleId);

    /**
     *
     * @param scheduleId
     * @return
     */
    public List<TmsScheduleFile> findScheduleFilesByScheduleId(TmsScheduler scheduleId);

    /**
     *
     * @param scheduleId
     * @param unitItemId
     * @return
     */
    public List<TmsScheduleEstate> findByscheduleIdAndUnitItemId(TmsScheduler scheduleId, TmsEstateItem unitItemId);

    /**
     *
     * @param actionStatus
     * @param status
     * @param action
     * @param downloadType
     * @param scheduleType
     * @param pg
     * @return
     */
    Page<TmsScheduler> findAll(String actionStatus, String status, String action, String downloadType, String scheduleType,String needle, Pageable pg);

    /**
     *
     * @param actionStatus
     * @param status
     * @param action
     * @param downloadType
     * @param scheduleType
     * @param pg
     * @return
     */
    Page<TmsScheduler> findAllUsingDate(String actionStatus, String status, String action, String downloadType, String scheduleType,Date from, Date to,String needle, Pageable pg);

    /**
     *
     * @param downloadStatus
     * @param from
     * @param to
     * @param deviceId
     * @param scheduleId
     * @param pg
     * @return
     */
    Page<TmsDeviceTask> findDeviceTasks(String downloadStatus, Date from, Date to, String deviceId, String scheduleId,String needle, Pageable pg);

    /**
     *
     * @param pg
     * @return
     */
    Page<TmsScheduler> findPendingSchedule(Pageable pg);

    /**
     *
     * @return
     */
    Integer findPendingSchedule();

    /**
     *
     * @return
     */
    Integer pendingSchedules();
    
    /**
     *
     * @param dateTime
     * @return
     */
    Integer findPendingTasksByDay(LocalDateTime dateTime);
    
    /**
     *
     * @param dateTime
     * @return
     */
    Integer findAllTaskByDay(LocalDateTime dateTime);
    
    /**
     *
     * @param dateTime
     * @return
     */
    Integer findDownloadedTaskByDay(LocalDateTime dateTime);

    /**
     *
     * @param schedular
     */
    public void createTaskItems(TmsScheduler schedular);

    /**
     *
     * @param schedular
     */
    public void updateTaskItems(TmsScheduler schedular);

    /**
     *
     * @param schedular
     */
    public void cancelTaskItems(TmsScheduler schedular);

    /**
     *
     * @param id
     * @return
     */
    public Optional<TmsDeviceTask> findDeviceTask(BigDecimal id);
}
