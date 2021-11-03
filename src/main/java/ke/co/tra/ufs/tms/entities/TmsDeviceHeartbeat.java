/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import ke.axle.chassis.annotations.Searchable;
import ke.co.tra.ufs.tms.utils.annotations.ExportField;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_HEARTBEAT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceHeartbeat.findAll", query = "SELECT t FROM TmsDeviceHeartbeat t")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByLogId", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.logId = :logId")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByApplicationVersion", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.applicationVersion = :applicationVersion")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByBatteryPercentage", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.batteryPercentage = :batteryPercentage")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByChargingStatus", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.chargingStatus = :chargingStatus")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByOsVersion", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.osVersion = :osVersion")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findBySerialNo", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.serialNo = :serialNo")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findBySignalStrength", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.signalStrength = :signalStrength")
    , @NamedQuery(name = "TmsDeviceHeartbeat.findByTmVersion", query = "SELECT t FROM TmsDeviceHeartbeat t WHERE t.tmVersion = :tmVersion")})
public class TmsDeviceHeartbeat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_ID") 
    @ExportField(name="ID")
    private BigDecimal logId;
    @Size(max = 20)
    @Column(name = "APPLICATION_VERSION")
    private String applicationVersion;
    @Size(max = 20)
    @Column(name = "BATTERY_PERCENTAGE")
    @ExportField(name = "Battery Percentage")
    private String batteryPercentage;
    @Size(max = 20)
    @Column(name = "CHARGING_STATUS")
    @ExportField(name="Charging Status")
    private String chargingStatus;
    @Size(max = 20)
    @Column(name = "OS_VERSION")
    @ExportField(name="Os version")
    private String osVersion;
    @Size(max = 50)
    @Column(name = "SERIAL_NO")
    @Searchable
    @ExportField(name="Serial no")
    private String serialNo;
    @Size(max = 20)
    @Column(name = "SIGNAL_STRENGTH")
    @ExportField(name="Signal Strength")
    private String signalStrength;
    @Size(max = 20)
    @Column(name = "TM_VERSION")
    @ExportField(name="Tm Version")
    private String tmVersion;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)    
    @ExportField(name="Creation date")
    private Date creationDate;

    @Column(name = "TID")
    private String tid;

    @Column(name = "OBJ")
    private String obj;

    @Column(name = "DEVICE_TEMPERATURE")
    private Integer deviceTemperature;

    public TmsDeviceHeartbeat() {
    }

    public TmsDeviceHeartbeat(BigDecimal logId) {
        this.logId = logId;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(String batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public String getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(String chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public Integer getDeviceTemperature() {
        return deviceTemperature;
    }

    public void setDeviceTemperature(Integer deviceTemperature) {
        this.deviceTemperature = deviceTemperature;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsDeviceHeartbeat)) {
            return false;
        }
        TmsDeviceHeartbeat other = (TmsDeviceHeartbeat) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceHeartbeat[ logId=" + logId + " ]";
    }
    
}
