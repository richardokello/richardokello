package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import ke.tra.ufs.webportal.entities.enums.ParameterFileType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "PAR_GLOBAL_CONFIG_FILE_TYPES")
public class ParGlobalFileConfigType implements Serializable {

    @Id
    @GenericGenerator(
            name = "PAR_POS_GLOBAL_TYPES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_POS_GLOBAL_TYPES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_POS_GLOBAL_TYPES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Searchable
    @ModifiableField
    @NotNull
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    @ModifiableField
    private ParameterFileType type;

    @Filter
    @Column(name = "TYPE", insertable = false, updatable = false)
    @ModifiableField
    private String fileType;

    @ModifiableField
    @NotNull
    @Column(name = "FILE_NAME")
    private String fileName;

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

    @Size(max = 3)
    @Filter
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @JsonIgnore
    @OneToMany(mappedBy = "configType")
    List<ParGlobalConfigFormValues> configValues;

    @JsonIgnore
    @OneToMany(mappedBy = "configType")
    List<ParGlobalConfigProfile> profiles;
}
