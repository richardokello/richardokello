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
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@Entity(name = "PAR_GLOBAL_MASTER_PROFILE")
public class ParGlobalMasterProfile {

    @Id
    @GenericGenerator(
            name = "PAR_GLOBAL_MASTER_PROFILE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_GLOBAL_MASTER_PROFILE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_GLOBAL_MASTER_PROFILE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "MENU_PROFILE")
    private BigDecimal menuProfileId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "MENU_PROFILE", insertable = false, updatable = false)
    private ParMenuProfile menuProfile;

    @Column(name = "DATE_CREATED", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Searchable
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @OneToMany(mappedBy = "masterProfile")
    private List<ParGlobalMasterChildProfile> childProfiles;

    @Transient
    @ModifiableField
    private List<BigDecimal> childProfileIds;

    @JsonIgnore
    @OneToMany(mappedBy = "masterProfile")
    private List<TmsDevice> devices;
}
