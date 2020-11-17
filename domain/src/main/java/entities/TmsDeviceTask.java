/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import entities.TmsDevice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_DEVICE_TASK")
@NamedQueries({
    @NamedQuery(name = "TmsDeviceTask.findAll", query = "SELECT t FROM TmsDeviceTask t")})
public class TmsDeviceTask implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "TASK_ID")
    private BigDecimal taskId;
    @Column(name = "SCHEDULE_ID")
    private BigInteger scheduleId;
    @Column(name = "DOWNLOAD_STATUS")
    private String downloadStatus;
    @Column(name = "START_DOWNLOAD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDownloadTime;
    @Column(name = "END_DOWNLOAD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDownloadTime;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;

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

    public BigInteger getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigInteger scheduleId) {
        this.scheduleId = scheduleId;
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
        return "com.mycompany.oracleufs.TmsDeviceTask[ taskId=" + taskId + " ]";
    }
    
}
