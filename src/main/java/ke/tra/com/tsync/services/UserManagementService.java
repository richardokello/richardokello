/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;


import ke.tra.com.tsync.entities.wrappers.UserDetailsWrapper;
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

import java.util.Optional;

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
                ulr.setCode(503);
                ulr.setMessage("REMOTE SYSTEM UNAVAILABLE");
            }
            e.printStackTrace();
        }
        return ulr;
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
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(wrapper);
        String username = wrapper.getUsername();
        String currentPin = wrapper.getCurrentPin();
        String newPin = wrapper.getPin();
        String confirmPin = wrapper.getConfirmPin();


        // if any of the required data is null, then it's time to exit
        if (currentPin == null || newPin == null || confirmPin == null){
            isomsg.set(39, "07");
            isomsg.set(47,"current pin || new pin || confirm pin missing");
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
                    }
                    else if( code == 403){
                        isomsg.set(39, "03");
                        isomsg.set(47, "Invalid old password");
                    }
                    else if( code == 400){
                        isomsg.set(39, "08");
                        isomsg.set(47, response.getMessage());
                    }else{
                        isomsg.set(47,response.getMessage());
                        if(Integer.toString(response.getCode()).trim().length()>2){
                            isomsg.set(39, Integer.toString(response.getCode()).substring(1,3));
                        }else{
                            isomsg.set(39, Integer.toString(response.getCode()));
                        }
                    }
                    System.out.println("++++++++++++++++++++++++++++++++++");
                    System.out.println(response.getCode());
                    System.out.println(response.getMessage());
                }catch(Exception e){
                    usemanagelog.error("An error occurred UFS-TMS service could not process the request", e.getMessage());
                    isomsg.set(39, "06");
                    isomsg.set(47, "Error processing  the request");
                    e.printStackTrace();
                }

            }else{
                isomsg.set(39, "05");
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
            responseWrapper = userService.makePosUser(wrapper);
            Integer code = Integer.valueOf(responseWrapper.getCode());
            if(code == 200){
                isoMsg.set(39, "00");
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else if(code == 409){
                isoMsg.set(39, "09"); // user exist
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else if(code == 400){
                isoMsg.set(39, "08"); // bad request
                isoMsg.set(47, responseWrapper.getMessage());
            }
            else if(code == 403){
                isoMsg.set(47, responseWrapper.getMessage());
                isoMsg.set(39, "03"); // forbidden
            }
            else{
                System.out.println(responseWrapper.getCode());
                if(Integer.toString(responseWrapper.getCode()).trim().length()>2){
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

        System.out.println(wrapper);

        PosUserWrapper req = new PosUserWrapper();

        //req.setUfsWorkgroup((String) wrapper.get("userWorkGroup"));
        req.setTID(isomsg.getString(41));
        req.setMID(isomsg.getString(42));

        ResponseWrapper responseWrapper = new ResponseWrapper<UserDetailsWrapper>();

        // additional data
        isomsg.unset(47);
        // local transaction time
        isomsg.unset(12);

        try {

            //don not honor response code=>05

            responseWrapper = userService.login(wrapper);

            Integer code = Integer.valueOf(responseWrapper.getCode());
            System.out.println("-------------------------+ "+responseWrapper.getCode());
            System.out.println(responseWrapper.getData());
            System.out.println(responseWrapper.getMessage());


            if(code == 403){
                isomsg.set(39, "03"); // forbidden
                isomsg.set(47, "Wrong password");
            }

            else if(code == 200){
                isomsg.set(39, "00"); // login success
                isomsg.set(47, responseWrapper.getMessage());
            } else{
                if(Integer.toString(responseWrapper.getCode()).trim().length()>2){
                    isomsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
                }else{
                    isomsg.set(39, Integer.toString(responseWrapper.getCode()));
                }
                isomsg.set(47, responseWrapper.getMessage());
            }


        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            isomsg.set(39,"06");
            isomsg.set(47, "System error");

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

        responseWrapper = userService.deletePosUser(wrapper);
        Integer code = Integer.valueOf(responseWrapper.getCode());

        if(code == 00){
            isoMsg.set(39, "00");
            isoMsg.set(47, responseWrapper.getMessage());
        }
        if(code == 409){
            isoMsg.set(39, "09"); // user exist
            isoMsg.set(47, responseWrapper.getMessage());
        }
        if(code == 400){
            isoMsg.set(39, "08"); // bad request
            isoMsg.set(47, responseWrapper.getMessage());
        }
        if(code == 403){
            isoMsg.set(39, "03"); // forbidden
            isoMsg.set(47, responseWrapper.getMessage());
        }
        else{
            System.out.println(responseWrapper.getCode());
            if(Integer.toString(responseWrapper.getCode()).trim().length()>2){
                isoMsg.set(39, Integer.toString(responseWrapper.getCode()).substring(1,3));
            }else{
                isoMsg.set(39, Integer.toString(responseWrapper.getCode()));
            }

            isoMsg.set(47, responseWrapper.getMessage());
        }
        System.out.println(responseWrapper.getMessage());
        return isoMsg;

    }

}
