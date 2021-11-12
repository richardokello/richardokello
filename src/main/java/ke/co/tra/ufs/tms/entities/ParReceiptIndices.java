package ke.co.tra.ufs.tms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@Entity(name = "PAR_RECEIPT_INDICES")
public class ParReceiptIndices implements Serializable, Comparable<ParReceiptIndices> {

    @Id
    @GenericGenerator(
            name = "PAR_RECEIPT_INDICES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PAR_RECEIPT_INDICES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increase_size", value = "1")
            }
    )
    @GeneratedValue(generator = "PAR_RECEIPT_INDICES_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @NotNull
    @Column(name = "RECEIPT_ITEM_ID")
    private BigDecimal receiptItemId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "RECEIPT_ITEM_ID", insertable = false, updatable = false)
    private ParReceiptItems receiptItem;

    @NotNull
    @Column(name = "RECEIPT_INDEX")
    private Integer receiptIndex;

    @NotNull
    @Column(name = "CUSTOMER_TYPE")
    private BigDecimal customerTypeId;

    @JoinColumn(name = "CUSTOMER_TYPE", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne
    private UfsCustomerType customerType;

    @Override
    public int compareTo(ParReceiptIndices parReceiptIndices) {
        return this.receiptItemId.compareTo(parReceiptIndices.receiptItemId);
    }

}
