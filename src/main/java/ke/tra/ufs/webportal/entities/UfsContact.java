/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_CONTACT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsContact.findAll", query = "SELECT u FROM UfsContact u")
    , @NamedQuery(name = "UfsContact.findByContactId", query = "SELECT u FROM UfsContact u WHERE u.contactId = :contactId")
    , @NamedQuery(name = "UfsContact.findByContact", query = "SELECT u FROM UfsContact u WHERE u.contact = :contact")
    , @NamedQuery(name = "UfsContact.findByAction", query = "SELECT u FROM UfsContact u WHERE u.action = :action")
    , @NamedQuery(name = "UfsContact.findByActionStatus", query = "SELECT u FROM UfsContact u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsContact.findByIntrash", query = "SELECT u FROM UfsContact u WHERE u.intrash = :intrash")
    , @NamedQuery(name = "UfsContact.findByCreationDate", query = "SELECT u FROM UfsContact u WHERE u.creationDate = :creationDate")})
public class UfsContact implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CONTACT")
    private String contact;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONTACT_ID")
    private Long contactId;
    @JoinColumn(name = "CONTACT_TYPE", referencedColumnName = "CONTACT_TYPE_ID")
    @ManyToOne(optional = false)
    private UfsContactType contactType;

    public UfsContact() {
    }

    public UfsContact(Long contactId) {
        this.contactId = contactId;
    }

    public UfsContact(Long contactId, String contact, Date creationDate) {
        this.contactId = contactId;
        this.contact = contact;
        this.creationDate = creationDate;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public UfsContactType getContactType() {
        return contactType;
    }

    public void setContactType(UfsContactType contactType) {
        this.contactType = contactType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactId != null ? contactId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsContact)) {
            return false;
        }
        UfsContact other = (UfsContact) object;
        if ((this.contactId == null && other.contactId != null) || (this.contactId != null && !this.contactId.equals(other.contactId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsContact[ contactId=" + contactId + " ]";
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
    
}
