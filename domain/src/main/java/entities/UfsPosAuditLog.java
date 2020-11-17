/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cotuoma
 */
@Entity
@Table(name = "UFS_POS_AUDIT_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsPosAuditLog.findAll", query = "SELECT u FROM UfsPosAuditLog u"),
    @NamedQuery(name = "UfsPosAuditLog.findByLogId", query = "SELECT u FROM UfsPosAuditLog u WHERE u.logId = :logId"),
    @NamedQuery(name = "UfsPosAuditLog.findByOccurenceTime", query = "SELECT u FROM UfsPosAuditLog u WHERE u.occurenceTime = :occurenceTime"),
    @NamedQuery(name = "UfsPosAuditLog.findByUserId", query = "SELECT u FROM UfsPosAuditLog u WHERE u.userId = :userId"),
    @NamedQuery(name = "UfsPosAuditLog.findByActivityType", query = "SELECT u FROM UfsPosAuditLog u WHERE u.activityType = :activityType"),
    @NamedQuery(name = "UfsPosAuditLog.findByStatus", query = "SELECT u FROM UfsPosAuditLog u WHERE u.status = :status"),
    @NamedQuery(name = "UfsPosAuditLog.findByEntityName", query = "SELECT u FROM UfsPosAuditLog u WHERE u.entityName = :entityName"),
    @NamedQuery(name = "UfsPosAuditLog.findByEntityId", query = "SELECT u FROM UfsPosAuditLog u WHERE u.entityId = :entityId"),
    @NamedQuery(name = "UfsPosAuditLog.findByDescription", query = "SELECT u FROM UfsPosAuditLog u WHERE u.description = :description"),
    @NamedQuery(name = "UfsPosAuditLog.findByNotes", query = "SELECT u FROM UfsPosAuditLog u WHERE u.notes = :notes"),
    @NamedQuery(name = "UfsPosAuditLog.findBySource", query = "SELECT u FROM UfsPosAuditLog u WHERE u.source = :source"),
    @NamedQuery(name = "UfsPosAuditLog.findByIpAddress", query = "SELECT u FROM UfsPosAuditLog u WHERE u.ipAddress = :ipAddress"),
    @NamedQuery(name = "UfsPosAuditLog.findByClientId", query = "SELECT u FROM UfsPosAuditLog u WHERE u.clientId = :clientId"),
    @NamedQuery(name = "UfsPosAuditLog.findByIntrash", query = "SELECT u FROM UfsPosAuditLog u WHERE u.intrash = :intrash")})
public class UfsPosAuditLog implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACTIVITY_TYPE")
    private String activityType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 50)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Size(max = 64)
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 255)
    @Column(name = "NOTES")
    private String notes;
    @Size(max = 1000)
    @Column(name = "SOURCE")
    private String source;
    @Basic(optional = false)

    @Size(min = 1, max = 20)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Size(max = 255)
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Basic(optional = false)

    @Size(min = 1, max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "UFS_POS_AUDIT_LOG_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_POS_AUDIT_LOG_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_POS_AUDIT_LOG_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Column(name = "OCCURENCE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurenceTime;
    @Column(name = "USER_ID")
    private Long userId;

    public UfsPosAuditLog() {
    }

    public UfsPosAuditLog(BigDecimal logId) {
        this.logId = logId;
    }

    public UfsPosAuditLog(BigDecimal logId, String activityType, String status, String ipAddress, String intrash) {
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsPosAuditLog)) {
            return false;
        }
        UfsPosAuditLog other = (UfsPosAuditLog) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.com.tsync.entities.UfsPosAuditLog[ logId=" + logId + " ]";
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
    
}
