/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_DEVICE_SIMCARD")
@NamedQueries({
    @NamedQuery(name = "TmsDeviceSimcard.findAll", query = "SELECT t FROM TmsDeviceSimcard t")})
public class TmsDeviceSimcard implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "MNO_ID")
    private BigInteger mnoId;
    @Basic(optional = false)
    @Column(name = "MSISDN")
    private String msisdn;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;

    public TmsDeviceSimcard() {
    }

    public TmsDeviceSimcard(BigDecimal id) {
        this.id = id;
    }

    public TmsDeviceSimcard(BigDecimal id, BigInteger mnoId, String msisdn, String serialNo) {
        this.id = id;
        this.mnoId = mnoId;
        this.msisdn = msisdn;
        this.serialNo = serialNo;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getMnoId() {
        return mnoId;
    }

    public void setMnoId(BigInteger mnoId) {
        this.mnoId = mnoId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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
        if (!(object instanceof TmsDeviceSimcard)) {
            return false;
        }
        TmsDeviceSimcard other = (TmsDeviceSimcard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsDeviceSimcard[ id=" + id + " ]";
    }
    
}
