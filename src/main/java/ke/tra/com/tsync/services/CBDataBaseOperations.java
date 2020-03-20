package ke.tra.com.tsync.services;


import ke.tra.com.tsync.utils.GeneralFuncs;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CBDataBaseOperations {

    @Autowired
    private GeneralFuncs generalFuncs;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(POSIrisTxns.class);

    public ISOMsg getAgentFloatBalance(ISOMsg isoMsg){
        try{
            if(!isoMsg.hasField(72) || isoMsg.getString(72).length()<1)
                throw new Exception("Field 72 must be present for txn " + isoMsg.getString(72));
            String agenttfloat = generalFuncs.getAgentBlance(isoMsg.getString(72));
            String decAmoutAsAtr = "91.34";
            Float doubleAmt = Float.valueOf(decAmoutAsAtr)*100;
            Integer valx100 = doubleAmt.intValue();
            logger.info(" returned Amount {} : as Float {} : Integer {} ", decAmoutAsAtr, doubleAmt, valx100);
            isoMsg.set(4, ISOUtil.padleft(valx100+"",12,'0'));
            isoMsg.set(39,"00");

        }catch (Exception e){
            e.printStackTrace();
            isoMsg.set(39,"96");
        }

        return isoMsg;
    }

}
