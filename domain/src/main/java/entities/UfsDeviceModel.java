/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_DEVICE_MODEL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsDeviceModel.findAll", query = "SELECT u FROM UfsDeviceModel u"),
        @NamedQuery(name = "UfsDeviceModel.findByModelId", query = "SELECT u FROM UfsDeviceModel u WHERE u.modelId = :modelId"),
        @NamedQuery(name = "UfsDeviceModel.findByMakeId", query = "SELECT u FROM UfsDeviceModel u WHERE u.makeId = :makeId"),
        @NamedQuery(name = "UfsDeviceModel.findByDeviceTypeId", query = "SELECT u FROM UfsDeviceModel u WHERE u.deviceTypeId = :deviceTypeId"),
        @NamedQuery(name = "UfsDeviceModel.findByModel", query = "SELECT u FROM UfsDeviceModel u WHERE u.model = :model"),
        @NamedQuery(name = "UfsDeviceModel.findByDescription", query = "SELECT u FROM UfsDeviceModel u WHERE u.description = :description"),
        @NamedQuery(name = "UfsDeviceModel.findByAction", query = "SELECT u FROM UfsDeviceModel u WHERE u.action = :action"),
        @NamedQuery(name = "UfsDeviceModel.findByActionStatus", query = "SELECT u FROM UfsDeviceModel u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsDeviceModel.findByIntrash", query = "SELECT u FROM UfsDeviceModel u WHERE u.intrash = :intrash")})
public class UfsDeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODEL_ID")
    private BigDecimal modelId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAKE_ID")
    private BigInteger makeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEVICE_TYPE_ID")
    private BigInteger deviceTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MODEL")
    private String model;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
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

    @XmlTransient
    @JsonIgnore
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
        return "UfsDeviceModel[ modelId=" + modelId + " ]";
    }

}
