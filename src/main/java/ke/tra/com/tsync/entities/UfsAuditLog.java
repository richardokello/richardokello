/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "UFS_AUDIT_LOG")
@NamedQueries({
    @NamedQuery(name = "UfsAuditLog.findAll", query = "SELECT u FROM UfsAuditLog u")})
public class UfsAuditLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Column(name = "OCCURENCE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurenceTime;
    @Column(name = "USER_ID")
    private Long userId;
    @Basic(optional = false)
    @Column(name = "ACTIVITY_TYPE")
    private String activityType;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "SOURCE")
    private String source;
    @Basic(optional = false)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Basic(optional = false)
    @Column(name = "INTRASH")
    private String intrash;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
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
        return "com.mycompany.oracleufs.UfsAuditLog[ logId=" + logId + " ]";
    }
    
}
