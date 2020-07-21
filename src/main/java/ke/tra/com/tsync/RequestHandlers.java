/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync;

import ke.tra.com.tsync.config.SpringContextBridge;
import ke.tra.com.tsync.entities.TransactionTypes;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import ke.tra.com.tsync.repository.TransTypesRepository;
import ke.tra.com.tsync.services.template.CoreProcessorTemplate;
import org.springframework.beans.factory.annotation.Autowired;

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

            coreProcessor.getTxnTypebyMTIAndProcodeAndActionstatusAndIntrash(
                    mti.trim(),
                    procode.trim(),
                    "Approved",
                    "NO"
            ).ifPresentOrElse(obj -> {
                        field39[0] = "00";

                    }, () -> {
                        field39[0] = "12";
                        field47[0] = "Invalid processing code";
                        logger.error("txn with mti {} and procode {} txnref {}  has not been configured or is not enabled on system yet", errStr[0], errStr[1], errStr[2]);
                    }
            );

            m.set(39, field39[0]);
            if (field39[0] == "12") {
                m.set(47,  "Invalid processing code");

            }


            logger.info(" HERE WE ARE" + m.getString(39));
            // change depending on field used for this data
            if (m.getString(39).equalsIgnoreCase("00"))
                m = coreProcessor.processTransactionsbyMTI(m);
            else
                m.unset(72);
            //set response desc if null
            // all txns start with system error by default ..success to be set on success
            //
            m = coreProcessor.setResponseDescription(m);

            //save to database
        } catch (Exception e) {
            try {
                logger.error(new String(m.pack()), e);
            } catch (ISOException ex) {
                logger.error(String.valueOf(ex));
                //ex.printStackTrace();
            }
            m.set(72, "REMOTE SYSTEM ERROR DURING PROCESSING");
            e.printStackTrace();
            logger.error("GE EXCEPTION rrn {} exception {}", m.getString(37), e);
        }

        coreProcessor.saveOnlineActivity(m);
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
