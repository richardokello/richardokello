/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;


import ke.tra.com.tsync.services.template.UserManagementTmpl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.ChangePin;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import ke.tra.com.tsync.wrappers.ResponseWrapper;

import ke.tra.com.tsync.wrappers.ufslogin.LoginesponseWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.UserLoginReq;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 * @author Cotuoma
 */
@Service
public class UserManagementService implements UserManagementTmpl {
    private final UserService userService;
    public UserManagementService(UserService userService) {

        this.userService = userService;
    }
    private static final org.slf4j.Logger usemanagelog = LoggerFactory.getLogger(UserManagementService.class);


    @Override
    public LoginesponseWrapper userResponseWrp(UserLoginReq logReq) {
        LoginesponseWrapper ulr = new LoginesponseWrapper();
        try {
            //  usemanagelog.info("usermanagementloginurl  : " + usermanagementloginurl);
            //  ulr = myRestTemplate.postForObject(usermanagementloginurl, logReq, LoginesponseWrapper.class);
        } catch (RestClientException e) {
            if (e instanceof ResourceAccessException) {
                ulr.setCode(05);
                ulr.setMessage("REMOTE SYSTEM UNAVAILABLE");
            }
            e.printStackTrace();
        }
        return ulr;
    }
    public ISOMsg loadUserNames(ISOMsg isoMsg, PosUserWrapper wrapper){

        try{

            ResponseWrapper response  =  new ResponseWrapper();

            response = userService.loadUserNames(wrapper);

            setResponse(isoMsg, response);

        }catch(Exception ex){
            handleException(isoMsg, ex);
        }


        return isoMsg;
    }
    @Override
    public ISOMsg changeUserPassword(ISOMsg isomsg, PosUserWrapper wrapper) {
        String fieldData = isomsg.getString(47);

        // if field 47 contains no data, then there is nothing to check
        if (fieldData == null || fieldData.isEmpty()){
            isomsg.set(39, AppConstants.POS_INVALID_PAYLOAD);
            isomsg.set(47, "field 47 is empty");
            return isomsg;
        }

        String currentPin = wrapper.getCurrentPin();
        String newPin = wrapper.getPin();
        String confirmPin = wrapper.getConfirmPin();


        // if any of the required data is null, then it's time to exit
        if (currentPin == null || newPin == null){
            isomsg.set(39, AppConstants.POS_CURR_OR_NEW_PIN_NULL);
            isomsg.set(47,"current pin || new pin  is null");
            return isomsg;
        }

        try {
            // check if newPin matches confirmPin before making any DB query
            if (newPin.equals(confirmPin)) {
                // todo send request to ufs change password resource || implimenent change password logic here
                ChangePin changePinWrapper = new ChangePin();
                changePinWrapper.setNewpin(wrapper.getPin());
                changePinWrapper.setUsername(wrapper.getUsername());
                changePinWrapper.setPin(wrapper.getCurrentPin());
                changePinWrapper.setConfirmPin(wrapper.getConfirmPin());
                changePinWrapper.setMID(isomsg.getString(42));
                changePinWrapper.setTID(isomsg.getString(41));
                changePinWrapper.setSerialNumber(wrapper.getSerialNumber());
                ResponseWrapper response = new ResponseWrapper();
                try {
                    //response = myRestTemplate.postForObject(posUserAuthUrl+"change-pin", changePinWrapper, ResponseWrapper.class);
                    response = userService.changePin(changePinWrapper);
//                    Integer code = Integer.valueOf(response.getCode());
                    setResponse(isomsg, response);

                }catch(Exception e){
                    handleException(isomsg, e);
                    e.printStackTrace();
                }

            }else{
                isomsg.set(39, AppConstants.POS_PIN_AND_CONFIRM_NOT_MATCH);
                isomsg.set(47, "Pin and confirm pin does not match");
            }

        } catch (Exception e) {
            handleException(isomsg, e);
        }

        return isomsg;
    }

    @Override
    public ISOMsg resetUserPin(ISOMsg isomsg, PosUserWrapper wrapper) {
        try{
            ResponseWrapper response  =  new ResponseWrapper();

            response = userService.resetPassword(wrapper);

            setResponse(isomsg, response);

        }catch (Exception e){
            handleException(isomsg, e);
        }
        return  isomsg;
    }



    @Override
    public ISOMsg createUser(ISOMsg isoMsg, PosUserWrapper wrapper) {

        ResponseWrapper response = new ResponseWrapper();
        try{
            response = userService.createPosUser(wrapper);
            setResponse(isoMsg, response);

        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;
    }



    @Override
    public ISOMsg processUserLogin(ISOMsg isomsg, PosUserWrapper wrapper) {

        PosUserWrapper req = new PosUserWrapper();

        req.setTID(isomsg.getString(41));
        req.setMID(isomsg.getString(42));

        ResponseWrapper responseWrapper = new ResponseWrapper();

        // additional data
        isomsg.unset(47);
        // local transaction time
        isomsg.unset(12);

        try {

            responseWrapper = userService.login(wrapper);

            setResponse(isomsg, responseWrapper);



        } catch (Exception e) {

            handleException(isomsg, e);

        }

        return isomsg;


    }

    @Override
    public ISOMsg deleteUser(ISOMsg isoMsg, PosUserWrapper wrapper){

        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            responseWrapper = userService.deletePosUser(wrapper);

            setResponse(isoMsg, responseWrapper);

        }catch(Exception ex){
            handleException(isoMsg, ex);
        }

        return isoMsg;

    }

    public ISOMsg firstTimeLogin(ISOMsg isoMsg, PosUserWrapper wrapper){

        ResponseWrapper responseWrapper = new ResponseWrapper();

        try{
            responseWrapper = userService.firstTimeLogin(wrapper);

            setResponse(isoMsg, responseWrapper);


        }catch (Exception e){
            handleException(isoMsg, e);

        }

        return isoMsg;
    }
    public  ISOMsg logout (ISOMsg isoMsg, PosUserWrapper wrapper){

        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.logout(wrapper);

            usemanagelog.info("---------000---------- "+responseWrapper.getCode());

            setResponse(isoMsg, responseWrapper);


        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;

    }

    public ISOMsg processTerminalReset(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{

            responseWrapper = userService.terminalWasReset(wrapper);

            setResponse(isoMsg, responseWrapper);

        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;
    }

    public ISOMsg disableUsers(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.disableUsers(wrapper);

            setResponse(isoMsg, responseWrapper);

        }catch (Exception e){
            handleException(isoMsg, e);

        }
        return isoMsg;
    }

    public ISOMsg enableUser(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.enableUser(wrapper);

            setResponse(isoMsg, responseWrapper);

        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;
    }

    private void chunkUses(ResponseWrapper responseWrapper, ISOMsg isoMsg){
        String usersStr = responseWrapper.getMessage();

        Integer [] fields = {47,116,117,118,119,120,121,122, 123,124,125};
        String t = "";
        int chunkLen = 990;
        int end = 990;
        int countField = 0;
        int start = 0;
        int usersStrLen = usersStr.length();
        while(usersStrLen > chunkLen && end < usersStrLen){

            isoMsg.set(fields[countField], usersStr.substring(start, end));

            usemanagelog.info(usersStr.substring(start, end));

            start = end;
            end  = end + chunkLen;
            countField += 1;


        }

        isoMsg.set(fields[countField], usersStr.substring(start)); //use start coz it will still in bound of the string

    } // chunk users

    private void setResponse(ISOMsg isoMsg, ResponseWrapper responseWrapper){
        // set field 47 and 39

        isoMsg.set(39, responseWrapper.getCode());
        isoMsg.set(47, responseWrapper.getMessage());

    }

    private void handleException(ISOMsg msg, Exception e){
        msg.set(39, AppConstants.POS_SERVER_ERROR); // system error
        usemanagelog.error("System error ", e.getMessage());
        msg.set(47, "System Error Occurred");
        e.printStackTrace();
    }
}
