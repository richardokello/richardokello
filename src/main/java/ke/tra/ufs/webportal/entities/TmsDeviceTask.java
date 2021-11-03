/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_TASK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceTask.findAll", query = "SELECT t FROM TmsDeviceTask t")
    , @NamedQuery(name = "TmsDeviceTask.findByTaskId", query = "SELECT t FROM TmsDeviceTask t WHERE t.taskId = :taskId")
    , @NamedQuery(name = "TmsDeviceTask.findByDownloadStatus", query = "SELECT t FROM TmsDeviceTask t WHERE t.downloadStatus = :downloadStatus")
    , @NamedQuery(name = "TmsDeviceTask.findByStartDownloadTime", query = "SELECT t FROM TmsDeviceTask t WHERE t.startDownloadTime = :startDownloadTime")
    , @NamedQuery(name = "TmsDeviceTask.findByEndDownloadTime", query = "SELECT t FROM TmsDeviceTask t WHERE t.endDownloadTime = :endDownloadTime")
    , @NamedQuery(name = "TmsDeviceTask.findByIntrash", query = "SELECT t FROM TmsDeviceTask t WHERE t.intrash = :intrash")})
public class TmsDeviceTask implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_TASK_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_TASK_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_DEVICE_TASK_SEQ")
    @Basic(optional = false)//
    @Column(name = "TASK_ID")
    private BigDecimal taskId;
    @Size(max = 20)
    @Column(name = "DOWNLOAD_STATUS")
    private String downloadStatus;
    @Column(name = "START_DOWNLOAD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDownloadTime;
    @Column(name = "END_DOWNLOAD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDownloadTime;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;
    @JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
    @ManyToOne    
    @JsonIgnore
    private TmsScheduler scheduleId;

    public TmsDeviceTask() {
    }

    public TmsDeviceTask(BigDecimal taskId) {
        this.taskId = taskId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsDeviceTask)) {
            return false;
        }
        TmsDeviceTask other = (TmsDeviceTask) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceTask[ taskId=" + taskId + " ]";
    }

}
