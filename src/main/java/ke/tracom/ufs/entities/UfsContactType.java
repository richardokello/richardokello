/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_CONTACT_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsContactType.findAll", query = "SELECT u FROM UfsContactType u")
    , @NamedQuery(name = "UfsContactType.findByContactTypeId", query = "SELECT u FROM UfsContactType u WHERE u.contactTypeId = :contactTypeId")
    , @NamedQuery(name = "UfsContactType.findByContactType", query = "SELECT u FROM UfsContactType u WHERE u.contactType = :contactType")
    , @NamedQuery(name = "UfsContactType.findByDescription", query = "SELECT u FROM UfsContactType u WHERE u.description = :description")
    , @NamedQuery(name = "UfsContactType.findByAction", query = "SELECT u FROM UfsContactType u WHERE u.action = :action")
    , @NamedQuery(name = "UfsContactType.findByActionStatus", query = "SELECT u FROM UfsContactType u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsContactType.findByIntrash", query = "SELECT u FROM UfsContactType u WHERE u.intrash = :intrash")
    , @NamedQuery(name = "UfsContactType.findByCreationDate", query = "SELECT u FROM UfsContactType u WHERE u.creationDate = :creationDate")})
public class UfsContactType implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CONTACT_TYPE")
    private String contactType;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_CONTACT_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CONTACT_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_CONTACT_TYPE_SEQ")
    @Column(name = "CONTACT_TYPE_ID")
    private Short contactTypeId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactType")
    private List<UfsContact> ufsContactList;

    public UfsContactType() {
    }

    public UfsContactType(Short contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    public UfsContactType(Short contactTypeId, String contactType, Date creationDate) {
        this.contactTypeId = contactTypeId;
        this.contactType = contactType;
        this.creationDate = creationDate;
    }

    public Short getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(Short contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
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

    @XmlTransient
    public List<UfsContact> getUfsContactList() {
        return ufsContactList;
    }

    public void setUfsContactList(List<UfsContact> ufsContactList) {
        this.ufsContactList = ufsContactList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactTypeId != null ? contactTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsContactType)) {
            return false;
        }
        UfsContactType other = (UfsContactType) object;
        if ((this.contactTypeId == null && other.contactTypeId != null) || (this.contactTypeId != null && !this.contactTypeId.equals(other.contactTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsContactType[ contactTypeId=" + contactTypeId + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
