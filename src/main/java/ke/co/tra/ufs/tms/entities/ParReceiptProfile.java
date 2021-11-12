package ke.co.tra.ufs.tms.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PAR_RECEIPT_PROFILE")
public class ParReceiptProfile implements Serializable {

    @Id
    @GenericGenerator(
            name = "PAR_RECEIPT_PROFILE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_RECEIPT_PROFILE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_RECEIPT_PROFILE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Searchable
    @Filter
    @ModifiableField
    @Column(name = "NAME")
    private String name;

    @ModifiableField
    @Searchable
    @Filter
    @Column(name = "DESCRIPTION")
    private String description;

    //An array of [{receiptItemId:receiptPrintCopy}]
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "RECEIPT_VALUES")
    @ModifiableField
    private String receiptValues;

    @Column(name = "CREATION_DATE", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date creationDate;

    @Searchable
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @JsonIgnore
    @OneToMany(mappedBy = "receiptProfile")
    private List<ParGlobalMasterProfile> masterProfile;

    @NotNull
    @Column(name = "CUSTOMER_TYPE")
    private BigDecimal customerTypeId;

    @JoinColumn(name = "CUSTOMER_TYPE", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsCustomerType customerType;
}
