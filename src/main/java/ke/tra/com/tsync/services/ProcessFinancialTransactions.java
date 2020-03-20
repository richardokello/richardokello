/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;


import ke.tra.com.tsync.services.template.FinancialTransactionsTmpl;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Tracom
 */
@Service
public class ProcessFinancialTransactions implements FinancialTransactionsTmpl {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProcessFinancialTransactions.class);

/*
00	Goods and Services (Debit)
01	Cash (Debit)
02	Adjustment (Debit)
03	Check Guarantee (Debit)
04	Check Verification (Debit)
05	Eurocheque (Debit)
06	Travelers Check (Debit)
07	Letter of Credit (Debit)
08	Giro (Postal Banking) (Debit)
09	Goods and Services with Cash Disbursement (Debit)
0A	Phone Top-Up (Private)
0B	Fee Collection (Debit)
10	Non-cash Financial Instrument (Debit)
11	Quasi-cash and Scrip (Debit)
17	Fast Cash (Debit)
18	Private Use (Debit)
19	Private Use (Debit)
20	Return (Credit)
21	Deposit (Credit)
22	Adjustment (Credit)
23	Check Deposit Guarantee (Credit)
24	Check Deposit (Credit)
28	Deposit with Cash Back (Credit)
29	Check Deposit with Cash Back (Credit)
2A	Funds Dispursement
2A	Funds Dispursement
2B	Prepaid Load (Credit)
2C	Original Credits
2D	Cash Deposit with Cash Back
2E	Cash Deposit
2F	Split Deposit
30	Inquiry – Available Funds Inquiry
31	Inquiry – Balance Inquiry
38	Card Verification
39	Statement Print (inbound/outbound)
3A	Mini-Statement Inquiry Check Clear (inbound/outbound)
3B	Mini-Statement Inquiry Last Debit/Credit (inbound/outbound)
3C	Mini-Statement Inquiry Last Source (inbound/outbound)
3D	Mini-Statement Inquiry Last Check (inbound/outbound)
3E	Mini-Statement Inquiry Last Debit (inbound/outbound)
3F	Mini-Statement Inquiry Last Credit (inbound/outbound)
3G	Mini-Statement Inquiry Last Transfer (inbound/outbound)
3H	Inquiry – Customer Vendor
3J	Inquiry – Scheduled Payment
3J	Inquiry – Scheduled Payment
3K	Inquiry – Scheduled Transfer
3L	Inquiry – Last Payment and Transfer
3M	Inquiry – Scheduled Transaction
3N	Inquiry – Account List
40	Transfer – Cardholder Accounts Transfer
48	Transfer – Private Use
49	Transfer – Private Use
4A	Transfer – Future
4B	Transfer – Recurring
50	Payment (can include both a from and to account type)
56	Payment to (only a to account is present)
58	Payment Enclosed
59	Payment – Private Use
5A	Payment – Payment Future
5B	Payment – Recurring
5C	Bulk Authorization
5D	Return Payment
5E	Scheme Return Payment
5F	Corporate Dated Payment
5G	Payment To Third Party
5H	Payment From Third Party
90	PIN Change
91	PIN Verify
 */

    public ISOMsg switchByProcode(ISOMsg isoMsg) {
        switch (isoMsg.getString(3)) {
            case "0100000":
                break;

            default:
                break;
        }

        return isoMsg;
    }


    // @Autowired
    // PostOfflineCBS postOfflineCBS;
    // @Autowired
    // AccStatementsRepositoryImpl accStatementsRepositoryImpl;
    /*
    public ISOMsg processDepositToAccount(ISOMsg isomsg, String agentfloatacc, String agentcommacc, String decimalplaces) {
        String f4amount = isomsg.getString(4);
        int txnamount = 00;
        String txnref = isomsg.getString(37);
        System.out.println(" \n \n FORMATTED F4 AS IN " + txnamount);
        if (isomsg.hasField(41) && isomsg.hasField(4) && isomsg.hasField(42) && isomsg.hasField(37)) {
            // String customeracc = ""
            String custaccount = isomsg.getString(60).split("#")[0];
            //Get Transaction charges
           // ScCustomerlimitsCommTax sct = new ScCustomerlimitsCommTax();
            try {
               // sct = postOfflineCBS.getTxnChargesByProcodeMTI(isomsg);
            } catch (NoSuchElementException e) {
                // Logger.getLogger(CardLess.class.getName()).log(Level.SEVERE, null, e);
                isomsg.set(39, "46");
                isomsg.set(60, "CDLSS ACC NO CONFIGURATION ERROR");
                return isomsg;
            }catch (UnsupportedOperationException ex) {
               // ex.printStackTrace();
               // Logger.getLogger(CardLess.class.getName()).log(Level.SEVERE, null, ex);
                isomsg.set(39, "46");
                isomsg.set(60, "CDLSS ACC NO ECONFIGURATION ERROR");
                return isomsg;
            }
            //int commission = sct.getCommission().intValue();
            int commission = 0;
            String debitacc = agentfloatacc;
            String creditacc = custaccount;
            // Push the transaction for entry in database
            System.out.println("************* post deposit ToOfflineCBS START *************");
            System.out.println("txnref : " + txnref);
            //  System.out.println("sct.getTrantypeId().getId().intValue() : " + sct.getTrantypeId().getId().intValue());
            System.out.println("Deposit to Acc Actuel : " + txnamount);
            System.out.println("commission : " + commission);
            // System.out.println("sct.getTaxrate().intValue() * commission / 100 : " + sct.getTaxrate().intValue() * commission / 100);
            System.out.println("debitacc : " + debitacc);
            System.out.println("agentcommacc : " + agentcommacc);
            System.out.println("credicaccount : " + creditacc);
            System.out.println("************ postToOfflineCBS END  *******************");
            return isomsg;
        } else {
            isomsg.set(39, "30");
            isomsg.set(60, "TID MID DETAILS MISSING ");
        }
        return isomsg;
    }
*/

}
