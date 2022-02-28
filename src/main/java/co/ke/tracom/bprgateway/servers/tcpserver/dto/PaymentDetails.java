package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {


        private String debitAccount;
        private String creditAccount;
        private String senderName;
        private String mobileNumber;
        private double amount;
        private int schoolId;
        //private List<TransactionData> data = new ArrayList<>();
        private String schoolName;
        private String svcCode;
        private String studentName;
        private String billNumber;

}
