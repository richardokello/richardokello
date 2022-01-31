package co.ke.tracom.bprgateway.web.sendmoney.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BPRMONEYSEND")
public class MoneySend {
	@Id
    @Basic(optional = false)
    @Column(nullable = false, precision = 19)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long moneysendid;
    private String recevernumber;
    private String sendernumber;
    private String mstoken;
    private String cno;
    private String amount;// save pos amount as is..change later
    private String agentid;
    private String channel;
    private String fulfilmentref;//gatewayref
    private String fulfilmentreft24;// t24 ref
    private int fulfilmentstatus; // default 0
    private String sendmoneyref;// gateway initiation ref
    private String fulfilmentchannel; //pos .. web
    private String fulfilmentdate; // ese
    private String fulfilmentagentid; // fulfilment agentid
    @Column(name = "SENDMONEYTOKENSTARTTIME")
    private Long sendmoneytokenstarttime;
    @Column(name = "SENDMONEYTOKENEXPIRETIME")
    private Long sendmoneytokenexpiretime;
    private String sendernationalid;
    private  String typeofid;
    private String sendmoneylegt24ref;

    //added by walter
    //should be saved with other transaction details
    //to be used by the scheduled background service
    @Column(name = "TRANSACTIONRRN")
    private String transactionRRN;
    @Column(name = "SECONDTOKENSTARTTIME")
    private Long sendmoneytokenstarttime2;
    @Column(name = "SECONDTOKENEXPIRYTIME")
    private Long sendmoneytokenexpiretime2;
    @Column(name = "SECONDMTOKEN")
    private String mstoken2;
    @Column(name = "SECONDCNO")
    private String cno2;

}
