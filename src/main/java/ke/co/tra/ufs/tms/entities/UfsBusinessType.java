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

/**
 * @author Tracom
 */
@Entity
@Data
@Table(name = "UFS_BUSINESS_TYPE")
public class UfsBusinessType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BUSINESS_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BUSINESS_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_BUSINESS_TYPE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 40)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "BUSINESS_TYPE")
    private String businessType;
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
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Filter
    @Searchable
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Searchable
    @Filter
    @Column(name = "INTRASH")
    private String intrash;

}
