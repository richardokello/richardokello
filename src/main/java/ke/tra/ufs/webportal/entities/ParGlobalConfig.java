package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

// TODO relate to config profile in db
@Data
@NoArgsConstructor
@Entity(name = "PAR_GLOBAL_CONFIGS")
public class ParGlobalConfig implements Serializable {

    @Id
    @GenericGenerator(
            name = "PAR_POS_GLOBAL_CONFIGS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_POS_GLOBAL_CONFIGS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_POS_GLOBAL_CONFIGS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;


    @ModifiableField
    @NotNull
    @Column(name = "PARAM_NAME")
    private String paramName;

    @ModifiableField
    @NotNull
    @Column(name = "PARAM_VALUE")
    private String paramValue;

    @NotNull
    @ModifiableField
    @Column(name = "PROFILE")
    private BigDecimal profileId;

    @Filter
    @Column(name = "PROFILE", insertable = false, updatable = false)
    @ModifiableField
    private String profile;

    @Column(name = "DESCRIPTION")
    @ModifiableField
    private String description;

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

//    @JsonIgnore
//    @OneToOne(mappedBy = "configItem")
//    private ParGlobalConfigIndices indices;
}
