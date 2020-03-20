/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TMS_DEVICE_STATUS")
@NamedQueries({
    @NamedQuery(name = "TmsDeviceStatus.findAll", query = "SELECT t FROM TmsDeviceStatus t")})
public class TmsDeviceStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "DEVICE_STATUS")
    private String deviceStatus;
    @Column(name = "OS_VERSION")
    private String osVersion;
    @Basic(optional = false)
    @Column(name = "APP_VERSION")
    private String appVersion;
    @Basic(optional = false)
    @Column(name = "BATTERY_LEVEL")
    private String batteryLevel;
    @Basic(optional = false)
    @Column(name = "CHARGING_STATUS")
    private String chargingStatus;
    @Basic(optional = false)
    @Column(name = "SIGNAL_STRENGTH")
    private String signalStrength;
    @Column(name = "LAST_HEARTBEAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastHeartbeat;
    @Column(name = "TM_VERSION")
    private String tmVersion;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;

    public TmsDeviceStatus() {
    }

    public TmsDeviceStatus(BigDecimal id) {
        this.id = id;
    }

    public TmsDeviceStatus(BigDecimal id, String deviceStatus, String appVersion, String batteryLevel, String chargingStatus, String signalStrength) {
        this.id = id;
        this.deviceStatus = deviceStatus;
        this.appVersion = appVersion;
        this.batteryLevel = batteryLevel;
        this.chargingStatus = chargingStatus;
        this.signalStrength = signalStrength;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(String chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getTmVersion() {
        return tmVersion;
    }

    public void setTmVersion(String tmVersion) {
        this.tmVersion = tmVersion;
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
        if (!(object instanceof TmsDeviceStatus)) {
            return false;
        }
        TmsDeviceStatus other = (TmsDeviceStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsDeviceStatus[ id=" + id + " ]";
    }
    
}
