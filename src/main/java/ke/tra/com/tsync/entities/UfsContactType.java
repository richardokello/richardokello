/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_CONTACT_TYPE")
@NamedQueries({
    @NamedQuery(name = "UfsContactType.findAll", query = "SELECT u FROM UfsContactType u")})
public class UfsContactType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CONTACT_TYPE_ID")
    private Short contactTypeId;
    @Basic(optional = false)
    @Column(name = "CONTACT_TYPE")
    private String contactType;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactType")
    private Collection<UfsContact> ufsContactCollection;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Collection<UfsContact> getUfsContactCollection() {
        return ufsContactCollection;
    }

    public void setUfsContactCollection(Collection<UfsContact> ufsContactCollection) {
        this.ufsContactCollection = ufsContactCollection;
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
        return "com.mycompany.oracleufs.UfsContactType[ contactTypeId=" + contactTypeId + " ]";
    }
    
}
