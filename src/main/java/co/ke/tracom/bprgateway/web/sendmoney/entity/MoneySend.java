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
    private Long sendmoneytokenstarttime;
    private Long sendmoneytokenexpiretime;
    private String sendernationalid;
    private  String typeofid;
    private String sendmoneylegt24ref; 

}
