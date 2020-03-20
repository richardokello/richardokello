package ke.tra.com.tsync.services;


import ke.tra.com.tsync.services.template.AuthorizationTxnsTmpl;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProcessAuthorizationServices implements AuthorizationTxnsTmpl {

    @Autowired
    private  UserManagementService userManagementService;

    @Autowired
    private  CoreProcessorService coreProcessorService;

    @Autowired
    private  POSIrisTxns posIrisTxns;

    @Autowired
    private ToCBSTxns toCBSTxns;

    @Autowired private CBDataBaseOperations cbDataBaseOperations;

    @Autowired private CountyRevenueStreams countyRevenueStreams;

    @Autowired private RatePayerCategories ratePayerCategories;

    @Autowired private SupportCategoriesModule supportCategoriesModule;

    @Autowired private  SupportModule supportModule;

    @Autowired private  CashCollectionReconSvc cashCollectionReconSvc;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(POSIrisTxns.class);



    @Override
    public ISOMsg processAuthbyProcode(ISOMsg isoMsg, HashMap<String, Object> fieldDataMap){
        isoMsg.set(39, "06");
        switch (isoMsg.getString(3)){
            case "001000":
                //pos agent login
                isoMsg = userManagementService.processUserLogin(isoMsg,fieldDataMap);
                break;

            case "001110":
                //reset Pin change
                ISOMsg isoMsg2 = userManagementService.processUserLogin(isoMsg,fieldDataMap);
                if(isoMsg2.getString(39).equalsIgnoreCase("00"))
                    isoMsg = userManagementService.resetUserPin(isoMsg,fieldDataMap);
                else
                    return isoMsg2;

                break;

            case "001111":
                //password change
                String newpass = isoMsg.getString(72).trim();
                //logger.info("\n\nmewpasss {} \n\n", newpass);
                if((userManagementService.processUserLogin(isoMsg,fieldDataMap))
                        .getString(39).equalsIgnoreCase("00"))
                    isoMsg = userManagementService.changeUserPassword(isoMsg,fieldDataMap,newpass);
                break;

            case "011111":
                //Control number
                break;

            case "001100":
                isoMsg = posIrisTxns.receiveHeartBeat(isoMsg);
                break;

            case "002000":
                // fetch revenue streams
                isoMsg = countyRevenueStreams.setRevenueStreams(isoMsg);
                break;

            case "003000":
                // fetch revenue streams by Device
                isoMsg = countyRevenueStreams.RevenueItemsByDeviceRes(isoMsg,fieldDataMap);
                break;

            case "003100":
                // fetch revenue streams by stream Id
                isoMsg = countyRevenueStreams.getRevenueStreamItems(isoMsg);
                break;

            case "003200":
                // fetch Amount by Zone .. Stream ID
                isoMsg = countyRevenueStreams.getAmountByZoneAndStreamItemId(isoMsg);
                break;

            case "003300":
                // fetch Amount by Zone .. Stream ID
                isoMsg = ratePayerCategories.getARatePayerCategories(isoMsg);
                break;

            case "003400":
                // fetch child details using the stream code
                isoMsg = countyRevenueStreams.getStreamByUniqueID(isoMsg);
                break;

            case "003500":
                // fetch Account number usingthe using the stream code
                return countyRevenueStreams.getRevenueStreamAccountByID(isoMsg);


            case "003600":
                // fetch Account number using the using the stream code
                return supportCategoriesModule.getAllSupportCategories(isoMsg);


            case "003700":
                // create support case
                return supportModule.createSupportCase(isoMsg,fieldDataMap);


            case "003800":
                //create support case
                return supportModule.getSupportCaseByRefNo(isoMsg);

            case"003900":
                //all support cases by Agenct No
                return supportModule.getAllSupportCasesByAgentNo(isoMsg, fieldDataMap);


            case "004000":
                return supportModule.addFeedBackByCaseRef(isoMsg,fieldDataMap);

            case "311000":
                return cbDataBaseOperations.getAgentFloatBalance(isoMsg);


            case "310000":
                return toCBSTxns.sendToFlex(isoMsg);

            case "004400":
                return  cashCollectionReconSvc.getCashCollectionReconData(isoMsg);

            case "004200":
                return cashCollectionReconSvc.recordHourlyParkingStartDuration(isoMsg);

            case "004300":
                return  cashCollectionReconSvc.fetchPaymentDurationbyUniqueCode(isoMsg);

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
