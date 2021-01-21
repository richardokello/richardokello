/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_AUDIT_LOG")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsAuditLog.findAll", query = "SELECT u FROM UfsAuditLog u")
        , @NamedQuery(name = "UfsAuditLog.findByLogId", query = "SELECT u FROM UfsAuditLog u WHERE u.logId = :logId")
        , @NamedQuery(name = "UfsAuditLog.findByOccurenceTime", query = "SELECT u FROM UfsAuditLog u WHERE u.occurenceTime = :occurenceTime")
        , @NamedQuery(name = "UfsAuditLog.findByActivityType", query = "SELECT u FROM UfsAuditLog u WHERE u.activityType = :activityType")
        , @NamedQuery(name = "UfsAuditLog.findByStatus", query = "SELECT u FROM UfsAuditLog u WHERE u.status = :status")
        , @NamedQuery(name = "UfsAuditLog.findByEntityName", query = "SELECT u FROM UfsAuditLog u WHERE u.entityName = :entityName")
        , @NamedQuery(name = "UfsAuditLog.findByEntityId", query = "SELECT u FROM UfsAuditLog u WHERE u.entityId = :entityId")
        , @NamedQuery(name = "UfsAuditLog.findByDescription", query = "SELECT u FROM UfsAuditLog u WHERE u.description = :description")
        , @NamedQuery(name = "UfsAuditLog.findByNotes", query = "SELECT u FROM UfsAuditLog u WHERE u.notes = :notes")
        , @NamedQuery(name = "UfsAuditLog.findBySource", query = "SELECT u FROM UfsAuditLog u WHERE u.source = :source")
        , @NamedQuery(name = "UfsAuditLog.findByIpAddress", query = "SELECT u FROM UfsAuditLog u WHERE u.ipAddress = :ipAddress")
        , @NamedQuery(name = "UfsAuditLog.findByClientId", query = "SELECT u FROM UfsAuditLog u WHERE u.clientId = :clientId")
        , @NamedQuery(name = "UfsAuditLog.findByIntrash", query = "SELECT u FROM UfsAuditLog u WHERE u.intrash = :intrash")})
public class UfsAuditLog implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACTIVITY_TYPE")
    @Filter
    @Searchable
    private String activityType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 30)
    @Column(name = "STATUS")
    @Filter
    @Searchable
    private String status;
    @Size(max = 50)
    @Column(name = "ENTITY_NAME")
    @Filter
    @Searchable
    private String entityName;
    @Size(max = 20)
    @Column(name = "ENTITY_ID")
    @Filter
    @Searchable
    private String entityId;
    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 255)
    @Column(name = "NOTES")
    private String notes;
    @Size(max = 1000)
    @Column(name = "SOURCE")
    @Searchable
    private String source;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 20)
    @Column(name = "IP_ADDRESS")
    @Searchable
    private String ipAddress;
    @Size(max = 255)
    @Column(name = "CLIENT_ID")
    @Searchable
    private String clientId;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Column(name = "OCCURENCE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date occurenceTime;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser userId;

    public UfsAuditLog() {
    }

    public UfsAuditLog(BigDecimal logId) {
        this.logId = logId;
    }

    public UfsAuditLog(BigDecimal logId, String activityType, String status, String ipAddress, String intrash) {
        this.logId = logId;
        this.activityType = activityType;
        this.status = status;
        this.ipAddress = ipAddress;
        this.intrash = intrash;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public Date getOccurenceTime() {
        return occurenceTime;
    }

    public void setOccurenceTime(Date occurenceTime) {
        this.occurenceTime = occurenceTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }


    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
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
        if (!(object instanceof UfsAuditLog)) {
            return false;
        }
        UfsAuditLog other = (UfsAuditLog) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsAuditLog[ logId=" + logId + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

}
