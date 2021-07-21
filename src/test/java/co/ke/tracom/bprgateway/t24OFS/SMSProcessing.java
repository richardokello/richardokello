package co.ke.tracom.bprgateway.t24OFS;

import org.junit.Test;

public class SMSProcessing {
    @Test
    public void process_failed_SMS_Response() {
        String smsResults = "0081,Y.STATUS::STATUS/Y.REASON::REJECT.REASON,\"FAILED\" \"The access token has expired\"";
        String[] T24MessageBodyArray = smsResults.split(",");
        if (T24MessageBodyArray.length == 3) {
            String[] split = T24MessageBodyArray[2].split("\"");

            if (split[1].equalsIgnoreCase("FAILED")) {
                System.out.println("Processing status = " + split[1]);
            }
            if (split[1].equalsIgnoreCase("SUCCESS")) {
                System.out.println("Processing status = " + split[1]);
            }

            if (!split[3].isEmpty()) {
                System.out.println(split[3]);
            }
        }
    }
}
