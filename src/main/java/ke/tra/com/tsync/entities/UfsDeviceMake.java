/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_DEVICE_MAKE")
@NamedQueries({
    @NamedQuery(name = "UfsDeviceMake.findAll", query = "SELECT u FROM UfsDeviceMake u")})
public class UfsDeviceMake implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "MAKE_ID")
    private BigDecimal makeId;
    @Column(name = "VENDOR_NAME")
    private String vendorName;
    @Basic(optional = false)
    @Column(name = "MAKE")
    private String make;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;

    public UfsDeviceMake() {
    }

    public UfsDeviceMake(BigDecimal makeId) {
        this.makeId = makeId;
    }

    public UfsDeviceMake(BigDecimal makeId, String make, String action, String actionStatus) {
        this.makeId = makeId;
        this.make = make;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public BigDecimal getMakeId() {
        return makeId;
    }

    public void setMakeId(BigDecimal makeId) {
        this.makeId = makeId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
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
        hash += (makeId != null ? makeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsDeviceMake)) {
            return false;
        }
        UfsDeviceMake other = (UfsDeviceMake) object;
        if ((this.makeId == null && other.makeId != null) || (this.makeId != null && !this.makeId.equals(other.makeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsDeviceMake[ makeId=" + makeId + " ]";
    }
    
}
