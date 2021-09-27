/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_SIMCARD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceSimcard.findAll", query = "SELECT t FROM TmsDeviceSimcard t")
    , @NamedQuery(name = "TmsDeviceSimcard.findById", query = "SELECT t FROM TmsDeviceSimcard t WHERE t.id = :id")
    , @NamedQuery(name = "TmsDeviceSimcard.findByDeviceId", query = "SELECT t FROM TmsDeviceSimcard t WHERE t.deviceId = :deviceId")
    , @NamedQuery(name = "TmsDeviceSimcard.findByMnoId", query = "SELECT t FROM TmsDeviceSimcard t WHERE t.mnoId = :mnoId")
    , @NamedQuery(name = "TmsDeviceSimcard.findByMsisdn", query = "SELECT t FROM TmsDeviceSimcard t WHERE t.msisdn = :msisdn")
    , @NamedQuery(name = "TmsDeviceSimcard.findBySerialNo", query = "SELECT t FROM TmsDeviceSimcard t WHERE t.serialNo = :serialNo")})
public class TmsDeviceSimcard implements Serializable {

    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    @JsonIgnore
    private TmsDevice deviceId;
    @JoinColumn(name = "MNO_ID", referencedColumnName = "MNO_ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsMno mnoId;
    @Column(name = "MNO_ID")
    private BigDecimal mnoIds;


    @Transient
    private BigDecimal deviceIds;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_SIMCARD_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_SIMCARD_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_DEVICE_SIMCARD_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "MSISDN")
    private String msisdn;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "INTRASH",insertable = false)
    private String intrash;

    public TmsDeviceSimcard() {
    }

    public TmsDeviceSimcard(BigDecimal id) {
        this.id = id;
    }

    public TmsDeviceSimcard(BigDecimal id, TmsDevice deviceId, UfsMno mnoId, String msisdn, String serialNo) {
        this.id = id;
        this.deviceId = deviceId;
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
        return "ke.co.tra.ufs.tms.entities.TmsDeviceSimcard[ id=" + id + " ]";
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public UfsMno getMnoId() {
        return mnoId;
    }

    public void setMnoId(UfsMno mnoId) {
        this.mnoId = mnoId;
    }

    public BigDecimal getMnoIds() {
        return mnoIds;
    }

    public void setMnoIds(BigDecimal mnoIds) {
        this.mnoIds = mnoIds;
    }

    public BigDecimal getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(BigDecimal deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
}
