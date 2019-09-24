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
 * @author eli.muraya
 */
@Entity
@Table(name = "UFS_USER_TYPE", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsUserType.findAll", query = "SELECT u FROM UfsUserType u")
        , @NamedQuery(name = "UfsUserType.findByTypeId", query = "SELECT u FROM UfsUserType u WHERE u.typeId = :typeId")
        , @NamedQuery(name = "UfsUserType.findByUserType", query = "SELECT u FROM UfsUserType u WHERE u.userType = :userType")
        , @NamedQuery(name = "UfsUserType.findByDescription", query = "SELECT u FROM UfsUserType u WHERE u.description = :description")
        , @NamedQuery(name = "UfsUserType.findByAction", query = "SELECT u FROM UfsUserType u WHERE u.action = :action")
        , @NamedQuery(name = "UfsUserType.findByActionStatus", query = "SELECT u FROM UfsUserType u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsUserType.findByCreationDate", query = "SELECT u FROM UfsUserType u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsUserType.findByIntrash", query = "SELECT u FROM UfsUserType u WHERE u.intrash = :intrash")})
public class UfsUserType implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "USER_TYPE")
    private String userType;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "TYPE_ID")
    private BigDecimal typeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userType")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsUser> ufsUserList;

    public UfsUserType() {
    }

    public UfsUserType(BigDecimal typeId) {
        this.typeId = typeId;
    }

    public UfsUserType(BigDecimal typeId, String userType, Date creationDate, String intrash) {
        this.typeId = typeId;
        this.userType = userType;
        this.creationDate = creationDate;
        this.intrash = intrash;
    }

    public BigDecimal getTypeId() {
        return typeId;
    }

    public void setTypeId(BigDecimal typeId) {
        this.typeId = typeId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
    public List<UfsUser> getUfsUserList() {
        return ufsUserList;
    }

    public void setUfsUserList(List<UfsUser> ufsUserList) {
        this.ufsUserList = ufsUserList;
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
        if (!(object instanceof UfsUserType)) {
            return false;
        }
        UfsUserType other = (UfsUserType) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsUserType[ typeId=" + typeId + " ]";
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