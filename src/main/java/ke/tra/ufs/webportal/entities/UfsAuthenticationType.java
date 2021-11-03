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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_AUTHENTICATION_TYPE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsAuthenticationType.findAll", query = "SELECT u FROM UfsAuthenticationType u")
        , @NamedQuery(name = "UfsAuthenticationType.findByTypeId", query = "SELECT u FROM UfsAuthenticationType u WHERE u.typeId = :typeId")
        , @NamedQuery(name = "UfsAuthenticationType.findByAuthenticationType", query = "SELECT u FROM UfsAuthenticationType u WHERE u.authenticationType = :authenticationType")
        , @NamedQuery(name = "UfsAuthenticationType.findByDescription", query = "SELECT u FROM UfsAuthenticationType u WHERE u.description = :description")
        , @NamedQuery(name = "UfsAuthenticationType.findByAction", query = "SELECT u FROM UfsAuthenticationType u WHERE u.action = :action")
        , @NamedQuery(name = "UfsAuthenticationType.findByActionStatus", query = "SELECT u FROM UfsAuthenticationType u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsAuthenticationType.findByIntrash", query = "SELECT u FROM UfsAuthenticationType u WHERE u.intrash = :intrash")
        , @NamedQuery(name = "UfsAuthenticationType.findByCreationDate", query = "SELECT u FROM UfsAuthenticationType u WHERE u.creationDate = :creationDate")})
public class UfsAuthenticationType implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUTHENTICATION_TYPE")
    private String authenticationType;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
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
    @Column(name = "TYPE_ID")
    private BigDecimal typeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authenticationType")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsAuthentication> ufsAuthenticationList;

    public UfsAuthenticationType() {
    }

    public UfsAuthenticationType(BigDecimal typeId) {
        this.typeId = typeId;
    }

    public UfsAuthenticationType(BigDecimal typeId, String authenticationType, Date creationDate) {
        this.typeId = typeId;
        this.authenticationType = authenticationType;
        this.creationDate = creationDate;
    }

    public BigDecimal getTypeId() {
        return typeId;
    }

    public void setTypeId(BigDecimal typeId) {
        this.typeId = typeId;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
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
    public List<UfsAuthentication> getUfsAuthenticationList() {
        return ufsAuthenticationList;
    }

    public void setUfsAuthenticationList(List<UfsAuthentication> ufsAuthenticationList) {
        this.ufsAuthenticationList = ufsAuthenticationList;
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
        return "ke.tracom.ufs.entities.UfsAuthenticationType[ typeId=" + typeId + " ]";
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
