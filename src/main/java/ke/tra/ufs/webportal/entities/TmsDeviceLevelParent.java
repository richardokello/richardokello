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
import java.math.BigDecimal;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_LEVEL_PARENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceLevelParent.findAll", query = "SELECT t FROM TmsDeviceLevelParent t")
    , @NamedQuery(name = "TmsDeviceLevelParent.findById", query = "SELECT t FROM TmsDeviceLevelParent t WHERE t.id = :id")
    , @NamedQuery(name = "TmsDeviceLevelParent.findByUnitLevel", query = "SELECT t FROM TmsDeviceLevelParent t WHERE t.unitLevel = :unitLevel")})
public class TmsDeviceLevelParent implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "UNIT_LEVEL")
    private String unitLevel;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne(optional = false)
    private TmsEstateItem parentId;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;

    public TmsDeviceLevelParent() {
    }

    public TmsDeviceLevelParent(BigDecimal id) {
        this.id = id;
    }

    public TmsDeviceLevelParent(BigDecimal id, String unitLevel) {
        this.id = id;
        this.unitLevel = unitLevel;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUnitLevel() {
        return unitLevel;
    }

    public void setUnitLevel(String unitLevel) {
        this.unitLevel = unitLevel;
    }

    public TmsEstateItem getParentId() {
        return parentId;
    }

    public void setParentId(TmsEstateItem parentId) {
        this.parentId = parentId;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
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
        if (!(object instanceof TmsDeviceLevelParent)) {
            return false;
        }
        TmsDeviceLevelParent other = (TmsDeviceLevelParent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceLevelParent[ id=" + id + " ]";
    }
    
}
