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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;
import ke.co.tra.ufs.tms.utils.annotations.NickName;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author tracom9
 */
@Entity
@NickName(name = "Device Type")
@Table(name = "UFS_DEVICE_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsDeviceType.findAll", query = "SELECT u FROM UfsDeviceType u")
    , @NamedQuery(name = "UfsDeviceType.findByDeviceTypeId", query = "SELECT u FROM UfsDeviceType u WHERE u.deviceTypeId = :deviceTypeId")
    , @NamedQuery(name = "UfsDeviceType.findByType", query = "SELECT u FROM UfsDeviceType u WHERE u.type = :type")
    , @NamedQuery(name = "UfsDeviceType.findByDescription", query = "SELECT u FROM UfsDeviceType u WHERE u.description = :description")
    , @NamedQuery(name = "UfsDeviceType.findByAction", query = "SELECT u FROM UfsDeviceType u WHERE u.action = :action")
    , @NamedQuery(name = "UfsDeviceType.findByActionStatus", query = "SELECT u FROM UfsDeviceType u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsDeviceType.findByIntrash", query = "SELECT u FROM UfsDeviceType u WHERE u.intrash = :intrash")})
public class UfsDeviceType implements Serializable {

    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceType", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UfsDeviceModel> deviceModels;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_DEVICE_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "UFS_DEVICE_TYPE_SEQ")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_DEVICE_TYPE_SEQ")
    @Column(name = "DEVICE_TYPE_ID")
    private BigDecimal deviceTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TYPE")
    @ModifiableField
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
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

    public Set<UfsDeviceModel> getDeviceModels() {
        return deviceModels;
    }

    public void setDeviceModels(Set<UfsDeviceModel> deviceModels) {
        this.deviceModels = deviceModels;
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
        return "ke.co.tra.ufs.tms.entities.UfsDeviceType[ deviceTypeId=" + deviceTypeId + " ]";
    }
    
}
