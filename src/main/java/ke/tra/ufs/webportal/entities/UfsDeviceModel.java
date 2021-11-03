/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import ke.axle.chassis.annotations.Filter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_DEVICE_MODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsDeviceModel.findAll", query = "SELECT u FROM UfsDeviceModel u"),
    @NamedQuery(name = "UfsDeviceModel.findByModelId", query = "SELECT u FROM UfsDeviceModel u WHERE u.modelId = :modelId"),
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
         @GenericGenerator(
            name = "UFS_DEVICE_MODEL_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_DEVICE_MODEL_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_DEVICE_MODEL_SEQ")
    @Column(name = "MODEL_ID")
    private BigDecimal modelId;
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
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "MAKE_ID", referencedColumnName = "MAKE_ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsDeviceMake makeId;
    @Column(name = "MAKE_ID")
    private BigDecimal makeIds;
    @JoinColumn(name = "DEVICE_TYPE_ID", referencedColumnName = "DEVICE_TYPE_ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsDeviceType deviceTypeId;
    @Column(name = "DEVICE_TYPE_ID")
    private BigDecimal deviceTypeIds;

    public UfsDeviceModel() {
    }

    public UfsDeviceModel(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public UfsDeviceModel(BigDecimal modelId, String model, String description) {
        this.modelId = modelId;
        this.model = model;
        this.description = description;
    }

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.modelId = modelId;
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


    public UfsDeviceMake getMakeId() {
        return makeId;
    }

    public void setMakeId(UfsDeviceMake makeId) {
        this.makeId = makeId;
    }

    public UfsDeviceType getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(UfsDeviceType deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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
        return "ke.tra.ufs.webportal.entities.UfsDeviceModel[ modelId=" + modelId + " ]";
    }
    
}
