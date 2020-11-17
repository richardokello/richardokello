/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_VOIDED_DEVICE")
@NamedQueries({
    @NamedQuery(name = "TmsVoidedDevice.findAll", query = "SELECT t FROM TmsVoidedDevice t")})
public class TmsVoidedDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "DEVICE_ID")
    private BigInteger deviceId;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Basic(optional = false)
    @Column(name = "MODEL_ID")
    private BigInteger modelId;
    @Column(name = "UNIT_ITEM_ID")
    private BigInteger unitItemId;
    @Column(name = "TID")
    private String tid;
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

    public TmsVoidedDevice(BigDecimal id, String serialNo, BigInteger modelId) {
        this.id = id;
        this.serialNo = serialNo;
        this.modelId = modelId;
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

    public BigInteger getModelId() {
        return modelId;
    }

    public void setModelId(BigInteger modelId) {
        this.modelId = modelId;
    }

    public BigInteger getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(BigInteger unitItemId) {
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
        return "com.mycompany.oracleufs.TmsVoidedDevice[ id=" + id + " ]";
    }
    
}
