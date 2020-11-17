/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
@Table(name = "TMS_UPDATE_LOGS")
@NamedQueries({
    @NamedQuery(name = "TmsUpdateLogs.findAll", query = "SELECT t FROM TmsUpdateLogs t")})
public class TmsUpdateLogs implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Column(name = "TASK_ID")
    private BigInteger taskId;
    @Column(name = "DATE_TIME_ADDED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeAdded;
    @Column(name = "COMPLETED_FILES")
    private BigInteger completedFiles;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "TIME_TAKEN")
    private String timeTaken;
    @Column(name = "SOURCE_IP")
    private String sourceIp;
    @JoinColumn(name = "TMS_DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne
    private TmsDevice tmsDeviceId;

    public TmsUpdateLogs() {
    }

    public TmsUpdateLogs(BigDecimal logId) {
        this.logId = logId;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public BigInteger getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId;
    }

    public Date getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(Date dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    public BigInteger getCompletedFiles() {
        return completedFiles;
    }

    public void setCompletedFiles(BigInteger completedFiles) {
        this.completedFiles = completedFiles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public TmsDevice getTmsDeviceId() {
        return tmsDeviceId;
    }

    public void setTmsDeviceId(TmsDevice tmsDeviceId) {
        this.tmsDeviceId = tmsDeviceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsUpdateLogs)) {
            return false;
        }
        TmsUpdateLogs other = (TmsUpdateLogs) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsUpdateLogs[ logId=" + logId + " ]";
    }
    
}
