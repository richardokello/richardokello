package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.tra.ufs.webportal.entities.enums.TariffType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "UFS_TARIFF_FEE")
public class UfsTariffFee {

    @Id
    @SequenceGenerator(name = "UFS_TARIFF_FEE_SEQ", sequenceName = "UFS_TARIFF_FEE_SEQ")
    @GeneratedValue(generator = "UFS_TARIFF_FEE_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @Column(name = "FEE_CYCLE")
    private BigInteger cycleId;

    @ManyToOne
    @JoinColumn(name = "FEE_CYCLE", referencedColumnName = "ID", insertable = false, updatable = false)
    private UfsTariffFeeCycle cycle;

    @Enumerated(value = EnumType.STRING)
    @ModifiableField
    @Column(name = "TARIFF_TYPE")
    private TariffType tariffType;

    @ModifiableField
    @Column(name = "TARIFF_VALUES")
    private String values;

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
