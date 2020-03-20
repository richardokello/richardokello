/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_DEVICE_MODEL")
@NamedQueries({
    @NamedQuery(name = "UfsDeviceModel.findAll", query = "SELECT u FROM UfsDeviceModel u")})
public class UfsDeviceModel implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "MODEL_ID")
    private BigDecimal modelId;
    @Basic(optional = false)
    @Column(name = "MAKE_ID")
    private BigInteger makeId;
    @Basic(optional = false)
    @Column(name = "DEVICE_TYPE_ID")
    private BigInteger deviceTypeId;
    @Basic(optional = false)
    @Column(name = "MODEL")
    private String model;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelId")
    private Collection<TmsDevice> tmsDeviceCollection;

    public UfsDeviceModel() {
    }

    public UfsDeviceModel(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public UfsDeviceModel(BigDecimal modelId, BigInteger makeId, BigInteger deviceTypeId, String model, String description) {
        this.modelId = modelId;
        this.makeId = makeId;
        this.deviceTypeId = deviceTypeId;
        this.model = model;
        this.description = description;
    }

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public BigInteger getMakeId() {
        return makeId;
    }

    public void setMakeId(BigInteger makeId) {
        this.makeId = makeId;
    }

    public BigInteger getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(BigInteger deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsDeviceModel)) {
            return false;
        }
        UfsDeviceModel other = (UfsDeviceModel) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsDeviceModel[ modelId=" + modelId + " ]";
    }
    
}
