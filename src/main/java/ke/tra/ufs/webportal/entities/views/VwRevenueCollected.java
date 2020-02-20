package ke.tra.ufs.webportal.entities.views;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_REVENUE_COLLECTED")
public class VwRevenueCollected implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "MID")
    private String merchantId;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "TRX_DATE")
//    @Temporal(TemporalType.DATE)
    private String transactionDate;
}
