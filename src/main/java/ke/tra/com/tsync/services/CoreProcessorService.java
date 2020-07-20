/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import ke.tra.com.tsync.entities.CashCollectionRecon;
import ke.tra.com.tsync.entities.FailedTransactions;
import ke.tra.com.tsync.entities.OnlineActivity;
import ke.tra.com.tsync.entities.TransactionTypes;
import ke.tra.com.tsync.repository.*;
import ke.tra.com.tsync.services.template.CoreProcessorTemplate;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.hibernate.exception.JDBCConnectionException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vincent
 */
@Service("coreProcessorTemplate")
public class CoreProcessorService implements CoreProcessorTemplate {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CoreProcessorService.class);

    @Autowired
    private TransationResponseMaps transationResponseMaps;
    @Autowired
    private TransTypesRepository transTypesRepository;
    @Autowired
    private OnlineActivityRepository onlineActivityRepository;

    @Autowired
    private ISOMsgTOEntityImp isoMsgTOEntity;
    @Autowired
    private FailedTransactionRepository failedTransactionRepository;
    @Autowired
    private ProcessAuthorizationServices processAuthorizationServices;
    @Autowired
    private ForwardToWay4Switch forwardToWay4Switch;


    @Autowired private  CashCollectionReconSvc cashCollectionReconSvc;



    @Override
    public ISOMsg setResponseMTI(ISOMsg isoMsg) {
        try {
            if (!isoMsg.isResponse())
                isoMsg.setResponseMTI();
        } catch (ISOException e) {
            //is a response already
            e.printStackTrace();
        }

        return isoMsg;
    }

    @Override
    public ISOMsg setResponseDescription(ISOMsg isoMsg) {
        if (!isoMsg.hasField(72)) {
            final String responseDesc[] = new String[1];
            System.out.println("________________++++++++++++++++++++"+isoMsg.getString(39));
            transationResponseMaps.findFirstByCodeEquals(isoMsg.getString(39)).ifPresentOrElse(
                    response -> responseDesc[0] = response.getDescription(),
                    () -> responseDesc[0] = "SYSTEM ERROR"
            );
            isoMsg.set(72, responseDesc[0]);
        } else {
            LOGGER.info("RESPONSE DESC : " + isoMsg.getString(72));
        }
        return isoMsg;
    }

    @Override
    public PosUserWrapper terminalDataTLVMap(String fielddata) {

        PosUserWrapper wrapper = new PosUserWrapper();
        int skip = 0;
        String Tag = null;
        String TagLength = null;
        String TagValue = null;
        System.out.println("++++++++++++ HERE");
        boolean more = true;

        // Expecting these tags
        // could have convertted the tlv to hashmap but its much easier to
        // use names that show what we expect in the TLV

        if (!fielddata.isBlank()) {
            //  String field047 = isoMsg.getString(47).trim();
            int i = 0;
            while (more) {
                try {
                    Tag = fielddata.substring(skip, skip + 3);
                    TagLength = fielddata.substring(skip + 3, skip + 6);
                    if (Integer.valueOf(TagLength) > 0) {
                        TagValue = fielddata.substring(skip + 6, skip + 6 + Integer.valueOf(TagLength));
                    } else {
                        TagValue = null;
                    }

                    switch (Integer.valueOf(Tag)) {
                        case 25:
                            wrapper.setAppVersion(TagValue);
                            System.out.println("++++++++++++ setAppVersion "+TagValue);
                            break;
                        case 26:
                            wrapper.setSerialNumber(TagValue);
                            System.out.println("+++++++++++++ setSerialNumber "+TagValue);
                            break;

                        case 29:
                            wrapper.setGender(Integer.valueOf(TagValue));
                            System.out.println("+++++++++++++ ssetGenderr " +TagValue);
                            break;

                        case 30:
                            wrapper.setUsername(TagValue);
                            System.out.println("+++++++++++++ setUsername " +TagValue);
                            break;

                        case 31:
                            wrapper.setFullName(TagValue);
                            System.out.println("+++++++++++++ setFullName "+TagValue);
                            break;

                        case 32:
                            wrapper.setEmail(TagValue);
                            break;

                        case 33:
                            wrapper.setPhoneNumber(TagValue);
                            System.out.println("+++++++++++++ setPhoneNumber "+TagValue);
                            break;

                        case 34:
                            wrapper.setIdNumber(TagValue);
                            System.out.println("+++++++++++++ setIdNumber "+TagValue);
                            break;

                        case 35:
                            wrapper.setUserAccessCode(TagValue);
                            break;

                        case 36:
                            wrapper.setPin(TagValue); // New pin|user pin
                            System.out.println("+++++++++++++ setPin "+TagValue);
                            break;

                        case 37:
                            wrapper.setUfsWorkgroup(TagValue);
                            System.out.println("+++++++++++++ setUfsWorkgroup "+TagValue);
                            break;
                        case 40:
                            wrapper.setConfirmPin(TagValue); //  confirm
                            System.out.println("confirm Pin "+ TagValue);
                        case 41:
                            wrapper.setCurrentPin(TagValue); //  confirm
                            System.out.println("Current Pin "+ TagValue); // pin that is being changed
                        default:
                            break;
                    }
                    i++;
                    skip = skip + Integer.valueOf(TagLength) + 6;
                    if (((skip) == fielddata.length()) || (i > 50)) {
                        more = false;
                    }
                } catch (NumberFormatException e) {
                    break;
                } catch(StringIndexOutOfBoundsException e){
                    e.printStackTrace();
                    break;

                }
            }
        }

        return wrapper;
    }

    @Override
    public void saveOnlineActivity(ISOMsg isoMsg) {
        LOGGER.info("~~~~~~~~~ saveOnlineActivity ", "saving " + isoMsg.getString(37));
        try {
            if (!isoMsg.getString(39).equalsIgnoreCase("00")) {
                FailedTransactions failedTransactions = isoMsgTOEntity.convertFailedIsoToPojo(isoMsg);
                failedTransactionRepository.save(failedTransactions);
            } else {
                OnlineActivity onlineActivity = (OnlineActivity) isoMsgTOEntity.convertIsoToPojo(isoMsg);
                onlineActivityRepository.save(onlineActivity);
            }
        } catch (Exception e) {
            LOGGER.error("saveOnlineActivity for isomsg %s ", isoMsg.toString(), e);
            e.printStackTrace();
        }
    }


    @Override
    public ISOMsg processTransactionsbyMTI(ISOMsg isoMsg) throws ISOException, JDBCConnectionException {
        LOGGER.info(" processTransactionsbyMTI : " + isoMsg.getString(3));
        PosUserWrapper fieldData = new PosUserWrapper();


        System.out.println(fieldData);
       if (isoMsg.hasField(47)) {
            fieldData = terminalDataTLVMap(isoMsg.getString(47));
            //process the transaction
            LOGGER.info(" fieldDataMap  : " + fieldData);
        }
        fieldData.setMID(isoMsg.getString(42)==null?"":isoMsg.getString(42));
        fieldData.setTID(isoMsg.getString(41)==null?"":isoMsg.getString(41));
        isoMsg.set(39, "06");
        System.out.println("++++++++++++++++++++_____________ "+fieldData);
        switch (isoMsg.getString(0)) {
            case "0100":
            case "1100":
                LOGGER.info(" STEP INQUIRY  : " + isoMsg.getString(0) + " F37" + isoMsg.getString(37));
                //Authorization Request	Request from a point-of-sale terminal for authorization for a cardholder purchase
                isoMsg = processAuthorizationServices.processAuthbyProcode(isoMsg,fieldData);
                break;
            default:
                break;
        }
        return isoMsg;
    }

    @Override
    public Optional<TransactionTypes> getTxnTypebyMTIAndProcodeAndActionstatusAndIntrash(String mti, String procode, String actionstatus, String intrash)
            throws JDBCConnectionException {
        return Optional.ofNullable(transTypesRepository.findByTxnMtiAndTxnProcodeAndActionStatusAndIntrash(mti, procode, actionstatus, intrash));
    }

}
