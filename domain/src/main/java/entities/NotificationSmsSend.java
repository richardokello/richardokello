/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
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
@Table(name = "NOTIFICATION_SMS_SEND")
@NamedQueries({
    @NamedQuery(name = "NotificationSmsSend.findAll", query = "SELECT n FROM NotificationSmsSend n")})
public class NotificationSmsSend implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "TIME_TO_SEND")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeToSend;
    @Column(name = "MESSAGE_STATUS")
    private String messageStatus;
    @Basic(optional = false)
    @Column(name = "TIME_TO_SEND_TYPE")
    private String timeToSendType;
    @Basic(optional = false)
    @Column(name = "RECEPIENTS")
    private String recepients;
    @Column(name = "SUCCESSFULLY_SENT_TO")
    private String successfullySentTo;
    @Column(name = "FAILED_SENT_TO")
    private String failedSentTo;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public NotificationSmsSend() {
    }

    public NotificationSmsSend(Long id) {
        this.id = id;
    }

    public NotificationSmsSend(Long id, Date timeToSend, String timeToSendType, String recepients) {
        this.id = id;
        this.timeToSend = timeToSend;
        this.timeToSendType = timeToSendType;
        this.recepients = recepients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeToSend() {
        return timeToSend;
    }

    public void setTimeToSend(Date timeToSend) {
        this.timeToSend = timeToSend;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getTimeToSendType() {
        return timeToSendType;
    }

    public void setTimeToSendType(String timeToSendType) {
        this.timeToSendType = timeToSendType;
    }

    public String getRecepients() {
        return recepients;
    }

    public void setRecepients(String recepients) {
        this.recepients = recepients;
    }

    public String getSuccessfullySentTo() {
        return successfullySentTo;
    }

    public void setSuccessfullySentTo(String successfullySentTo) {
        this.successfullySentTo = successfullySentTo;
    }

    public String getFailedSentTo() {
        return failedSentTo;
    }

    public void setFailedSentTo(String failedSentTo) {
        this.failedSentTo = failedSentTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationSmsSend)) {
            return false;
        }
        NotificationSmsSend other = (NotificationSmsSend) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.NotificationSmsSend[ id=" + id + " ]";
    }
    
}
