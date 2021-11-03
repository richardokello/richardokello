package ke.tra.ufs.webportal.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class CodeGenerator {


    public static String generateOutletCode() {
        // todo generate random no.
        return "OUTLET" + addZeros(3, Integer.parseInt(RandomStringUtils.random(2, false, true)));
    }
    private static String addZeros(int n, int value) {
        // todo determine length of value
        return StringUtils.leftPad(String.valueOf(value), n, "0");
    }


}
