package co.ke.tracom.bprgateway.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    @Test
    public void printPOSDate(){
        SimpleDateFormat yyyyMMDDhhmmss = new SimpleDateFormat("yyyyMMddhhmmss");
    System.out.println("yyyyMMddhhmmss = " + yyyyMMDDhhmmss.format(new Date()));
    }

    @Test
    public void sanitizePaymentDetails() {
        String paymentdetail1="BPR{Payment*Gateway";
        String DefaultValue="Mkenya Daima";
        String paymentDetail = paymentdetail1.isEmpty() ? DefaultValue : paymentdetail1.trim();
        String replace = paymentDetail.replaceAll("\\P{Alnum}", "");
    System.out.println("replace = " + replace);
    }

}
