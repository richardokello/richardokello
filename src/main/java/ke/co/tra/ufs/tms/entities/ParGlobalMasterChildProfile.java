package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


@Data
@ToString
@NoArgsConstructor
@Entity(name = "PAR_GLOBAL_MASTER_CHILD")
public class ParGlobalMasterChildProfile {

    @Id
    @GenericGenerator(
            name = "PAR_GLOBAL_MASTER_CHILD_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_GLOBAL_MASTER_CHILD_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_GLOBAL_MASTER_CHILD_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @ModifiableField
    @Column(name = "MASTER_PROFILE")
    private BigDecimal masterProfileId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MASTER_PROFILE", insertable = false, updatable = false)
    private ParGlobalMasterProfile masterProfile;

    @Filter
    @Column(name = "MASTER_PROFILE", insertable = false, updatable = false)
    @ModifiableField
    private String master;

    @NotNull
    @ModifiableField
    @Column(name = "CONFIG_PROFILE")
    private BigDecimal configProfileId;

    @ManyToOne
    @JoinColumn(name = "CONFIG_PROFILE", insertable = false, updatable = false)
    private ParGlobalConfigProfile configProfile;

    @Column(name = "DATE_CREATED", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Searchable
    @Filter
    @ModifiableField
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @ModifiableField
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
