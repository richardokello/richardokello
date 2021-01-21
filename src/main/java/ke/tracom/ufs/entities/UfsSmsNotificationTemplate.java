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
@Table(name = "UFS_SMS_NOTIFICATION_TEMPLATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsSmsNotificationTemplate.findAll", query = "SELECT u FROM UfsSmsNotificationTemplate u"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findById", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.id = :id"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByTitle", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.title = :title"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByDescription", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.description = :description"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByContent", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.content = :content"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByType", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.type = :type"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByAction", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.action = :action"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByActionStatus", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByIntrash", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.intrash = :intrash"),
    @NamedQuery(name = "UfsSmsNotificationTemplate.findByCreatedAt", query = "SELECT u FROM UfsSmsNotificationTemplate u WHERE u.createdAt = :createdAt")})
public class UfsSmsNotificationTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "UFS_SMS_NOTIFICATION_TEMP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_SMS_NOTIFICATION_TEMP_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_SMS_NOTIFICATION_TEMP_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 20)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 30)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 200)
    @Column(name = "CONTENT")
    private String content;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 15)
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

    public UfsSmsNotificationTemplate() {
    }

    public UfsSmsNotificationTemplate(Long id) {
        this.id = id;
    }

    public UfsSmsNotificationTemplate(Long id, String title, String description, String content, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (!(object instanceof UfsSmsNotificationTemplate)) {
            return false;
        }
        UfsSmsNotificationTemplate other = (UfsSmsNotificationTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsSmsNotificationTemplate[ id=" + id + " ]";
    }
    
}
