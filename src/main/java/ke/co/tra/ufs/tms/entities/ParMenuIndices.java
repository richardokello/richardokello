package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.Filter;
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
@Entity(name = "PAR_MENU_INDICES")
public class ParMenuIndices implements Serializable, Comparable<ParMenuIndices> {

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
    @Column(name = "MENU_ITEM")
    private BigDecimal menuItem;

    @NotNull
    @Column(name = "MENU_INDEX")
    private Integer menuIndex;

    @NotNull
    @Column(name = "CUSTOMER_TYPE")
    private BigDecimal customerType;

    @Column(name = "DATE_CREATED", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION", insertable = false)
    @Filter
    private String action;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @Override
    public int compareTo(ParMenuIndices parMenuIndices) {
        return this.menuItem.compareTo(parMenuIndices.menuItem);
    }
}
