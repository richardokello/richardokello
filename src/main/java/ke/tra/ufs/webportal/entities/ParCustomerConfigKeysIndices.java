package ke.tra.ufs.webportal.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@Entity(name = "PAR_CUSTOMER_CONFIG_INDICES")
public class ParCustomerConfigKeysIndices implements Comparable<ParCustomerConfigKeysIndices> {

    @Id
    @GenericGenerator(
            name = "PAR_CUSTOMER_INDICES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_CUSTOMER_INDICES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_CUSTOMER_INDICES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "CONFIG")
    private BigDecimal configId;

    @OneToOne()
    @JoinColumn(name = "CONFIG", insertable = false, updatable = false)
    private ParCustomerConfigKeys config;

    @Column(name = "CONFIG_INDEX")
    private Integer configIndex;

    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @Override
    public int compareTo(ParCustomerConfigKeysIndices parCustomerConfigKeysIndices) {
        return configId.compareTo(parCustomerConfigKeysIndices.configId);
    }
}
