package ke.tra.com.tsync.utils;


import ke.tra.com.tsync.entities.CashCollectionRecon;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

@Component
public class GeneralFuncs {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GeneralFuncs.class.getName());

    @Qualifier("db1EntityManagerFactory")
    @Autowired
    private EntityManager em;

    public String getNextStanNumber() {
        Query q = em.createNativeQuery("SELECT STANSEQUENCE.nextval from DUAL");
        BigDecimal result = (BigDecimal) q.getSingleResult();
        return result.toString();
    }

    // Get Agent Blance
    public String getAgentBlance(String accountnumber) {
        try {
            Query q = em.createNativeQuery("select ACY_AVL_BAL from sttm_cust_account@flex2 where cust_ac_no = '" + accountnumber + "'");
            Object result = q.getSingleResult();
            logger.info("RETURNED BALANCE FOR REF {}   ", result);
            return result.toString();
        } catch (Exception e) {
            logger.error("RETURNED BALANCE FOR REF {}   ", e);
            return "";
        }
    }

    public String logISOMsg(ISOMsg msg, Integer direction) {
        String finalStr = "\n";
        try {
            finalStr += "  MTI : " + msg.getMTI() + "\n";
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    finalStr += "Field-" + i + " : " + msg.getString(i) + "\n";
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
          //  logger.error(e);
        } finally {
            return finalStr;
        }
    }

    public static String exceptionToStr(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionToStr = sw.toString();
        return exceptionToStr;
    }

    //password bycypt
}
