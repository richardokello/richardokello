package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@Entity(name = "UFS_COMMERCIAL_ACTIVITIES")
public class UfsCommercialActivities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_COMMERCIAL_ACTIVITIES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_COMMERCIAL_ACTIVITIES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_COMMERCIAL_ACTIVITIES_SEQ")
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(max = 30)
    @Column(name = "COMMERCIAL_ACTIVITY")
    private String commercialActivity;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 20)
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
