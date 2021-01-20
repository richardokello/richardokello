package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import ke.co.tra.ufs.tms.entities.enums.CustomerParamEntities;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

// contains column names and entities --- values in this table will be hard codes
@Data
@NoArgsConstructor
@Entity(name = "PAR_CUSTOMER_CONFIG_KEYS")
public class ParCustomerConfigKeys implements Serializable {

    @Id
    @GenericGenerator(
            name = "PAR_CUSTOMER_CONFIG_KEYS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_CUSTOMER_CONFIG_KEYS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_CUSTOMER_CONFIG_KEYS_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "NAME")
    private String name; // readable column name

    @Column(name = "KEY_NAME")
    private String keyName; // the name of a column in table

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ENTITY_NAME")
    private CustomerParamEntities entityName; // should be exact name of the table

    @Column(name = "IS_ALLOWED")
    private short isAllowed;

    @Column(name = "HAS_CHILD")
    private short hasChild;

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

    @OneToMany(mappedBy = "parent")
    private List<ParCustomerConfigChildKeys> childKeys;

    @JsonIgnore
    @OneToOne(mappedBy = "config")
    private ParCustomerConfigKeysIndices config;
}
