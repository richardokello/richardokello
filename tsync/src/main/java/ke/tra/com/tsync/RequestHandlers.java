/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync;

import entities.TransactionTypes;
import ke.tra.com.tsync.config.SpringContextBridge;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import ke.tra.com.tsync.services.template.CoreProcessorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import repository.TransTypesRepository;

/**
 * @author / Vincent
 */
public class RequestHandlers implements ISORequestListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestHandlers.class);
    private CoreProcessorTemplate coreProcessor;

    @Autowired
    private TransTypesRepository transTypesRepository;

    /**
     * Receives and manages inbound requests from Inter-switch.
     *
     * @param source
     * @param m
     * @return
     */
    @Override
    public boolean process(ISOSource source, ISOMsg m) {

        String tid = "";
        String mid = "";
        String mti = "";
        String procode = "";
        boolean is_advice = false;


        try {

            coreProcessor = SpringContextBridge.services().getcoreProcessor();

            tid = m.hasField(41) ? m.getString(41) : "";
            mid = m.hasField(42) ? m.getString(42) : "";
            mti = m.hasField(0) ? m.getString(0) : "";
            procode = m.hasField(3) ? m.getString(3) : "";

            //field37 must be present

            if (!m.hasField(37) || m.getString(37).length() < 12)
                m.set(37, new SimpleDateFormat("ddmmyyhhmmss").format(new Date()).toString());
            logger.info("\n~^~^~^^~^~^~^ Server receive request time {}    MTI  :  {}    procode {} ref {} ", new SimpleDateFormat("dd/MM/yy HH:mm:ss SSS Z").format(new Date()).toString(), mti, procode, m.getString(37));
            Optional<TransactionTypes> txntypes;
            final String[] field39 = new String[1];
            final String[] field47 = new String[1];
            String f37 = m.getString(37);
            final String[] errStr = new String[3];
            errStr[0] = mti;
            errStr[1] = procode;
            errStr[2] = f37;
            String IS_ADVICE_MTI = m.getMTI();


            // lets not change field 39 if the transaction is an advice
            switch (IS_ADVICE_MTI){
                case "0220": // 8583
                case "1220": // 1993
                case "0020": //
                    is_advice = true;
                    field39[0] = m.getString(39) == null? AppConstants.POS_INVALID_PROCODE_OR_MTI :m.getString(39);

                    // for cases where switches return 000 for success
                    m.set(39, field39[0]);

                    logger.info("Tsync is being advised hence no need to do anything="+field39[0] + "+");
                    break;
                default:
                    coreProcessor.getTxnTypebyMTIAndProcodeAndActionstatusAndIntrash(
                            mti.trim(),
                            procode.trim(),
                            "Approved",
                            "NO"
                    ).ifPresentOrElse(obj -> {
                                field39[0] = AppConstants.POS_APPROVED;

                            }, () -> {
                                field39[0] = AppConstants.POS_INVALID_PROCODE_OR_MTI;
                                field47[0] = "Invalid processing code and MTI combination";
                                logger.error("txn with mti {} and procode {} txnref {}  has not been configured or is not enabled on system yet", errStr[0], errStr[1], errStr[2]);
                            }
                    );

                    m.set(39, field39[0]);
                    if (field39[0] == AppConstants.POS_INVALID_PROCODE_OR_MTI) {
                        m.set(47,  field47[0]);

                    }

                    logger.info(" HERE WE ARE" + m.getString(39));

                    // change depending on field used for this data
                    // Avoid here if this is an advice BECAUSE IT MIGHT END UP doing unnccesary stuffs


                    if (m.getString(39).equalsIgnoreCase(AppConstants.POS_APPROVED))
                        m = coreProcessor.processTransactionsbyMTI(m);
                    else
                        m.unset(72);

                    m.unset(120);
                    //set response desc if null
                    // all txns start with system error by default ..success to be set on success
                    //
                    m = coreProcessor.setResponseDescription(m);


            }


        } catch (Exception e ) {
            e.printStackTrace();
            try {
                logger.error(new String(m.pack()), e);
            } catch (ISOException ex) {
                logger.error(String.valueOf(ex));
                ex.printStackTrace();
            }
            m.set(39, AppConstants.POS_SERVER_ERROR);
            m.set(72, "REMOTE SYSTEM ERROR DURING PROCESSING");
            m.set(47, "REMOTE SYSTEM ERROR DURING PROCESSING");

            e.printStackTrace();
            logger.error("GE EXCEPTION rrn {} exception {}", m.getString(37), e);
        }

        String IS_USER_MANAGEMENT = m.getString(120)==null?"":m.getString(120);

        // user management
        if(IS_USER_MANAGEMENT.equalsIgnoreCase("UMANAGE")){
            final String temp39 = m.getString(39).strip();
            String field39 = temp39.length()<=2?temp39:temp39.substring(1,3);

            logger.error("GE------------ {}", m.getString(39));
            // makes user management to be saved with codes of length two to
            // differentiate our codes with other codes(advices from other switches)
            // this is useful because the same code we use for user management might mean diff messages from
            // other switches
            ISOMsg message = (ISOMsg) m.clone();
            message.set(39, field39);
            coreProcessor.saveOnlineActivity(message);
        }else if(is_advice){
            // if its an advice we want to first save with the original response code at
            // field 39, then call process advice to change the response code if the whole process
            // went well ie saving device heart beat then setting field 39 to 000
            // this is to avoid sending back as response what we received from pos
            coreProcessor.saveOnlineActivity(m);
            m = coreProcessor.processTransactionAdvice(m);

        }
        else{
            // not user management and not advice
            logger.error("GE--------=++++---- {}", m.getString(39));
            coreProcessor.saveOnlineActivity(m);
        }


        try {
            source.send(coreProcessor.setResponseMTI(m));
        } catch (IOException | ISOException e) {
            logger.error("RESPONSE-send-failed rrn {} exception {}", m.getString(37), e);
            e.printStackTrace();
        }
        logger.info("\n\n ~^~^~^^~^~^~^ Server Response time {}    MTI  :  {}    procode {}  Ref {}  RESPONSE CODE {} \n\n", new SimpleDateFormat("dd/MM/yy HH:mm:ss SSS Z").format(new Date()).toString(), mti, procode, m.getString(37), m.getString(39));
        return true;
    }

}
