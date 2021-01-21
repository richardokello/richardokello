/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
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
@Table(name = "UFS_EMAIL_NOTIFICATION_TEMPL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEmailNotificationTempl.findAll", query = "SELECT u FROM UfsEmailNotificationTempl u"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findById", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.id = :id"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findBySubject", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.subject = :subject"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByBody", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.body = :body"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findBySignature", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.signature = :signature"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByType", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.type = :type"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByAction", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.action = :action"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByActionStatus", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByIntrash", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.intrash = :intrash"),
    @NamedQuery(name = "UfsEmailNotificationTempl.findByCreatedAt", query = "SELECT u FROM UfsEmailNotificationTempl u WHERE u.createdAt = :createdAt")})
public class UfsEmailNotificationTempl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_EMAIL_NOTIF_TEMPL_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_EMAIL_NOTIF_TEMPL_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_EMAIL_NOTIF_TEMPL_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 30)
    @Column(name = "SUBJECT")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 200)
    @Column(name = "BODY")
    private String body;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 30)
    @Column(name = "SIGNATURE")
    private String signature;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 10)
    @Column(name = "TYPE")
    private String type;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "CREATED_AT",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public UfsEmailNotificationTempl() {
    }

    public UfsEmailNotificationTempl(Long id) {
        this.id = id;
    }

    public UfsEmailNotificationTempl(Long id, String subject, String body, String signature, String type) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.signature = signature;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(object instanceof UfsEmailNotificationTempl)) {
            return false;
        }
        UfsEmailNotificationTempl other = (UfsEmailNotificationTempl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsEmailNotificationTempl[ id=" + id + " ]";
    }
    
}
