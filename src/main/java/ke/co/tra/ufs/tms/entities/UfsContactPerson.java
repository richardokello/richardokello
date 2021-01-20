package ke.co.tra.ufs.tms.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tracom
 */
@Entity
@Data
@Table(name = "UFS_CONTACT_PERSON")
public class UfsContactPerson implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_CONTACT_PERSON_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CONTACT_PERSON_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CONTACT_PERSON_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 20)
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "POS_ROLE")
    @Filter
    @ModifiableField
    private String posRole;
    @JoinColumn(name = "OUTLET_ID",referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerOutlet customerOutlet;
    @Column(name = "OUTLET_ID")
    @ModifiableField
    @Filter
    private Long customerOutletId;
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
    @Column(name = "USER_NAME")
    @Filter
    @ModifiableField
    private String userName;

}
