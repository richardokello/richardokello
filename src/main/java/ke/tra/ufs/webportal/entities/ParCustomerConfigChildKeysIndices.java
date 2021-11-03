package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity(name = "PAR_CUSTOMER_CON_CHILD_INDICES")
public class ParCustomerConfigChildKeysIndices implements Comparable<ParCustomerConfigChildKeysIndices> {

    @Id
    @GenericGenerator(
            name = "PAR_CUSTOMER_CON_CHILD_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_CUSTOMER_CON_CHILD_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_CUSTOMER_CON_CHILD_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "CHILD_CONFIG")
    private BigDecimal childConfigId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "CHILD_CONFIG", insertable = false, updatable = false)
    private ParCustomerConfigChildKeys childConfig;

    @Column(name = "CONFIG_INDEX")
    private Integer configIndex;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @Override
    public int compareTo(ParCustomerConfigChildKeysIndices parCustomerConfigChildKeysIndices) {
        return childConfigId.compareTo(parCustomerConfigChildKeysIndices.getChildConfigId());
    }
}
