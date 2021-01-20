package ke.co.tra.ufs.tms.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UFS_SWITCHES")
@Data
public class UfsSwitches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_SWITCHES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_SWITCHES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_SWITCHES_SEQ")
    @Column(name = "ID")
    private Long id;

    @Size(max = 30)
    @Filter
    @Searchable
    @ModifiableField
    @Column(name = "SWITCH_NAME")
    private String switchName;

    @Size(max = 30)
    @Filter
    @Searchable
    @ModifiableField
    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 20)
    @Filter
    @Searchable
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 20)
    @Filter
    @Searchable
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Filter
    @Searchable
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
