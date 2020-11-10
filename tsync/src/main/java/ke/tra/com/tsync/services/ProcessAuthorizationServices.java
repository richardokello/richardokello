package ke.tra.com.tsync.services;

import ke.tra.com.tsync.services.crdb.CRDBPipService;
import ke.tra.com.tsync.services.template.AuthorizationTxnsTmpl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProcessAuthorizationServices implements AuthorizationTxnsTmpl {

    @Autowired private RatePayerCategories ratePayerCategories;
    @Autowired private SupportCategoriesModule supportCategoriesModule;
    @Autowired private  CashCollectionReconSvc cashCollectionReconSvc;
    @Autowired private CRDBPipService crdbPipService;
    @Autowired private UserManagementService userManagementService;
    @Autowired private POSIrisTxns posIrisTxns;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(POSIrisTxns.class);

    @Override
    public ISOMsg processAuthbyProcode(ISOMsg isoMsg, PosUserWrapper wrapper){
        isoMsg.set(39, "006");
        isoMsg.set(47, "An error occurred within the processAuthbyProcode method");

        switch (isoMsg.getString(3)){
            case "001000":
                //pos agent login
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

                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.changeUserPassword(isoMsg,wrapper);

                break;
            case "000020":
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.createUser(isoMsg,wrapper);

                break;
            case "001120":
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))

                isoMsg = userManagementService.loadUserNames(isoMsg,wrapper);

                break;
            case "000120":
                userManagementService.disableUsers(isoMsg, wrapper);

                break;
            case  "000121":
                isoMsg = userManagementService.enableUser(isoMsg, wrapper);

                break;
            case "011113":

                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.deleteUser(isoMsg,wrapper);

                break;

            case "011115":

                isoMsg = userManagementService.processTerminalReset(isoMsg,wrapper);

                break;

            case "011114":
                //password change

                isoMsg = userManagementService.firstTimeLogin(isoMsg,wrapper);

                break;
            case "000021":
                //password change
                //String newpass = isoMsg.getString(72).trim();
                isoMsg = userManagementService.logout(isoMsg, wrapper);

                break;

            case "001100": //TMS heartbeats
                isoMsg = posIrisTxns.receiveHeartBeat(isoMsg);
                break;
            case "011111": //Inquire Control Number
                return crdbPipService.inquireGEPGControlNumber(isoMsg);

            case "011112": //Post Control Number 000020
                //Post Control number
                return crdbPipService.postGEPGControlNumber(isoMsg);

            case "003300":
                // fetch Amount by Zone .. Stream ID
                isoMsg = ratePayerCategories.getARatePayerCategories(isoMsg);
                break;

            case "003600":
                // fetch Account number using the using the stream code
                return supportCategoriesModule.getAllSupportCategories(isoMsg);

            default:
                isoMsg.set(39, AppConstants.POS_INVALID_PROCODE_OR_MTI);
                isoMsg.set(47, "Invalid Processing code..");
                break;
        }

        return  isoMsg;
    }

    public ISOMsg processAuthbyProcode_noTlv(ISOMsg isoMsg) {
        return isoMsg;
    }
}
