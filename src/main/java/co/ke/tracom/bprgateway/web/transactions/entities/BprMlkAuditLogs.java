package co.ke.tracom.bprgateway.web.transactions.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "MLK_AUDIT_LOG")
@XmlRootElement

public class BprMlkAuditLogs implements java.io.Serializable {
    private static final long serialVersionUID=1l;
    @Id


    @Column(name = "LOG_ID")
    private BigDecimal logId;

    @Column(name = "OCCURENCE_TIME", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurenceTime;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACTIVITY_TYPE")
    private String activityType;
    // @Filter

    @NotNull
    @Column(name = "STATUS")
    private String status;

    @Size(max = 50)
    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Size(max = 20)
    @Column(name = "ENTITY_ID")
    private String entityId;

    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 255)
    @Column(name = "NOTES")
    private String notes;
    //  @Filter
    @Size(max = 1000)
    @Column(name = "SOURCE")
    private String source;
    //@Filter

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Size(max = 255)
    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "INTRASH")
    private String intrash;
    //  @Filter
    @Column(name = "USER_ID")
    private String _userId;



    public BigDecimal getLogId() {
        return logId;
    }


    public BprMlkAuditLogs setLogId(BigDecimal logId) {
        this.logId = logId;
        return this;
    }

    public Date getOccurenceTime() {
        return occurenceTime;
    }

    public BprMlkAuditLogs setOccurenceTime(Date occurenceTime) {
        this.occurenceTime = occurenceTime;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public BprMlkAuditLogs setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BprMlkAuditLogs setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public BprMlkAuditLogs setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getEntityId() {
        return entityId;
    }

    public BprMlkAuditLogs setEntityId(String entityId) {
        this.entityId = entityId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BprMlkAuditLogs setDescription(String description) {
        this.description = description;
        return this;
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

    public BprMlkAuditLogs setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public BprMlkAuditLogs setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
//
//    public MlkUser getUserId() {
//        return userId;
//    }
//
//    public void setUserId(MlkUser userId) {
//        this.userId = userId;
//    }

    public String get_userId() {
        return _userId;
    }

    public BprMlkAuditLogs set_userId(String _userId) {
        this._userId = _userId;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public BprMlkAuditLogs setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }
}

