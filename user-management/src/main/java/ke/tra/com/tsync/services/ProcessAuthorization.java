package ke.tra.com.tsync.services;

import entities.OnlineActivity;
import entities.PosIris;
import entities.TransactionTypes;
import ke.tra.com.tsync.services.template.AuthorizationTxnsTemplate;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import org.hibernate.exception.JDBCConnectionException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.OnlineActivityRepository;
import repository.PosIrisRepo;
import repository.TransTypesRepository;
import repository.TransationResponseMaps;

import java.util.Date;
import java.util.Optional;


@Service
public class ProcessAuthorization implements AuthorizationTxnsTemplate {

    @Autowired private  UserManagementService userManagementService;
    @Autowired private  POSIrisTxns posIrisTxns;
    @Autowired
    private PosIrisRepo posIrisRepo;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(POSIrisTxns.class);

    private void UserManagementResponseCodesHelper(ISOMsg isoMsg){
         isoMsg.set(120, "UMANAGE");
    }

    @Override
    public ISOMsg processAuthbyProcode(ISOMsg isoMsg, PosUserWrapper wrapper){
        isoMsg.set(39, "006");
        isoMsg.set(47, "An error occurred within the processAuthbyProcode method");
        LOGGER.info("procode--------- "+isoMsg.getString(3));
        switch (isoMsg.getString(3)){
            case "001000":
                //pos agent login
                LOGGER.info("wrapper at process auth service--------- "+wrapper);

                isoMsg = userManagementService.processUserLogin(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);

                break;
            case "001110":
                //reset Pin change
                //ISOMsg isoMsg2 = userManagementService.processUserLogin(isoMsg,wrapper);
                //if(isoMsg2.getString(39).equalsIgnoreCase("00"))
                 isoMsg = userManagementService.resetUserPin(isoMsg,wrapper);
                 UserManagementResponseCodesHelper(isoMsg);
//                else
//                    return isoMsg2;
                break;

            case "001111":

                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.changeUserPassword(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;
            case "000020":
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.createUser(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;
            case "001120":
                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))

                isoMsg = userManagementService.loadUserNames(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);

                break;
            case "000120":
                userManagementService.disableUsers(isoMsg, wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;
            case  "000121":
                isoMsg = userManagementService.enableUser(isoMsg, wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;
            case "011113":

                //String newpass = isoMsg.getString(72).trim();
//                if((userManagementService.processUserLogin(isoMsg,wrapper))
//                        .getString(39).equalsIgnoreCase("00"))
                isoMsg = userManagementService.deleteUser(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;

            case "011115":

                isoMsg = userManagementService.processTerminalReset(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;

            case "011114":
                //password change

                isoMsg = userManagementService.firstTimeLogin(isoMsg,wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;
            case "000021":
                //password change
                //String newpass = isoMsg.getString(72).trim();
                isoMsg = userManagementService.logout(isoMsg, wrapper);
                UserManagementResponseCodesHelper(isoMsg);
                break;

            case "001100": //TMS heartbeats
                isoMsg = posIrisTxns.receiveHeartBeat(isoMsg);
                break;


            default:
                isoMsg.set(39, AppConstants.POS_INVALID_PROCODE_OR_MTI);
                isoMsg.set(47, "Invalid Processing code..");
                break;
        }

        return  isoMsg;
    }


    @Autowired
    private TransationResponseMaps transationResponseMaps;
    @Autowired
    private TransTypesRepository transTypesRepository;
    @Autowired
    private OnlineActivityRepository onlineActivityRepository;

    @Autowired
    private ISOMsgTOEntityImp isoMsgTOEntity;


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


    public ISOMsg setResponseDescription(ISOMsg isoMsg) {
        if (!isoMsg.hasField(72)) {
            final String responseDesc[] = new String[1];
            transationResponseMaps.findFirstByCodeEquals(isoMsg.getString(39)).ifPresentOrElse(
                    response -> responseDesc[0] = response.getDescription(),
                    () -> responseDesc[0] = "ERROR: INVALID POSIRIS TRANSACTION CODE"
            );
            isoMsg.set(72, responseDesc[0]);
        } else {
            LOGGER.info("RESPONSE DESC : " + isoMsg.getString(72));
        }
        return isoMsg;
    }


    public PosUserWrapper terminalDataTLVMap(String fielddata) {

        PosUserWrapper wrapper = new PosUserWrapper();
        int skip = 0;
        String Tag = null;
        String TagLength = null;
        String TagValue = null;
        LOGGER.info("++++++++===++)))++++ HERE");
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
//                            System.out.println("++++++++++++ setAppVersion "+TagValue);
                            break;
                        case 26:
                            wrapper.setSerialNumber(TagValue);
//                            System.out.println("+++++++++++++ setSerialNumber "+TagValue);
                            break;


                        case 29:
                            wrapper.setGender(Integer.valueOf(TagValue));
//                            System.out.println("+++++++++++++ Genderr " +TagValue);
                            break;

                        case 30:
                            wrapper.setUsername(TagValue);
//                            System.out.println("+++++++++++++ setUsername " +TagValue);
                            break;

                        case 31:
                            wrapper.setOtherName(TagValue);
//                            System.out.println("+++++++++++++ set OtherName"+TagValue);
                            break;

                        case 32:
                            wrapper.setEmail(TagValue);
                            break;

                        case 33:
                            wrapper.setPhoneNumber(TagValue);
//                            System.out.println("+++++++++++++ setPhoneNumber "+TagValue);
                            break;

                        case 34:
                            wrapper.setIdNumber(TagValue);
//                            System.out.println("+++++++++++++ setIdNumber "+TagValue);
                            break;

                        case 35:
                            wrapper.setUserAccessCode(TagValue);
                            break;

                        case 36:
                            wrapper.setPin(TagValue); // New pin|user pin
//                            System.out.println("+++++++++++++ setPin "+TagValue);
                            break;

                        case 37:
                            wrapper.setWorkgroup(TagValue);
//                            System.out.println("+++++++++++++ setUfsWorkgroup "+TagValue);
                            break;
                        case 39:
                            wrapper.setFirstName(TagValue);
//                            System.out.println("+++++++++++++ setFirstName "+TagValue);
                            break;
                        case 40:
                            wrapper.setConfirmPin(TagValue); //  confirm
//                            System.out.println("-----confirm Pin "+ TagValue);
                        case 41:
                            wrapper.setCurrentPin(TagValue); //  confirm
//                            System.out.println("++++++Current Pin "+ TagValue); // pin that is being changed
                        default:
                            break;
                    }
                    i++;
                    skip = skip + Integer.valueOf(TagLength) + 6;
                    if (((skip) == fielddata.length()) || (i > 50)) {
                        more = false;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    break;
                } catch(StringIndexOutOfBoundsException e){
                    e.printStackTrace();
                    break;

                }
            }
        }

        return wrapper;
    }


    public void saveOnlineActivity(ISOMsg isoMsg) {

        String posIrisData = isoMsg.getString(9) == null?"":isoMsg.getString(9);
        LOGGER.info("Some data-----at user management TLV-------"+ posIrisData);
        insertPosIrisData(posIrisRepo, posIrisData);
        try {
            Date date = new Date();
            OnlineActivity onlineActivity = (OnlineActivity) isoMsgTOEntity.convertIsoToPojo(isoMsg);
            onlineActivity.setInserttime(date);
            if (isoMsg.getString(39).strip().equalsIgnoreCase(AppConstants.POS_APPROVED) || isoMsg.getString(39).strip().equalsIgnoreCase(AppConstants.POS_APPROVED_00)) {


                onlineActivity.setStatus("SUCCESS");

                onlineActivityRepository.save(onlineActivity);

            } else {

                onlineActivity.setStatus("FAILED");
                onlineActivityRepository.save(onlineActivity);


            }
        } catch (Exception e) {
            LOGGER.error("saveOnlineActivity for isomsg %s ", isoMsg.toString(), e);
            e.printStackTrace();
        }
    }


    public ISOMsg processTransactionsbyMTI(ISOMsg isoMsg) throws ISOException, JDBCConnectionException {
        LOGGER.info(" processTransactionsbyMTI : " + isoMsg.getString(3));
        PosUserWrapper fieldData = new PosUserWrapper();

        if (isoMsg.hasField(47)) {
            fieldData = terminalDataTLVMap(isoMsg.getString(47));
            //process the transaction
            LOGGER.info(" fieldDataMap  : " + fieldData);
        }
        fieldData.setMID(isoMsg.getString(42)==null?"":isoMsg.getString(42));
        fieldData.setTID(isoMsg.getString(41)==null?"":isoMsg.getString(41));
        isoMsg.set(39, "06");
        switch (isoMsg.getString(0)) {
            case "0100":
            case "1100":
                LOGGER.info(" STEP INQUIRY  : " + isoMsg.getString(0) + " F37" + isoMsg.getString(37));
                //Authorization Request	Request from a point-of-sale terminal for authorization for a cardholder purchase
                isoMsg = processAuthbyProcode(isoMsg,fieldData);

                break;

            default:
                isoMsg.set(47, "Invalid MTI");
                isoMsg.set(39, "100");
                break;
        }
        return isoMsg;
    }

    public Optional<TransactionTypes> getTxnTypebyMTIAndProcodeAndActionstatusAndIntrash(String mti, String procode, String actionstatus, String intrash)
            throws JDBCConnectionException {
        return Optional.ofNullable(transTypesRepository.findByTxnMtiAndTxnProcodeAndActionStatusAndIntrash(mti, procode, actionstatus, intrash));
    }

    public void insertPosIrisData(PosIrisRepo posIrisRepo, String tmsData){

        String[] terminalData = tmsData.split("\\|");
        if (terminalData.length != 7)
            LOGGER.info("Some data about  PosIris are Missing ");
        else  {
            PosIris posIris = new PosIris();
            try {
                posIris.setCharging(terminalData[0]);
                posIris.setBatteryLevel(terminalData[1]);
                posIris.setSignal(terminalData[2]);
                posIris.setOsVersion(terminalData[3]);
                posIris.setTemperature(terminalData[4]);
                posIris.setAppVersion(terminalData[5]);
                posIris.setSerialNumber(terminalData[6]);

                posIrisRepo.save(posIris);
            } catch (Exception e){
                LOGGER.error("Exception Logging PosIris data {}", e.getMessage());

            }
        }
    }


    public ISOMsg processTransactionAdvice(ISOMsg isoMsg) {
        LOGGER.info("Processing financial advice");

        try {

            if (isoMsg.getString(9) != null) {
                // posIris data is available
                insertPosIrisData(posIrisRepo, isoMsg.getString(9));
            }

            isoMsg.set(39, AppConstants.POS_APPROVED);
            isoMsg.set(47, "Advice success");

        }catch (Exception e){
            isoMsg.set(39, AppConstants.POS_SERVER_ERROR); // System mulfunction
            isoMsg.set(47, "PosIris data not saved");
            LOGGER.error("An error occurred when processing advice", e);
        }

        return isoMsg;

    }

}
