package ke.tra.ufs.webportal.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "UFS_CUSTOMER_PROFILE")
public class UfsCustomerProfile {

    @Id
    @SequenceGenerator(name = "UFS_CUSTOMER_PROFILE_SEQ", sequenceName = "UFS_CUSTOMER_PROFILE_SEQ")
    @GeneratedValue(generator = "UFS_CUSTOMER_PROFILE_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @Column(name = "CUSTOMER_CLASS")
    private BigInteger customerClass;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_CLASS", referencedColumnName = "ID", insertable = false, updatable = false)
    private UfsCustomerClass ufsCustomerClass;

    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "PROFILE_VALUES")
    private String values;

    @Filter(isDateRange = true)
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Size(max = 15)
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Filter
    @Size(max = 20)
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 5)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
}
