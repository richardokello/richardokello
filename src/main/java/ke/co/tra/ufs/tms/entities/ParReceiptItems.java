package ke.co.tra.ufs.tms.entities;


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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PAR_RECEIPT_ITEMS")
public class ParReceiptItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "PAR_RECEIPT_ITEM_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_RECEIPT_ITEM_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_RECEIPT_ITEM_SEQ")
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

    @Filter
    @Column(name = "CUSTOMER_TYPE", updatable = false, insertable = false)
    private String type;

    @JoinColumn(name = "CUSTOMER_TYPE", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsCustomerType customerType;

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
}
