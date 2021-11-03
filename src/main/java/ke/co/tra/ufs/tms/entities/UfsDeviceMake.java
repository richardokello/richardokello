/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Table(name = "UFS_DEVICE_MAKE")
@NickName(name = "Device Make")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsDeviceMake.findAll", query = "SELECT u FROM UfsDeviceMake u")
    , @NamedQuery(name = "UfsDeviceMake.findByMakeId", query = "SELECT u FROM UfsDeviceMake u WHERE u.makeId = :makeId")
    , @NamedQuery(name = "UfsDeviceMake.findByVendorName", query = "SELECT u FROM UfsDeviceMake u WHERE u.vendorName = :vendorName")
    , @NamedQuery(name = "UfsDeviceMake.findByMake", query = "SELECT u FROM UfsDeviceMake u WHERE u.make = :make")
    , @NamedQuery(name = "UfsDeviceMake.findByDescription", query = "SELECT u FROM UfsDeviceMake u WHERE u.description = :description")
    , @NamedQuery(name = "UfsDeviceMake.findByAction", query = "SELECT u FROM UfsDeviceMake u WHERE u.action = :action")
    , @NamedQuery(name = "UfsDeviceMake.findByActionStatus", query = "SELECT u FROM UfsDeviceMake u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsDeviceMake.findByIntrash", query = "SELECT u FROM UfsDeviceMake u WHERE u.intrash = :intrash")})
public class UfsDeviceMake implements Serializable {

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    private String action;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "makeId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UfsDeviceModel> ufsDeviceModelSet;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_DEVICE_MAKE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "UFS_DEVICE_MAKE_SEQ")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_DEVICE_MAKE_SEQ")
    @Column(name = "MAKE_ID")
    private BigDecimal makeId;
    @Size(max = 100)
    @Column(name = "VENDOR_NAME")
    @ModifiableField
    private String vendorName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MAKE")
    @ModifiableField
    private String make;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH",insertable = false)
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

    @ApiModelProperty(hidden = true)
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
        return "ke.co.tra.ufs.tms.entities.UfsDeviceMake[ makeId=" + makeId + " ]";
    }

    @ApiModelProperty(hidden = true)
    public Set<UfsDeviceModel> getUfsDeviceModelSet() {
        return ufsDeviceModelSet;
    }

    public void setUfsDeviceModelSet(Set<UfsDeviceModel> ufsDeviceModelSet) {
        this.ufsDeviceModelSet = ufsDeviceModelSet;
    }
    
}
