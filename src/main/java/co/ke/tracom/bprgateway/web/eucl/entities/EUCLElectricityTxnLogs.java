package co.ke.tracom.bprgateway.web.eucl.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BPR_EUCL_ELECAUDITTRAIL")
public class EUCLElectricityTxnLogs implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String tid;
    private String mid;
    private String posref;
    private String meterno;
    private String customer_name;
    private String token_no;
    private String amount;
    private String gateway_t24postingstatus;
}
