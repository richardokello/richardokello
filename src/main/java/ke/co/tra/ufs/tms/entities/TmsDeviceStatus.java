/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_STATUS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceStatus.findAll", query = "SELECT t FROM TmsDeviceStatus t")
    , @NamedQuery(name = "TmsDeviceStatus.findById", query = "SELECT t FROM TmsDeviceStatus t WHERE t.id = :id")
    , @NamedQuery(name = "TmsDeviceStatus.findByDeviceId", query = "SELECT t FROM TmsDeviceStatus t WHERE t.deviceId = :deviceId")
    , @NamedQuery(name = "TmsDeviceStatus.findByDeviceStatus", query = "SELECT t FROM TmsDeviceStatus t WHERE t.deviceStatus = :deviceStatus")
    , @NamedQuery(name = "TmsDeviceStatus.findByOsVersion", query = "SELECT t FROM TmsDeviceStatus t WHERE t.osVersion = :osVersion")
    , @NamedQuery(name = "TmsDeviceStatus.findByAppVersion", query = "SELECT t FROM TmsDeviceStatus t WHERE t.appVersion = :appVersion")
    , @NamedQuery(name = "TmsDeviceStatus.findByBatteryLevel", query = "SELECT t FROM TmsDeviceStatus t WHERE t.batteryLevel = :batteryLevel")
    , @NamedQuery(name = "TmsDeviceStatus.findByChargingStatus", query = "SELECT t FROM TmsDeviceStatus t WHERE t.chargingStatus = :chargingStatus")
    , @NamedQuery(name = "TmsDeviceStatus.findByNetworkingStatus", query = "SELECT t FROM TmsDeviceStatus t WHERE t.networkingStatus = :networkingStatus")
    , @NamedQuery(name = "TmsDeviceStatus.findByLastHeartbeat", query = "SELECT t FROM TmsDeviceStatus t WHERE t.lastHeartbeat = :lastHeartbeat")})
public class TmsDeviceStatus implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SIGNAL_STRENGTH")
    private String signalStrength;
    @Size(max = 20)
    @Column(name = "TM_VERSION")
    private String tmVersion;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    private TmsDevice deviceId;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DEVICE_STATUS")
    private String deviceStatus;
    @Size(max = 20)
    @Column(name = "OS_VERSION")
    private String osVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "APP_VERSION")
    private String appVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "BATTERY_LEVEL")
    private String batteryLevel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CHARGING_STATUS")
    private String chargingStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NETWORKING_STATUS")
    private String networkingStatus;
    @Column(name = "LAST_HEARTBEAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastHeartbeat;

    public TmsDeviceStatus() {
    }

    public TmsDeviceStatus(BigDecimal id) {
        this.id = id;
    }

    public TmsDeviceStatus(BigDecimal id, TmsDevice deviceId, String deviceStatus, String appVersion, String batteryLevel, String chargingStatus, String networkingStatus) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceStatus = deviceStatus;
        this.appVersion = appVersion;
        this.batteryLevel = batteryLevel;
        this.chargingStatus = chargingStatus;
        this.networkingStatus = networkingStatus;
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

    public String getNetworkingStatus() {
        return networkingStatus;
    }

    public void setNetworkingStatus(String networkingStatus) {
        this.networkingStatus = networkingStatus;
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
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
        return "ke.co.tra.ufs.tms.entities.TmsDeviceStatus[ id=" + id + " ]";
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
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
    
}
