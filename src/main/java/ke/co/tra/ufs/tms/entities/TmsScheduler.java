/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_SCHEDULER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsScheduler.findAll", query = "SELECT t FROM TmsScheduler t")
    , @NamedQuery(name = "TmsScheduler.findByScheduleId", query = "SELECT t FROM TmsScheduler t WHERE t.scheduleId = :scheduleId")
    , @NamedQuery(name = "TmsScheduler.findByDateTime", query = "SELECT t FROM TmsScheduler t WHERE t.dateTime = :dateTime")
    , @NamedQuery(name = "TmsScheduler.findByStatus", query = "SELECT t FROM TmsScheduler t WHERE t.status = :status")
    , @NamedQuery(name = "TmsScheduler.findByNoFiles", query = "SELECT t FROM TmsScheduler t WHERE t.noFiles = :noFiles")
    , @NamedQuery(name = "TmsScheduler.findByAppId", query = "SELECT t FROM TmsScheduler t WHERE t.appId = :appId")
    , @NamedQuery(name = "TmsScheduler.findByScheduleType", query = "SELECT t FROM TmsScheduler t WHERE t.scheduleType = :scheduleType")
    , @NamedQuery(name = "TmsScheduler.findByScheduledTime", query = "SELECT t FROM TmsScheduler t WHERE t.scheduledTime = :scheduledTime")
    , @NamedQuery(name = "TmsScheduler.findByDownloadType", query = "SELECT t FROM TmsScheduler t WHERE t.downloadType = :downloadType")
    , @NamedQuery(name = "TmsScheduler.findByDirPath", query = "SELECT t FROM TmsScheduler t WHERE t.dirPath = :dirPath")
    , @NamedQuery(name = "TmsScheduler.findByAction", query = "SELECT t FROM TmsScheduler t WHERE t.action = :action")
    , @NamedQuery(name = "TmsScheduler.findByActionStatus", query = "SELECT t FROM TmsScheduler t WHERE t.actionStatus = :actionStatus")
    , @NamedQuery(name = "TmsScheduler.findByIntrash", query = "SELECT t FROM TmsScheduler t WHERE t.intrash = :intrash")})
public class TmsScheduler implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "TMS_SCHEDULER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_SCHEDULER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_SCHEDULER_SEQ")
    @Basic(optional = false)//
    @Column(name = "SCHEDULE_ID")
    private BigDecimal scheduleId;
    @Column(name = "DATE_TIME", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date dateTime;
    @Size(max = 20)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NO_FILES")
    private Long noFiles;
    @Column(name = "APP_ID")
    private BigDecimal appId;
    @Size(max = 50)
    @Searchable
    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;
    @Column(name = "SCHEDULED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;
    @Size(max = 50)
    @Column(name = "DOWNLOAD_TYPE")
    private String downloadType;
    @Size(max = 150)
    @Column(name = "DIR_PATH")
    private String dirPath;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne
    @JsonIgnore
    private UfsDeviceModel modelId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne    
    private UfsProduct productId;
    @OneToMany(mappedBy = "scheduleId")
    @JsonIgnore
    private List<TmsScheduleFile> tmsScheduleFileList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduleId",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TmsScheduleEstate> tmsScheduleEstateList;
    @OneToMany(mappedBy = "scheduleId",fetch = FetchType.LAZY) 
    @JsonIgnore
    private List<TmsDeviceTask> tmsDeviceTaskList;

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

    public BigDecimal getAppId() {
        return appId;
    }

    public void setAppId(BigDecimal appId) {
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

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public UfsProduct getProductId() {
        return productId;
    }

    public void setProductId(UfsProduct productId) {
        this.productId = productId;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsScheduleFile> getTmsScheduleFileList() {
        return tmsScheduleFileList;
    }

    public void setTmsScheduleFileList(List<TmsScheduleFile> tmsScheduleFileList) {
        this.tmsScheduleFileList = tmsScheduleFileList;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsScheduleEstate> getTmsScheduleEstateList() {
        return tmsScheduleEstateList;
    }

    public void setTmsScheduleEstateList(List<TmsScheduleEstate> tmsScheduleEstateList) {
        this.tmsScheduleEstateList = tmsScheduleEstateList;
    }

    @XmlTransient
    public List<TmsDeviceTask> getTmsDeviceTaskList() {
        return tmsDeviceTaskList;
    }

    public void setTmsDeviceTaskList(List<TmsDeviceTask> tmsDeviceTaskList) {
        this.tmsDeviceTaskList = tmsDeviceTaskList;
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
        return "ke.co.tra.ufs.tms.entities.TmsScheduler[ scheduleId=" + scheduleId + " ]";
    }
    
}
