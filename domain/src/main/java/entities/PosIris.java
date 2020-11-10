package entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity()
@Table(name = "TMS_DEVICE_HEARTBEAT")
public class PosIris {
    @Id
    @Column(name = "LOG_ID")
    @GenericGenerator(
            name = "TMS_DEVICE_HEARTBEAT_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_HEARTBEAT_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_DEVICE_HEARTBEAT_SEQ")
    private long id;
    @Column(name = "APPLICATION_VERSION")
    private String appVersion;
    @Column(name = "BATTERY_PERCENTAGE")
    private String batteryLevel;
    @Column(name = "CHARGING_STATUS")
    private String charging;
    @Column(name = "OS_VERSION")
    private String osVersion;
    @Column(name = "SERIAL_NO")
    private String serialNumber;
    @Column(name = "SIGNAL_STRENGTH")
    private String signal;
    @Column(name = "TM_VERSION")
    private String tmVersion;
    @Column(name = "TID")
    private String tid;
    @Column(name = "DEVICE_TEMPERATURE")
    private String temperature;
    @Column(name = "OBJ")
    private String obj;
}

