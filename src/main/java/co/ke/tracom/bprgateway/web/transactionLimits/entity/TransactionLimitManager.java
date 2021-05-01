package co.ke.tracom.bprgateway.web.transactionLimits.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BPRTRANSACTIONLIMITS")
@Data
public class TransactionLimitManager {
  @Id private long txnlimitid;

  @Column(name = "LOWERLIMIT")
  private long lowerlimit;

  @Column(name = "UPPERLIMIT")
  private long upperlimit;

  @Column(name = "MTI")
  private String ISOMsgMTI = "";

  @Column(name = "PROCODE")
  private String processingCode = "";

  @Column(name = "TRANSACTIONNAME")
  private String transactionName = "";
}
