package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@Entity(name = "PAR_GLOBAL_CONFIG_PROFILE")
public class ParGlobalConfigProfile {

    @Id
    @GenericGenerator(
            name = "PAR_GLOBAL_CONFIG_PROFILE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_GLOBAL_CONFIG_PROFILE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_GLOBAL_CONFIG_PROFILE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @NotNull
    @ModifiableField
    @Column(name = "PARAM_FILE_TYPE")
    private BigDecimal fileTypeId;

    @ManyToOne
    @ModifiableField
    @JoinColumn(name = "PARAM_FILE_TYPE", insertable = false, updatable = false)
    private ParGlobalFileConfigType configType;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;

    @Column(name = "DATE_CREATED", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Size(max = 15)
    @Searchable
    @Filter
    @ModifiableField
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Size(max = 15)
    @Searchable
    @Filter
    @ModifiableField
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 3)
    @ModifiableField
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @JsonIgnore
    @ModifiableField
    @OneToMany(mappedBy = "configProfile")
    private List<ParGlobalMasterChildProfile> masterChildProfiles;
}
