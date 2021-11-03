package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.co.tra.ufs.tms.utils.annotations.ModifiableField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Kenny
 */
@Entity
@Data
@Table(name = "TID_MID_CURRENCY")
public class TidMidCurrency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "TID_MID_CURRENCY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TID_MID_CURRENCY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TID_MID_CURRENCY_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "CURRENCY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    @JsonIgnore
    private UfsCurrency currencyId;
    @Column(name = "CURRENCY")
    @Filter
    @ModifiableField
    private BigDecimal currencyIds;
    @JoinColumn(name = "TID_MID_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    @JsonIgnore
    private TmsDeviceTidsMids tidMidId;
    @Column(name = "TID_MID_ID")
    @Filter
    @ModifiableField
    private Long tidMidIds;
}
