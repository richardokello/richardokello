package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "UFS_TARIFF_LIMITS")
public class UfsTariffLimits {

    @Id
    @SequenceGenerator(name = "UFS_TARIFF_LIMITS_SEQ", sequenceName = "UFS_TARIFF_LIMITS_SEQ")
    @GeneratedValue(generator = "UFS_TARIFF_LIMITS_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "MINIMUM")
    private Double minimum;

    @ModifiableField
    @Column(name = "MAXIMUM")
    private Double maximum;

    @Filter(isDateRange = true)
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Size(max = 15)
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Filter
    @Size(max = 20)
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 5)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
