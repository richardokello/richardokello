/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;


import ke.tra.com.tsync.services.template.UserManagementTmpl;
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
 * @author Tracom
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
                ulr.setCode(53);
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
            isomsg.set(39, "07");
            isomsg.set(47, "field 47 is empty");
            return isomsg;
        }

        String currentPin = wrapper.getCurrentPin();
        String newPin = wrapper.getPin();
        String confirmPin = wrapper.getConfirmPin();


        // if any of the required data is null, then it's time to exit
        if (currentPin == null || newPin == null){
            isomsg.set(39, "07");
            isomsg.set(47,"current pin || new pin ||");
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
                    Integer code = Integer.valueOf(response.getCode());
                    if(code == 200){
                        isomsg.set(39, "00");
                        isomsg.set(47, "Password change was a success");
                    }else{
                        setResponse(isomsg, response);
                    }
                }catch(Exception e){
                    usemanagelog.error("An error occurred UFS-TMS service could not process the request", e.getMessage());
                    isomsg.set(39, "06");
                    isomsg.set(47, "Error processing  the request");
                    e.printStackTrace();
                }

            }else{
                isomsg.set(39, "32");
                isomsg.set(47, "Pin and confirm pin does not match");
            }

        } catch (Exception e) {
            usemanagelog.info("Exception changing user password: {}", e.getMessage());
            usemanagelog.error("Exception changing user password: {}", e.getMessage());
            isomsg.set(39, "06");
        }

        return isomsg;
    }

    @Override
    public ISOMsg resetUserPin(ISOMsg isomsg, PosUserWrapper wrapper) {
        try{
            ResponseWrapper response  =  new ResponseWrapper();

            System.out.println(wrapper.getUsername());

            response = userService.resetPassword(wrapper);
            Integer code = Integer.valueOf(response.getCode());
            if (code == 200){
                isomsg.set(39, "00");
                isomsg.set(47,"User password reset was a success");
            }
            if (code == 404){
                isomsg.set(39, "04");
                isomsg.set(47,response.getMessage());
            }
            else{
                isomsg.set(47,response.getMessage());
                if(Integer.toString(response.getCode()).trim().length()>2){
                    isomsg.set(39, Integer.toString(response.getCode()).substring(1,3));
                }else{
                    isomsg.set(39, Integer.toString(response.getCode()));
                }
            }

            System.out.println(response.getCode());
        }catch (Exception e){
            isomsg.set(47,"System  error");
            usemanagelog.error("Error sending request to ufs-tms", e.getMessage());
            e.printStackTrace();
        }
        return  isomsg;
    }



    @Override
    public ISOMsg createUser(ISOMsg isoMsg, PosUserWrapper wrapper) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.createPosUser(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }

            else if(code == 400){
                isoMsg.set(39, "08"); // bad request
                isoMsg.set(47, responseWrapper.getMessage());
            }

            else{

                if(Integer.toString(responseWrapper.getCode()).strip().length()>2){
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
                    isoMsg.set(47, responseWrapper.getMessage());
                }else{
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()));
                    isoMsg.set(47, responseWrapper.getMessage());
                }
            }
            System.out.println(responseWrapper.getMessage());
        }catch (Exception e){
            isoMsg.set(39, "06"); // system error
            usemanagelog.error("Error sending request to ufs-tms", e.getMessage());
            e.printStackTrace();
        }
        return isoMsg;
    }



    @Override
    public ISOMsg processUserLogin(ISOMsg isomsg, PosUserWrapper wrapper) {

        PosUserWrapper req = new PosUserWrapper();

        //req.setUfsWorkgroup((String) wrapper.get("userWorkGroup"));
        req.setTID(isomsg.getString(41));
        req.setMID(isomsg.getString(42));

        ResponseWrapper responseWrapper = new ResponseWrapper();

        // additional data
        isomsg.unset(47);
        // local transaction time
        isomsg.unset(12);

        try {

            responseWrapper = userService.login(wrapper);

            Integer code = Integer.valueOf(responseWrapper.getCode());


            if(code == 200){
                isomsg.set(39, "00"); // login success
                isomsg.set(47, responseWrapper.getMessage());
            } else{
                if(Integer.toString(responseWrapper.getCode()).strip().length()>2){
                    isomsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
                }else{
                    isomsg.set(39, Integer.toString(responseWrapper.getCode()));
                }
                isomsg.set(47, responseWrapper.getMessage());
            }


        } catch (Exception e) {

            handleException(isomsg, e);

        }

        return isomsg;



//        final String[] responsecode = new String[1];
//        final String[] responseData = new String[6];
//        final String[] posmessage = new String[1];


//        urw.ifPresentOrElse(
//                loginResponseWrpr -> {
//                    usemanagelog.info("loginResponse \n {} ", loginResponseWrpr.toString());
//                    // usemanagelog.info("loginResponseWrpr", loginResponseWrpr.getMessage());
//                    switch ((int) loginResponseWrpr.getCode()) {
//                        case 200:
//
//                            responsecode[0] = "00";
//                            //For now leave both ... confirm once we agree with POS guys
//                            responseData[0] = loginResponseWrpr.getMessage();
//                            final String[] userRoles = {""};
//                            loginResponseWrpr.getData().getATTENDANTROLE().stream().forEach(attendantrole -> {
//                                userRoles[0] += attendantrole.getId().toString().concat("|")
//                                        .concat(attendantrole.getName().concat("|*"));
//                            });
//
//
//                            /**
//                             * ZoneID|ZONECODE|ZONENAME|ONECOIUNTYID|ZONECOUNTYCODE|ZONECOUNTYNAME|*
//                             */
//
//                            posmessage[0] =
//                                    loginResponseWrpr.getCode().toString()
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getMessage())
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getData().getAccountNumber())
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getData().getMDA())
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getData().getATTENDANT())
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getData().getAGENTID())
//                                            .concat("#")
//                                            .concat(loginResponseWrpr.getData().getPHONENUMBER())
//                                            .concat("#")
//                                            .concat(userRoles[0])
//                                            .concat("#")
//                                            .concat(zoneData[0]);
//
//                            /**
//                             LOGINMESSAGE#ACNUMBER#MDA#ATTENDANT#AGENTID#PHONENUMBER#USERROLES#ZONEDATA
//                             */
//                            responsecode[0] = "00";
//                            break;
//                        case 503:
//                            responsecode[0] = "92";
//                            posmessage[0] = loginResponseWrpr.getMessage() + " - " + loginResponseWrpr.getCode();
//                            break;
//                        default:
//                            responsecode[0] = "03";
//                            posmessage[0] = loginResponseWrpr.getMessage() + " - " + loginResponseWrpr.getCode();
//                            break;
//                    }
//                },
//                () -> {
//                    responsecode[0] = "03";
//                }
//        );


//        usemanagelog.info("responsecode[0] \n {} ", responsecode[0]);
//        isomsg.set(39, responsecode[0]);
//
//        if (posmessage[0] != null)
//            isomsg.set(72, posmessage[0]);
//        return isomsg;

    }

    @Override
    public ISOMsg deleteUser(ISOMsg isoMsg, PosUserWrapper wrapper){

        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            responseWrapper = userService.deletePosUser(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());

            if (code == 00) {
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            } else {
                if (Integer.toString(responseWrapper.getCode()).trim().length() > 2) {
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1, 3));
                } else {
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()));
                }

                isoMsg.set(47, responseWrapper.getMessage());
            }
        }catch(Exception ex){
            handleException(isoMsg, ex);
        }

        return isoMsg;

    }

    public ISOMsg firstTimeLogin(ISOMsg isoMsg, PosUserWrapper wrapper){

        ResponseWrapper responseWrapper = new ResponseWrapper();

        try{
            responseWrapper = userService.firstTimeLogin(wrapper);

            Integer code = Integer.valueOf(responseWrapper.getCode());
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else{
                isoMsg.set(47,responseWrapper.getMessage());
                if(Integer.toString(responseWrapper.getCode()).trim().length()>2){
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
                }else{
                    isoMsg.set(39, Integer.toString(responseWrapper.getCode()));
                }
            }

        }catch (Exception e){
            handleException(isoMsg, e);

        }

        return isoMsg;
    }
    public  ISOMsg logout (ISOMsg isoMsg, PosUserWrapper wrapper){
        usemanagelog.info("---------000----------");
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.logout(wrapper);
            usemanagelog.info("---------111----------");
            int code = responseWrapper.getCode();
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else{
                setResponse(isoMsg, responseWrapper);
            }
            System.out.println(responseWrapper.getMessage());
        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;

    }

    public ISOMsg processTerminalReset(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            // another approach
            // would be divide the usersstrlLen with chinkLen to ge t count of complete chunks
            // the for loop and substring
            responseWrapper = userService.terminalWasReset(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());

            if(code == 200){
                chunkUses(responseWrapper, isoMsg);

                isoMsg.set(39, "00");

            }
            else{
                setResponse(isoMsg, responseWrapper);
            }
        }catch (Exception e){
            handleException(isoMsg, e);
        }
        return isoMsg;
    }

    public ISOMsg disableUsers(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.disableUsers(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else{
                setResponse(isoMsg, responseWrapper);
            }
        }catch (Exception e){
            isoMsg.set(39, "06"); // system error
            handleException(isoMsg, e);

        }
        return isoMsg;
    }

    public ISOMsg enableUser(ISOMsg isoMsg, PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            responseWrapper = userService.enableUser(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else{
                setResponse(isoMsg, responseWrapper);
            }
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
        if(Integer.toString(responseWrapper.getCode()).trim().length()>2){
            isoMsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
            isoMsg.set(47, responseWrapper.getMessage());
        }else{
            isoMsg.set(39, Integer.toString(responseWrapper.getCode()));
            isoMsg.set(47, responseWrapper.getMessage());
        }
    }

    private void handleException(ISOMsg msg, Exception e){
        msg.set(39, "06"); // system error
        usemanagelog.error("System error ", e.getMessage());
        msg.set(47, "System Error Occurred");
        e.printStackTrace();
    }
}
