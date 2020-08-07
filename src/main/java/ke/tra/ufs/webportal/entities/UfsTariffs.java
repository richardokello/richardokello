package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.tra.ufs.webportal.entities.enums.TariffType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity(name = "UFS_TARIFFS")
public class UfsTariffs implements Serializable {

    @Id
    @SequenceGenerator(name = "UFS_TARIFFS_SEQ", sequenceName = "UFS_TARIFFS_SEQ")
    @GeneratedValue(generator = "UFS_TARIFFS_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private TariffType type;

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
