/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Kenny
 */
@Entity
@Table(name = "TMS_DEVICE_TIDS_MIDS")
public class TmsDeviceTidsMids implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "TMS_DEVICE_TIDS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_TIDS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_DEVICE_TIDS_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID",updatable = false,insertable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private TmsDevice deviceId;

    @Column(name = "DEVICE_ID")
    private Long deviceIds;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TID")
    private String tid;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MID")
    private String mid;

    @JoinColumn(name = "SWITCH", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsSwitches switchId;
    @Column(name = "SWITCH")
    @Filter
    @ModifiableField
    private Long switchIds;
    @JoinColumn(name = "CURRENCY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsCurrency currencyId;
    @Column(name = "CURRENCY")
    @Filter
    @ModifiableField
    private BigDecimal currencyIds;

    @Size(max = 3)
    @Filter
    @Searchable
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    public TmsDeviceTidsMids() {
    }

    public TmsDeviceTidsMids(Long id) {
        this.id = id;
    }

    public TmsDeviceTidsMids(Long id, TmsDevice deviceId, String tid, String mid) {
        this.id = id;
        this.deviceId = deviceId;
        this.tid = tid;
        this.mid = mid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public String getTid() {
        return tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Long getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Long deviceIds) {
        this.deviceIds = deviceIds;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public UfsSwitches getSwitchId() {
        return switchId;
    }

    public void setSwitchId(UfsSwitches switchId) {
        this.switchId = switchId;
    }

    public Long getSwitchIds() {
        return switchIds;
    }

    public void setSwitchIds(Long switchIds) {
        this.switchIds = switchIds;
    }

    public UfsCurrency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(UfsCurrency currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(BigDecimal currencyIds) {
        this.currencyIds = currencyIds;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
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
        if (!(object instanceof TmsDeviceTidsMids)) {
            return false;
        }
        TmsDeviceTidsMids other = (TmsDeviceTidsMids) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceTids[ id=" + id + " ]";
    }
    
}
