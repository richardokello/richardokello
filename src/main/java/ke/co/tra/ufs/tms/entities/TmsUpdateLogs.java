/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_UPDATE_LOGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsUpdateLogs.findAll", query = "SELECT t FROM TmsUpdateLogs t")
    , @NamedQuery(name = "TmsUpdateLogs.findByLogId", query = "SELECT t FROM TmsUpdateLogs t WHERE t.logId = :logId")
    , @NamedQuery(name = "TmsUpdateLogs.findByTaskId", query = "SELECT t FROM TmsUpdateLogs t WHERE t.taskId = :taskId")
    , @NamedQuery(name = "TmsUpdateLogs.findByDateTimeAdded", query = "SELECT t FROM TmsUpdateLogs t WHERE t.dateTimeAdded = :dateTimeAdded")
    , @NamedQuery(name = "TmsUpdateLogs.findByCompletedFiles", query = "SELECT t FROM TmsUpdateLogs t WHERE t.completedFiles = :completedFiles")
    , @NamedQuery(name = "TmsUpdateLogs.findByStatus", query = "SELECT t FROM TmsUpdateLogs t WHERE t.status = :status")
    , @NamedQuery(name = "TmsUpdateLogs.findByTimeTaken", query = "SELECT t FROM TmsUpdateLogs t WHERE t.timeTaken = :timeTaken")
    , @NamedQuery(name = "TmsUpdateLogs.findBySourceIp", query = "SELECT t FROM TmsUpdateLogs t WHERE t.sourceIp = :sourceIp")})
public class TmsUpdateLogs implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "LOG_ID", nullable = false, precision = 38, scale = 0)
    private BigDecimal logId;
    @Column(name = "TASK_ID")
    private BigInteger taskId;
    @Column(name = "DATE_TIME_ADDED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeAdded;
    @Column(name = "COMPLETED_FILES")
    private BigInteger completedFiles;
    @Column(name = "STATUS", length = 15)
    private String status;
    @Column(name = "TIME_TAKEN", length = 20)
    private String timeTaken;
    @Column(name = "SOURCE_IP", length = 20)
    private String sourceIp;
    @JoinColumn(name = "TMS_DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne
    @JsonIgnore
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
        return "entities.TmsUpdateLogs[ logId=" + logId + " ]";
    }
    
}
