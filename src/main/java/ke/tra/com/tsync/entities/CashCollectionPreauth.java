package ke.tra.com.tsync.entities;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "CASH_COLLECTION_PREAUTH")
public class CashCollectionPreauth  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CASH_COLLECTION_PREAUTH_SEQ")
    @SequenceGenerator(sequenceName = "cash_collection_preauth_seq", allocationSize = 1, name = "CASH_COLLECTION_PREAUTH_SEQ")
    @Column(name = "CCP_ID")
    private BigDecimal ccpID;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "NARRATION")
    private String narration;

    @Column(name = "STARTTIME")
    private Timestamp starttime;

    @Column(name = "ENDTIME")
    private Timestamp endtime;

    @Column(name = "CASHCOLLECTCODE")
    private String cashcollectcode;

    @Column(name = "REVENUEITEMNAME")
    private String revenueItem;

     @Column(name = "DURATIONRATE")
    private   BigDecimal durationRate;

    @Column(name = "DURATIONNAME")
    private  BigDecimal durationName;


    @Column(name = "INSERTTIME")
    private  BigDecimal insertTime;

}
