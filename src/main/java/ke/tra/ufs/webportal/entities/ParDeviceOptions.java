package ke.tra.ufs.webportal.entities;

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
@Entity(name = "PAR_DEVICE_OPTIONS")
public class ParDeviceOptions {
//
    @Id
    @GenericGenerator(
            name = "PAR_DEVICE_OPTIONS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_DEVICE_OPTIONS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_DEVICE_OPTIONS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @ModifiableField
    @NotNull
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "IS_ALLOWED")
    private Short isAllowed;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
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
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @OneToMany(mappedBy = "deviceOptions")
    private List<ParDeviceSelectedOptions> selectedOptions;

    @JsonIgnore
    @OneToOne(mappedBy = "option")
    private ParDeviceOptionsIndices optionsIndex;
}
