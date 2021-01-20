/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_FTP_LOGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsFtpLogs.findAll", query = "SELECT t FROM TmsFtpLogs t")
    , @NamedQuery(name = "TmsFtpLogs.findByLogId", query = "SELECT t FROM TmsFtpLogs t WHERE t.logId = :logId")
    , @NamedQuery(name = "TmsFtpLogs.findByDateTimeAdded", query = "SELECT t FROM TmsFtpLogs t WHERE t.dateTimeAdded = :dateTimeAdded")
    , @NamedQuery(name = "TmsFtpLogs.findByLogger", query = "SELECT t FROM TmsFtpLogs t WHERE t.logger = :logger")
    , @NamedQuery(name = "TmsFtpLogs.findByLogLevel", query = "SELECT t FROM TmsFtpLogs t WHERE t.logLevel = :logLevel")
    , @NamedQuery(name = "TmsFtpLogs.findByMessage", query = "SELECT t FROM TmsFtpLogs t WHERE t.message = :message")
    , @NamedQuery(name = "TmsFtpLogs.findBySourceIp", query = "SELECT t FROM TmsFtpLogs t WHERE t.sourceIp = :sourceIp")
    , @NamedQuery(name = "TmsFtpLogs.findByTerminalSerial", query = "SELECT t FROM TmsFtpLogs t WHERE t.terminalSerial = :terminalSerial")
    , @NamedQuery(name = "TmsFtpLogs.findBySessionId", query = "SELECT t FROM TmsFtpLogs t WHERE t.sessionId = :sessionId")
    , @NamedQuery(name = "TmsFtpLogs.findByStacktrace", query = "SELECT t FROM TmsFtpLogs t WHERE t.stacktrace = :stacktrace")})
public class TmsFtpLogs implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "LOG_ID", nullable = false, precision = 38, scale = 0)
    private BigDecimal logId;
    @Column(name = "DATE_TIME_ADDED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeAdded;
    @Column(name = "LOGGER", length = 100)
    private String logger;
    @Column(name = "LOG_LEVEL", length = 7)
    private String logLevel;
    @Column(name = "MESSAGE", length = 1000)
    private String message;
    @Column(name = "SOURCE_IP", length = 100)
    private String sourceIp;
    @Column(name = "TERMINAL_SERIAL", length = 15)
    private String terminalSerial;
    @Column(name = "SESSION_ID", length = 36)
    private String sessionId;
    @Column(name = "STACKTRACE", length = 3000)
    private String stacktrace;

    public TmsFtpLogs() {
    }

    public TmsFtpLogs(BigDecimal logId) {
        this.logId = logId;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public Date getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(Date dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getTerminalSerial() {
        return terminalSerial;
    }

    public void setTerminalSerial(String terminalSerial) {
        this.terminalSerial = terminalSerial;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
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
        if (!(object instanceof TmsFtpLogs)) {
            return false;
        }
        TmsFtpLogs other = (TmsFtpLogs) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TmsFtpLogs[ logId=" + logId + " ]";
    }
    
}
