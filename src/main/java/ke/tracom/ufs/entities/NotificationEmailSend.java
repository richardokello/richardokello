/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kenny
 */
@Entity
@Table(name = "NOTIFICATION_EMAIL_SEND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificationEmailSend.findAll", query = "SELECT n FROM NotificationEmailSend n"),
    @NamedQuery(name = "NotificationEmailSend.findById", query = "SELECT n FROM NotificationEmailSend n WHERE n.id = :id"),
    @NamedQuery(name = "NotificationEmailSend.findByTimeToSend", query = "SELECT n FROM NotificationEmailSend n WHERE n.timeToSend = :timeToSend"),
    @NamedQuery(name = "NotificationEmailSend.findByMessageStatus", query = "SELECT n FROM NotificationEmailSend n WHERE n.messageStatus = :messageStatus"),
    @NamedQuery(name = "NotificationEmailSend.findByTimeToSendType", query = "SELECT n FROM NotificationEmailSend n WHERE n.timeToSendType = :timeToSendType"),
    @NamedQuery(name = "NotificationEmailSend.findByRecepients", query = "SELECT n FROM NotificationEmailSend n WHERE n.recepients = :recepients"),
    @NamedQuery(name = "NotificationEmailSend.findBySuccessfullySentTo", query = "SELECT n FROM NotificationEmailSend n WHERE n.successfullySentTo = :successfullySentTo"),
    @NamedQuery(name = "NotificationEmailSend.findByFailedSentTo", query = "SELECT n FROM NotificationEmailSend n WHERE n.failedSentTo = :failedSentTo"),
    @NamedQuery(name = "NotificationEmailSend.findByContent", query = "SELECT n FROM NotificationEmailSend n WHERE n.content = :content"),
    @NamedQuery(name = "NotificationEmailSend.findBySignature", query = "SELECT n FROM NotificationEmailSend n WHERE n.signature = :signature"),
    @NamedQuery(name = "NotificationEmailSend.findByCreatedAt", query = "SELECT n FROM NotificationEmailSend n WHERE n.createdAt = :createdAt")})
public class NotificationEmailSend implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "NOTIFICATION_EMAIL_SEND_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "NOTIFICATION_EMAIL_SEND_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "NOTIFICATION_EMAIL_SEND_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIME_TO_SEND")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeToSend;
    @Size(max = 20)
    @Column(name = "MESSAGE_STATUS")
    private String messageStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Filter
    @Column(name = "TIME_TO_SEND_TYPE")
    private String timeToSendType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "RECEPIENTS")
    private String recepients;
    @Size(max = 200)
    @Column(name = "SUCCESSFULLY_SENT_TO")
    private String successfullySentTo;
    @Size(max = 200)
    @Column(name = "FAILED_SENT_TO")
    private String failedSentTo;
    @Size(max = 200)
    @Column(name = "CONTENT")
    private String content;
    @Size(max = 20)
    @Column(name = "SIGNATURE")
    private String signature;
    @Column(name = "CREATED_AT",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ATTACHEMENT")
    private String attachement;
    @Column(name = "CC")
    private String cc;
    @Column(name = "BCC")
    private String bcc;


    public NotificationEmailSend() {
    }

    public NotificationEmailSend(Long id) {
        this.id = id;
    }

    public NotificationEmailSend(Long id, Date timeToSend, String timeToSendType, String recepients) {
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
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
        if (!(object instanceof NotificationEmailSend)) {
            return false;
        }
        NotificationEmailSend other = (NotificationEmailSend) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.NotificationEmailSend[ id=" + id + " ]";
    }
    
}
