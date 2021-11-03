package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import ke.co.tra.ufs.tms.entities.enums.CustomerParamEntities;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "PAR_CUSTOMER_CONFIG_CHILD_KEYS")
public class ParCustomerConfigChildKeys implements Serializable {

    // PAR_CUSTOMER_CONFIG_CHILD_SEQ
    @Id
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "NAME")
    private String name; // readable column name

    @Column(name = "KEY_NAME")
    private String keyName; // the name of a column in table

    @Column(name = "PARENT_CONFIG")
    private BigDecimal parentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PARENT_CONFIG", insertable = false, updatable = false)
    private ParCustomerConfigKeys parent;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ENTITY_NAME")
    private CustomerParamEntities entityName; // should be exact name of the table

    @Column(name = "IS_ALLOWED")
    private short isAllowed;

    @Column(name = "DATE_CREATED", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Size(max = 15)
    @Searchable
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Size(max = 15)
    @Searchable
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @OneToOne(mappedBy = "childConfig")
    private ParCustomerConfigChildKeysIndices childIndex;
}
