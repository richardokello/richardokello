package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.EditChildEntity;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "PAR_CONFIG_FORM_VALUES")
@EditChildEntity
public class ParGlobalConfigFormValues {

    @Id
    @GenericGenerator(
            name = "PAR_CONFIG_FORM_VALUES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_CONFIG_FORM_VALUES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_CONFIG_FORM_VALUES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Column(name = "TYPE")
    private BigDecimal typeId;

    @ManyToOne
    @Filter
    @JoinColumn(name = "TYPE", insertable = false, updatable = false)
    private ParGlobalFileConfigType configType;

    /*@Filter
    @Column(name = "TYPE", insertable = false, updatable = false)
    private String type;*/

    @ModifiableField
    @Column(name = "FORM_VALUES")
    private String formValues;

    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_CREATED", insertable = false, updatable = false)
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
}
