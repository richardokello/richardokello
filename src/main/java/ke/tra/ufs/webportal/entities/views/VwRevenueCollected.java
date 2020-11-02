package ke.tra.ufs.webportal.entities.views;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
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
    @Searchable
    private String merchantId;

    @Filter
    @Column(name = "AMOUNT")
    @Searchable
    private String amount;

    @Filter(isDateRange = true)
    @Column(name = "TRX_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
    private String transactionDate;
}
