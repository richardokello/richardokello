package ke.tra.ufs.webportal.entities.views;

import ke.axle.chassis.annotations.Filter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_MERCHANTS")
public class VwMerchants implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Filter
    @Column(name = "MERCHANT_NAME")
    private String merchantName;

    @Filter
    @Column(name = "MID")
    private String merchantId;

    @Column(name = "TID")
    private String terminalId;

    @Column(name = "AMOUNT")
    private String purchaseAmount;

    @Column(name = "REVENUE")
    private String revenueGenerated;

    @Filter
    @Column(name = "LOCATION")
    private String location;

    @Filter
    @Column(name = "STATUS")
    private String status;

    @Column(name = "ONBOARDING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date onBoardingDate;
}
