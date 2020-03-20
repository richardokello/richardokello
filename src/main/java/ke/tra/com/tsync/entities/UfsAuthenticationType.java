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
@Table(name = "UFS_AUTHENTICATION_TYPE")
@NamedQueries({
    @NamedQuery(name = "UfsAuthenticationType.findAll", query = "SELECT u FROM UfsAuthenticationType u")})
public class UfsAuthenticationType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TYPE_ID")
    private Short typeId;
    @Basic(optional = false)
    @Column(name = "AUTHENTICATION_TYPE")
    private String authenticationType;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authenticationType")
    private Collection<UfsAuthentication> ufsAuthenticationCollection;

    public UfsAuthenticationType() {
    }

    public UfsAuthenticationType(Short typeId) {
        this.typeId = typeId;
    }

    public UfsAuthenticationType(Short typeId, String authenticationType, Date creationDate) {
        this.typeId = typeId;
        this.authenticationType = authenticationType;
        this.creationDate = creationDate;
    }

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
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

    public Collection<UfsAuthentication> getUfsAuthenticationCollection() {
        return ufsAuthenticationCollection;
    }

    public void setUfsAuthenticationCollection(Collection<UfsAuthentication> ufsAuthenticationCollection) {
        this.ufsAuthenticationCollection = ufsAuthenticationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsAuthenticationType)) {
            return false;
        }
        UfsAuthenticationType other = (UfsAuthenticationType) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsAuthenticationType[ typeId=" + typeId + " ]";
    }
    
}
