package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.repository.*;
import ke.tra.ufs.webportal.service.SchedulerService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
@CommonsLog
public class SchedulerServiceTemplate implements SchedulerService {

    private final DeviceTaskRepository deviceTaskRepository;
    private final SchedulerRepository schedulerRepository;
    private final ScheduleEstateRepository scheduleEstateRepository;
    private final ScheduleFileRepository scheduleFileRepository;
    private final TmsDeviceRepository deviceRepository;

    public SchedulerServiceTemplate(DeviceTaskRepository deviceTaskRepository, SchedulerRepository schedulerRepository, ScheduleEstateRepository scheduleEstateRepository, ScheduleFileRepository scheduleFileRepository, TmsDeviceRepository deviceRepository) {
        this.deviceTaskRepository = deviceTaskRepository;
        this.schedulerRepository = schedulerRepository;
        this.scheduleEstateRepository = scheduleEstateRepository;
        this.scheduleFileRepository = scheduleFileRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public TmsScheduler saveSchedule(TmsScheduler tmsScheduler) {
        return schedulerRepository.save(tmsScheduler);
    }

    @Override
    public TmsDeviceTask saveDeviceTask(TmsDeviceTask deviceTask) {
        return deviceTaskRepository.save(deviceTask);
    }

    @Override
    public Page<TmsScheduler> getSchedules(String actionStatus, String needle, Pageable pg) {
        return schedulerRepository.findAll(actionStatus, needle, "Auto", AppConstants.NO, pg);
    }

    @Override
    public TmsScheduleFile saveScheduleFile(TmsScheduleFile scheduleFile) {
        return scheduleFileRepository.save(scheduleFile);
    }

    @Override
    public TmsScheduleEstate saveScheduleEstate(TmsScheduleEstate scheduleEstate) {
        return scheduleEstateRepository.save(scheduleEstate);
    }

    @Override
    public Optional<TmsScheduler> getSchedule(BigDecimal id) {
        return schedulerRepository.findById(id);
    }

    @Override
    public List<TmsScheduleEstate> findByscheduleId(TmsScheduler scheduleId) {
        return scheduleEstateRepository.findByscheduleId(scheduleId);
    }

    @Override
    public List<TmsScheduleEstate> findByscheduleIdAndUnitItemId(TmsScheduler scheduleId, TmsEstateItem unitItemId) {
        return scheduleEstateRepository.findByscheduleIdAndUnitItemId(scheduleId, unitItemId);
    }

    @Override
    public void deleteScheduleFile(TmsScheduleFile scheduleFile) {
        scheduleFileRepository.delete(scheduleFile);
    }

    @Override
    public void deleteScheduleEstate(TmsScheduleEstate scheduleEstate) {
        scheduleEstateRepository.delete(scheduleEstate);
    }

    @Override
    public List<TmsScheduleFile> findScheduleFilesByScheduleId(TmsScheduler scheduleId) {
        return scheduleFileRepository.findByscheduleId(scheduleId);
    }

    @Override
    public void deleteScheduledTasks(TmsDeviceTask deviceTask) {
        deviceTaskRepository.delete(deviceTask);
    }

    @Override
    public List<TmsDeviceTask> findScheduledTasks(TmsScheduler scheduleId) {
        return deviceTaskRepository.findByscheduleId(scheduleId);
    }

    @Override
    public Page<TmsDeviceTask> findPendingManual(Pageable pg) {
        return deviceTaskRepository.findPendingManual(pg);
    }

    @Override
    public Page<TmsScheduler> findAll(String actionStatus, String status, String action, String downloadType, String scheduleType,String needle, Pageable pg) {
        return schedulerRepository.findAll(actionStatus, status, action, downloadType, scheduleType, needle, AppConstants.NO, pg);
    }

    @Override
    public Page<TmsScheduler> findAllUsingDate(String actionStatus, String status, String action, String downloadType, String scheduleType,Date from, Date to,String needle, Pageable pg) {
        return schedulerRepository.findAllByDateTime(actionStatus, status, action, downloadType, scheduleType, AppConstants.NO,from,to,needle, pg);
    }

    @Override
    public Page<TmsDeviceTask> findDeviceTasks(String downloadStatus, Date from, Date to, String deviceId, String scheduleId,String needle, Pageable pg) {
        return deviceTaskRepository.findAll(downloadStatus, from, to, deviceId, scheduleId, AppConstants.NO,needle, pg);
    }

    @Override
    public Page<TmsScheduler> findPendingSchedule(Pageable pg) {
        return schedulerRepository.findPendingSchedule(AppConstants.NO, pg);
    }

    @Override
    public Integer findPendingSchedule() {
        return deviceTaskRepository.findPendingSchedule();
    }

    @Override
    public Integer pendingSchedules() {
        return schedulerRepository.pendingSchedules();
    }

    @Override
    @Async
    public void createTaskItems(TmsScheduler schedular) {
        List<TmsScheduleEstate> estates = scheduleEstateRepository.findByscheduleId(schedular);
        estates.forEach((est) -> {
            List<TmsDevice> devices = deviceRepository.findByModelIdAndEstateIdAndIntrash(schedular.getModelId(), est.getUnitItemId(), AppConstants.NO);
            devices.forEach((dev) -> {
                TmsDeviceTask deviceTask = new TmsDeviceTask();
                deviceTask.setDeviceId(dev);
                deviceTask.setScheduleId(schedular);
                deviceTask.setDownloadStatus("PENDING");
                deviceTask.setIntrash(AppConstants.NO);

                //persist the device task
                deviceTaskRepository.save(deviceTask);
            });
        });
    }

    @Override
    @Async
    public void updateTaskItems(TmsScheduler schedular) {
        List<TmsDeviceTask> deviceTasks = findScheduledTasks(schedular);
        deviceTasks.forEach((tasks) -> {
            deleteScheduledTasks(tasks);
        });

        List<TmsScheduleEstate> estates = findByscheduleId(schedular);
        estates.forEach((est) -> {
            List<TmsDevice> devices = deviceRepository.findByestateId(est.getUnitItemId());
            devices.forEach((dev) -> {
                TmsDeviceTask deviceTask = new TmsDeviceTask();
                deviceTask.setDeviceId(dev);
                deviceTask.setScheduleId(schedular);
                deviceTask.setDownloadStatus("PENDING");
                deviceTask.setIntrash(AppConstants.NO);

                //persist the device task
                deviceTaskRepository.save(deviceTask);
            });
        });
    }

    @Override
    @Async
    public void cancelTaskItems(TmsScheduler schedular) {
        deviceTaskRepository.findByscheduleId(schedular).forEach(task -> {
            task.setDownloadStatus("CANCELLED");
            deviceTaskRepository.save(task);
        });
    }

    @Override
    public Optional<TmsDeviceTask> findDeviceTask(BigDecimal id) {
        return deviceTaskRepository.findById(id);
    }

    @Override
    public Integer findPendingTasksByDay(LocalDateTime dateTime) {
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        Date datefrom = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        Date dateto = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("From :" + datefrom + " to Date : " + dateto);

        return deviceTaskRepository.findPendingTasksByDay(datefrom, dateto);
    }

    @Override
    public Integer findAllTaskByDay(LocalDateTime dateTime) {
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        Date datefrom = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        Date dateto = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("From :" + datefrom + " to Date : " + dateto);
        return deviceTaskRepository.findAllTaskByDay(datefrom, dateto);
    }

    @Override
    public Integer findDownloadedTaskByDay(LocalDateTime dateTime) {
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        Date datefrom = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        Date dateto = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("From :" + datefrom + " to Date : " + dateto);
        return deviceTaskRepository.findDownloadedTasksByDay(datefrom, dateto);
    }
}
