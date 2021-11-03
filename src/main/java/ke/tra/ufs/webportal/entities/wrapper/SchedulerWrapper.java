package ke.tra.ufs.webportal.entities.wrapper;


import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsScheduler;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
public class SchedulerWrapper {

    private BigDecimal taskId;
    private String downloadStatus;
    private Date startDownloadTime;
    private Date endDownloadTime;
    private String intrash;
    private TmsDevice deviceId;
    private TmsScheduler scheduleId;
    BigDecimal menuProfileId;
    BigDecimal binProfileId;

    public SchedulerWrapper(BigDecimal taskId) {
        this.taskId = taskId;
    }

    public SchedulerWrapper(BigDecimal taskId, String downloadStatus, Date startDownloadTime, Date endDownloadTime, String intrash, TmsDevice deviceId, TmsScheduler scheduleId) {
        this.taskId = taskId;
        this.downloadStatus = downloadStatus;
        this.startDownloadTime = startDownloadTime;
        this.endDownloadTime = endDownloadTime;
        this.intrash = intrash;
        this.deviceId = deviceId;
        this.scheduleId = scheduleId;
    }

    public BigDecimal getTaskId() {
        return taskId;
    }

    public void setTaskId(BigDecimal taskId) {
        this.taskId = taskId;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public Date getStartDownloadTime() {
        return startDownloadTime;
    }

    public void setStartDownloadTime(Date startDownloadTime) {
        this.startDownloadTime = startDownloadTime;
    }

    public Date getEndDownloadTime() {
        return endDownloadTime;
    }

    public void setEndDownloadTime(Date endDownloadTime) {
        this.endDownloadTime = endDownloadTime;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public TmsScheduler getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(TmsScheduler scheduleId) {
        this.scheduleId = scheduleId;
    }

    public BigDecimal getMenuProfileId() {
        return menuProfileId;
    }

    public void setMenuProfileId(BigDecimal menuProfileId) {
        this.menuProfileId = menuProfileId;
    }

    public BigDecimal getBinProfileId() {
        return binProfileId;
    }

    public void setBinProfileId(BigDecimal binProfileId) {
        this.binProfileId = binProfileId;
    }
}
