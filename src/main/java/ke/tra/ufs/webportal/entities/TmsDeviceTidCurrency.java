package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TMS_DEVICE_TIDS_MIDS")
public class TmsDeviceTidCurrency implements Serializable {
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
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID", updatable = false, insertable = false)
    @ManyToOne(optional = false)
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
    @Column(name = "SWITCH")
    @Filter
    @ModifiableField
    private Long switchIds;
    @Column(name = "CURRENCY")
    @Filter
    @ModifiableField
    private BigDecimal currencyIds;

    @Size(max = 3)
    @Filter
    @Searchable
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
