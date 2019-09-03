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
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_VOIDED_DEVICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsVoidedDevice.findAll", query = "SELECT t FROM TmsVoidedDevice t")
    , @NamedQuery(name = "TmsVoidedDevice.findById", query = "SELECT t FROM TmsVoidedDevice t WHERE t.id = :id")
    , @NamedQuery(name = "TmsVoidedDevice.findByDeviceId", query = "SELECT t FROM TmsVoidedDevice t WHERE t.deviceId = :deviceId")
    , @NamedQuery(name = "TmsVoidedDevice.findBySerialNo", query = "SELECT t FROM TmsVoidedDevice t WHERE t.serialNo = :serialNo")
    , @NamedQuery(name = "TmsVoidedDevice.findByModelId", query = "SELECT t FROM TmsVoidedDevice t WHERE t.modelId = :modelId")
    , @NamedQuery(name = "TmsVoidedDevice.findByUnitItemId", query = "SELECT t FROM TmsVoidedDevice t WHERE t.unitItemId = :unitItemId")
    , @NamedQuery(name = "TmsVoidedDevice.findByTid", query = "SELECT t FROM TmsVoidedDevice t WHERE t.tid = :tid")
    , @NamedQuery(name = "TmsVoidedDevice.findByOwnerId", query = "SELECT t FROM TmsVoidedDevice t WHERE t.ownerId = :ownerId")
    , @NamedQuery(name = "TmsVoidedDevice.findByCreationDate", query = "SELECT t FROM TmsVoidedDevice t WHERE t.creationDate = :creationDate")})
public class TmsVoidedDevice implements Serializable {

    @JoinColumn(name = "UNIT_ITEM_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    private TmsEstateItem unitItemId;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEVICE_ID")
    private BigInteger deviceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TID")
    private String tid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "OWNER_ID")
    private String ownerId;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public TmsVoidedDevice() {
    }

    public TmsVoidedDevice(BigDecimal id) {
        this.id = id;
    }

    public TmsVoidedDevice(BigDecimal id, BigInteger deviceId, String serialNo, UfsDeviceModel modelId, String tid, String ownerId) {
        this.id = id;
        this.deviceId = deviceId;
        this.serialNo = serialNo;
        this.modelId = modelId;
        this.tid = tid;
        this.ownerId = ownerId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(BigInteger deviceId) {
        this.deviceId = deviceId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public TmsEstateItem getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(TmsEstateItem unitItemId) {
        this.unitItemId = unitItemId;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        if (!(object instanceof TmsVoidedDevice)) {
            return false;
        }
        TmsVoidedDevice other = (TmsVoidedDevice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsVoidedDevice[ id=" + id + " ]";
    }

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
    }
    
}
