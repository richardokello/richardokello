package ke.tra.com.tsync.services;

import ke.tra.com.tsync.services.crdb.CRDBPipService;
import ke.tra.com.tsync.services.template.AuthorizationTxnsTmpl;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProcessAuthorizationServices implements AuthorizationTxnsTmpl {

    @Autowired private  UserManagementService userManagementService;
    @Autowired private  CoreProcessorService coreProcessorService;
    @Autowired private  POSIrisTxns posIrisTxns;
    @Autowired private RatePayerCategories ratePayerCategories;
    @Autowired private SupportCategoriesModule supportCategoriesModule;
    @Autowired private  CashCollectionReconSvc cashCollectionReconSvc;
    @Autowired private CRDBPipService crdbPipService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(POSIrisTxns.class);

    @Override
    public ISOMsg processAuthbyProcode(ISOMsg isoMsg, PosUserWrapper wrapper){
        isoMsg.set(39, "06");

        switch (isoMsg.getString(3)){
            case "001000":
                //pos agent login
                System.out.println(wrapper);
                isoMsg = userManagementService.processUserLogin(isoMsg,wrapper);

                break;
            case "001110":
                //reset Pin change
                //ISOMsg isoMsg2 = userManagementService.processUserLogin(isoMsg,wrapper);
                //if(isoMsg2.getString(39).equalsIgnoreCase("00"))
                     isoMsg = userManagementService.resetUserPin(isoMsg,wrapper);
//                else
//                    return isoMsg2;
                break;

            case "001111":
                //password change
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.changeUserPassword(isoMsg,wrapper);
                break;
            case "000020":
                //password change
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.createUser(isoMsg,wrapper);
                break;
            case "011113":
                //password change
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.deleteUser(isoMsg,wrapper);
                break;

            case "011114":
                //password change
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))

                isoMsg = userManagementService.firstTimeLogin(isoMsg,wrapper);
                break;

            case "011111": //Inquire Control Number
                return crdbPipService.inquireGEPGControlNumber(isoMsg);

            case "011112": //Post Control Number 000020
                //Post Control number
                return crdbPipService.postGEPGControlNumber(isoMsg);

            case "001100": //TMS heartbeats
                isoMsg = posIrisTxns.receiveHeartBeat(isoMsg);
                break;

            case "003300":
                // fetch Amount by Zone .. Stream ID
                isoMsg = ratePayerCategories.getARatePayerCategories(isoMsg);
                break;

            case "003600":
                // fetch Account number using the using the stream code
                return supportCategoriesModule.getAllSupportCategories(isoMsg);

            default:
                isoMsg.set(39,"05");
                break;
        }

        return  isoMsg;
    }

    public ISOMsg processAuthbyProcode_noTlv(ISOMsg isoMsg) {
        return isoMsg;
    }
}
