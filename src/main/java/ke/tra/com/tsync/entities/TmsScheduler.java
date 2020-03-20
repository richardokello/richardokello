/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "TMS_SCHEDULER")
@NamedQueries({
    @NamedQuery(name = "TmsScheduler.findAll", query = "SELECT t FROM TmsScheduler t")})
public class TmsScheduler implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "SCHEDULE_ID")
    private BigDecimal scheduleId;
    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Column(name = "MODEL_ID")
    private BigInteger modelId;
    @Column(name = "PRODUCT_ID")
    private BigInteger productId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NO_FILES")
    private Long noFiles;
    @Column(name = "APP_ID")
    private BigInteger appId;
    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;
    @Column(name = "SCHEDULED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;
    @Column(name = "DOWNLOAD_TYPE")
    private String downloadType;
    @Column(name = "DIR_PATH")
    private String dirPath;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public TmsScheduler() {
    }

    public TmsScheduler(BigDecimal scheduleId) {
        this.scheduleId = scheduleId;
    }

    public BigDecimal getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigDecimal scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public BigInteger getModelId() {
        return modelId;
    }

    public void setModelId(BigInteger modelId) {
        this.modelId = modelId;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public void setProductId(BigInteger productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNoFiles() {
        return noFiles;
    }

    public void setNoFiles(Long noFiles) {
        this.noFiles = noFiles;
    }

    public BigInteger getAppId() {
        return appId;
    }

    public void setAppId(BigInteger appId) {
        this.appId = appId;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsScheduler)) {
            return false;
        }
        TmsScheduler other = (TmsScheduler) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsScheduler[ scheduleId=" + scheduleId + " ]";
    }
    
}
