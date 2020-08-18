/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_DEVICE_TYPE")
@NamedQueries({
    @NamedQuery(name = "UfsDeviceType.findAll", query = "SELECT u FROM UfsDeviceType u")})
public class UfsDeviceType implements Serializable {

    @OneToMany(mappedBy = "deviceType")
    private Collection<TmsDevice> tmsDeviceCollection;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DEVICE_TYPE_ID")
    private BigDecimal deviceTypeId;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public UfsDeviceType() {
    }

    public UfsDeviceType(BigDecimal deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public UfsDeviceType(BigDecimal deviceTypeId, String type, String description) {
        this.deviceTypeId = deviceTypeId;
        this.type = type;
        this.description = description;
    }

    public BigDecimal getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(BigDecimal deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceTypeId != null ? deviceTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsDeviceType)) {
            return false;
        }
        UfsDeviceType other = (UfsDeviceType) object;
        if ((this.deviceTypeId == null && other.deviceTypeId != null) || (this.deviceTypeId != null && !this.deviceTypeId.equals(other.deviceTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsDeviceType[ deviceTypeId=" + deviceTypeId + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }
    
}
