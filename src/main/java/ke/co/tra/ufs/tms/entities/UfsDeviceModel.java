/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_DEVICE_MODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsDeviceModel.findAll", query = "SELECT u FROM UfsDeviceModel u")
    , @NamedQuery(name = "UfsDeviceModel.findById", query = "SELECT u FROM UfsDeviceModel u WHERE u.modelId = :id And u.intrash = :intrash")
    , @NamedQuery(name = "UfsDeviceModel.findByMakeId", query = "SELECT u FROM UfsDeviceModel u WHERE u.makeId = :makeId")
    , @NamedQuery(name = "UfsDeviceModel.findByDeviceTypeId", query = "SELECT u FROM UfsDeviceModel u WHERE u.deviceTypeId = :deviceTypeId")
    , @NamedQuery(name = "UfsDeviceModel.findByModel", query = "SELECT u FROM UfsDeviceModel u WHERE u.model = :model")
    , @NamedQuery(name = "UfsDeviceModel.findByDescription", query = "SELECT u FROM UfsDeviceModel u WHERE u.description = :description")
    , @NamedQuery(name = "UfsDeviceModel.findByAction", query = "SELECT u FROM UfsDeviceModel u WHERE u.action = :action")
    , @NamedQuery(name = "UfsDeviceModel.findByActionStatus", query = "SELECT u FROM UfsDeviceModel u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsDeviceModel.findByIntrash", query = "SELECT u FROM UfsDeviceModel u WHERE u.intrash = :intrash")})
public class UfsDeviceModel implements Serializable {

    @JoinColumn(name = "MAKE_ID", referencedColumnName = "MAKE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UfsDeviceMake make;
    @JoinColumn(name = "DEVICE_TYPE_ID", referencedColumnName = "DEVICE_TYPE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UfsDeviceType deviceType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TmsWhitelist> whitelist;
    
    
    /*@JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TmsDeviceFileExt deviceFileExt;*/

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)    
    @GenericGenerator(
            name = "UFS_DEVICE_MODEL_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "UFS_DEVICE_MODEL_SEQ")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_DEVICE_MODEL_SEQ")
    @Column(name = "MODEL_ID")
    private BigDecimal modelId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAKE_ID")
    @ModifiableField
    private BigDecimal makeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEVICE_TYPE_ID")
    @ModifiableField
    private BigDecimal deviceTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MODEL")
    @ModifiableField
    private String model;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    @ModifiableField
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
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelId")
    private Set<TmsDevice> tmsDeviceList;
    
    @Transient
    private String pFileExt;

    public UfsDeviceModel() {
    }

    public UfsDeviceModel(BigDecimal modelId) {
        this.modelId = modelId;
    }

    public UfsDeviceModel(BigDecimal modelId, BigDecimal makeId, BigDecimal deviceTypeId, String model, String description) {
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

    public BigDecimal getMakeId() {
        return makeId;
    }

    public void setMakeId(BigDecimal makeId) {
        this.make = new UfsDeviceMake(makeId);
        this.makeId = makeId;
    }

    public BigDecimal getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(BigDecimal deviceTypeId) {
        this.deviceType = new UfsDeviceType(deviceTypeId);
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

    public UfsDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(UfsDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Set<TmsWhitelist> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<TmsWhitelist> whitelist) {
        this.whitelist = whitelist;
    }

    
//    public TmsDeviceFileExt getDeviceFileExt() {
//        return deviceFileExt;
//    }
//
//    public void setDeviceFileExt(TmsDeviceFileExt deviceFileExt) {
//        this.deviceFileExt = deviceFileExt;
//    }

    public Set<TmsDevice> getTmsDeviceList() {
        return tmsDeviceList;
    }

    public void setTmsDeviceList(Set<TmsDevice> tmsDeviceList) {
        this.tmsDeviceList = tmsDeviceList;
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
        return "ke.co.tra.ufs.tms.entities.UfsDeviceModel[ modelId=" + modelId + " ]";
    }

    public UfsDeviceMake getMake() {
        return make;
    }

    public void setMake(UfsDeviceMake make) {
        this.make = make;
    }

    public String getpFileExt() {
        return pFileExt;
    }

    public void setpFileExt(String pFileExt) {
        this.pFileExt = pFileExt;
    }
    
}
