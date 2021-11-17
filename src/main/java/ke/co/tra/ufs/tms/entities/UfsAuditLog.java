/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import ke.co.tra.ufs.tms.utils.annotations.ExportField;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author tracom9
 */
@Entity
@Table(name = "UFS_AUDIT_LOG")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsAuditLog.findAll", query = "SELECT m FROM UfsAuditLog m"),
        @NamedQuery(name = "UfsAuditLog.findByLogId", query = "SELECT m FROM UfsAuditLog m WHERE m.logId = :logId"),
        @NamedQuery(name = "UfsAuditLog.findByOccurenceTime", query = "SELECT m FROM UfsAuditLog m WHERE m.occurenceTime = :occurenceTime"),
        @NamedQuery(name = "UfsAuditLog.findByActivityType", query = "SELECT m FROM UfsAuditLog m WHERE m.activityType = :activityType"),
        @NamedQuery(name = "UfsAuditLog.findByStatus", query = "SELECT m FROM UfsAuditLog m WHERE m.status = :status"),
        @NamedQuery(name = "UfsAuditLog.findByEntityName", query = "SELECT m FROM UfsAuditLog m WHERE m.entityName = :entityName"),
        @NamedQuery(name = "UfsAuditLog.findByEntityId", query = "SELECT m FROM UfsAuditLog m WHERE m.entityId = :entityId"),
        @NamedQuery(name = "UfsAuditLog.findByDescription", query = "SELECT m FROM UfsAuditLog m WHERE m.description = :description"),
        @NamedQuery(name = "UfsAuditLog.findByNotes", query = "SELECT m FROM UfsAuditLog m WHERE m.notes = :notes"),
        @NamedQuery(name = "UfsAuditLog.findBySource", query = "SELECT m FROM UfsAuditLog m WHERE m.source = :source"),
        @NamedQuery(name = "UfsAuditLog.findByIpAddress", query = "SELECT m FROM UfsAuditLog m WHERE m.ipAddress = :ipAddress"),
        @NamedQuery(name = "UfsAuditLog.findByClientId", query = "SELECT m FROM UfsAuditLog m WHERE m.clientId = :clientId")})

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "IS_MAKER",
                procedureName = "IS_MAKER",
//                resultClasses = {Boolean.class},
                parameters = {
                        @StoredProcedureParameter(
                                name = "USER_ID_",
                                type = Long.class,
                                mode = ParameterMode.IN)
                        ,
                        @StoredProcedureParameter(
                                name = "ENTITY_NAME_",
                                type = String.class,
                                mode = ParameterMode.IN)
                        ,
                        @StoredProcedureParameter(
                                name = "ENTITY_ID_",
                                type = String.class,
                                mode = ParameterMode.IN)
                        ,
                        @StoredProcedureParameter(
                                name = "ACTIVITY_TYPE_",
                                type = String.class,
                                mode = ParameterMode.IN)
                        ,
                        @StoredProcedureParameter(
                                name = "STATUS_",
                                type = String.class,
                                mode = ParameterMode.IN)
                        ,
                        @StoredProcedureParameter(
                                name = "RESULT",
//                                type = String.class,
                                type = Boolean.class,
                                mode = ParameterMode.OUT)})
})
public class UfsAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "UFS_AUDIT_LOG_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_AUDIT_LOG_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_AUDIT_LOG_SEQ")
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Filter(isDateRange = true)
    @Column(name = "OCCURENCE_TIME", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurenceTime;
    @Filter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "ACTIVITY_TYPE")
    private String activityType;
    @Filter
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private String status;
    @Searchable
    @Filter
    @Size(max = 50)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Searchable
    @Filter
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Searchable
    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 4000)
    @Column(name = "NOTES")
    private String notes;
    @Filter
    @Size(max = 1000)
    @Column(name = "SOURCE")
    private String source;
    @Filter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Size(max = 255)
    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @Filter
    @Column(name = "USER_ID")
    private Long _userId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsUser userId;
    @Transient
    private String dateTime;

    public UfsAuditLog() {
    }

    public UfsAuditLog(BigDecimal logId, String activityType, String status, String ipAddress) {
        this.logId = logId;
        this.activityType = activityType;
        this.status = status;
        this.ipAddress = ipAddress;
    }

    public UfsAuditLog(Long user, String activityType, String status, String entityName,
                       String entityId, String description, String notes, String source, String ipAddress) {
        this._userId = user;
        this.activityType = activityType;
        this.status = status;
        this.entityName = entityName;
        this.entityId = entityId;
        this.description = description;
        this.notes = notes;
        this.source = source;
        this.ipAddress = ipAddress;
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

    public UfsAuditLog(Date currentDate, String activityType, String status, String entityName, String entityId, String description, String source, String ipAddress, Long user, String notes) {
        this._userId = user;
        this.activityType = activityType;
        this.status = status;
        this.description = description;
        this.entityName = entityName;
        this.entityId = entityId;
        this.notes = notes;
        this.source = source;
        this.ipAddress = ipAddress;
        this.occurenceTime = currentDate;
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

    public Long get_userId() {
        return _userId;
    }

    public void set_userId(Long _userId) {
        this._userId = _userId;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
