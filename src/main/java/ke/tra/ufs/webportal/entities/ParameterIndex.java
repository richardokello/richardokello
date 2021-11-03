package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.tra.ufs.webportal.entities.enums.ParameterCategory;
import ke.tra.ufs.webportal.entities.enums.ParameterType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@Entity(name = "PARAMETER_INDICES")
public class ParameterIndex implements Serializable {

    @Id
    @GenericGenerator(
            name = "PAR_MENU_INDEX_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_MENU_INDEX_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_MENU_INDEX_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Column(name = "ITEM_ID")
    private BigDecimal itemId;

    @NotNull
    @Column(name = "PARAMETER_INDEX")
    private Integer index;

    // stores the type - relates to a specific table in the db

    @NotNull
    @Column(name = "CATEGORY")
    @Enumerated(value = EnumType.STRING)
    private ParameterCategory category;


    // stores the type of file where parameter shall be put to when genereating files
    @NotNull
    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private ParameterType type;

    @Column(name = "DATE_CREATED", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    @Filter
    private String action;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;

}
