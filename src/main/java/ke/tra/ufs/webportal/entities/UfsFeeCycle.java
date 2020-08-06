package ke.tra.ufs.webportal.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.tra.ufs.webportal.entities.enums.FeeCycleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "UFS_FEE_CYCLE")
public class UfsFeeCycle implements Serializable {

    @Id
    @SequenceGenerator(name = "UFS_FEE_CYCLE_SEQ", sequenceName = "UFS_FEE_CYCLE_SEQ")
    @GeneratedValue(generator = "UFS_FEE_CYCLE_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @ModifiableField
    @Column(name = "FEE_CYCLE_TYPE")
    private FeeCycleType cycle;

    @ModifiableField
    @Column(name = "TIMES")
    private Long times;

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

    @Transient
    public Long getPeriod() {
        return times * cycle.getHours();
    }
}
