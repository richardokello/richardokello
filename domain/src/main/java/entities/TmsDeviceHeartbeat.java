/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_DEVICE_HEARTBEAT")
@NamedQueries({
    @NamedQuery(name = "TmsDeviceHeartbeat.findAll", query = "SELECT t FROM TmsDeviceHeartbeat t")})
@Data
public class TmsDeviceHeartbeat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ONLINE_ACTIVITY_SEQ")
    @SequenceGenerator(sequenceName = "tms_device_heartbeat_seq", allocationSize = 1, name = "TMS_DEVICE_HEARTBEAT_SEQ")
    @Column(name = "LOG_ID")
    private Long logId;

    @Column(name = "APPLICATION_VERSION")
    private String applicationVersion;
    @Column(name = "BATTERY_PERCENTAGE")
    private String batteryPercentage;
    @Column(name = "CHARGING_STATUS")
    private String chargingStatus;
    @Column(name = "OS_VERSION")
    private String osVersion;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "SIGNAL_STRENGTH")
    private String signalStrength;
    @Column(name = "TM_VERSION")
    private String tmVersion;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "TID")
    private String tid;
    @Column(name = "DEVICE_TEMPERATURE")
    private Short deviceTemperature;
    @Column(name = "OBJ")
    private String obj;

}
