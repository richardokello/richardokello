package ke.tra.ufs.webportal.entities.views;

import ke.tra.ufs.webportal.utils.annotations.Filter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "VW_REVENUE_COLLECTED")
public class VwRevenueCollected implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Filter
    @Column(name = "MID")
    private String merchantId;

    @Filter
    @Column(name = "AMOUNT")
    private String amount;

    @Filter
    @Column(name = "TRX_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
    private String transactionDate;
}
