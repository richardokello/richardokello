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
@Entity(name = "PAR_CONFIG_INDICES")
public class ParGlobalConfigIndices implements Serializable, Comparable<ParGlobalConfigIndices> {
    @Id
    @GenericGenerator(
            name = "PAR_CONFIG_INDICES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_CONFIG_INDICES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_CONFIG_INDICES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Column(name = "CONFIG_ITEM")
    private String configItem;

//    @OneToOne
//    @JoinColumn(name = "CONFIG_ITEM", updatable = false, insertable = false)
//    private ParGlobalConfig configItem;

    @NotNull
    @Column(name = "CONFIG_INDEX")
    private Integer configIndex;

    @NotNull
    @Column(name = "CONFIG_TYPE")
    private BigDecimal configType;

    @Column(name = "DATE_CREATED", updatable = false, insertable = false)
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
    public int compareTo(ParGlobalConfigIndices parGlobalConfigIndices) {
        return this.configItem.compareTo(parGlobalConfigIndices.getConfigItem());
    }
}
