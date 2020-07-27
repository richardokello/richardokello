package ke.tra.ufs.webportal.entities;


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
@Data
@Table(name = "UFS_BUSINESS_DESIGNATIONS")
public class UfsBusinessDesignations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BUSINESS_DESIGNATIONS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BUSINESS_DESIGNATIONS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_BUSINESS_DESIGNATIONS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 40)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "DESIGNATION")
    private String designation;
    @Size(max = 50)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
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
    @Searchable
    @Filter
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
