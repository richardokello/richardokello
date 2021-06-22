package co.ke.tracom.bprgateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class CurrencyFormatter {

    @Test
    public void formatDecimal() {
        long money = 30709880;
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(money);
        Assertions.assertEquals("30,709,880", format);
    }
}
