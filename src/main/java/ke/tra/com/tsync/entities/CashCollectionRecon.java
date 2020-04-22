package ke.tra.com.tsync.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "CASH_COLLECTION_RECON")
public class CashCollectionRecon  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASH_COLLECTION_RECON_SEQ")
    @SequenceGenerator(sequenceName = "cash_collection_recon_seq", allocationSize = 1, name = "CASH_COLLECTION_RECON_SEQ")
    @Column(name = "CCID")
    private BigDecimal ccid;



    @Column(name = "CBS_POSTINGSTATUS")
    String cbsPostingStatus;

    @Column(name = "CBS_POSTINGREFERENCE")
    String cbsPostingReference;

    @Column(name = "AGENT_USERNAME")
    String agentUsername;

    @Column(name = "DEBIT_ACCOUNT")
    String debitAccount;

    @Column(name = "CREDIT_ACCOUNT")
    String creditAccount;

    @Column(name = "AMOUNT")
    String amount;

    @Column(name = "PAYMENTNARRATION")
    String paymentNarration;
    @Column(name = "ZONENAME")
    String zoneName;

    @Column(name = "REVENUEITEMNAME")
    String revenueItemName;
    @Column(name = "PAYMENTMODE")
    String paymentMode;
    @Column(name = "PAYMENTACCOUNT")
    String paymentAccount;

    @Column(name = "RATEPAYER_CATNAME")
    String RatePayerCategoryName;

    @Column(name = "TID")
    String tid;

    @Column(name = "MID")
    String mid;

    @Column(name = "POS_REFERENCE")
    String posReference;

    @Column(name = "OTHERDATA")
    String otherData;

    @Column(name ="PAYMENT_DURATION")
    String paymentDuration;

    @Column(name = "INSERTTIME")
    Timestamp insertTime;



}
