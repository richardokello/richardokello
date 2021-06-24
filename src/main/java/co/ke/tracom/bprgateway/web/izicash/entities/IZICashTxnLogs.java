package co.ke.tracom.bprgateway.web.izicash.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BPR_IZICASHAUDITTRAIL")
public class IZICashTxnLogs implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, precision = 19)
    private Long id;

    @Column(name = "TID")
    private String tid;

    @Column(name = "MID")
    private String mid;

    @Column(name = "POSREF")
    private String posReference;

    @Column(name = "SECRETCODE")
    private String secretCode;

    @Column(name = "PASSCODE")
    private String passCode;

    @Column(name = "RECEIVERNO")
    private String recipientNo;

    @Column(name = "XMLREQ")
    private String xmlRequest;

    @Column(name = "XMLRESPONSE")
    private String xmlResponse;

    @Column(name = "GATEWAY_T24REF")
    private String gatewayT24Reference;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "MODEFIN_HTTPRESCODE")
    private String modeFinHTTPResponseCode;

    @Column(name = "MODEFIN_T24REF")
    private String modeFinT24Reference;

    @Column(name = "MODEFINRES_CODE")
    private String modeFinResponseCode;

    @Column(name = "MODEFIN_TXNDESC")
    private String modeFinTransactionDescription;

    @Column(name = "GATEWAY_T24POSTINGSTATUS")
    private String gatewayT24PostingStatus;

}
