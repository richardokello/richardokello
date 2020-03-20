/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;

import ke.tra.com.tsync.services.template.UserManagementTmpl;
import ke.tra.com.tsync.wrappers.TrcmGeneralRestWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.LoginesponseWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.UserLoginReq;
import org.jpos.iso.ISOMsg;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tracom
 */
@Service
public class UserManagementService implements UserManagementTmpl {
    private static final org.slf4j.Logger usemanagelog = LoggerFactory.getLogger(UserManagementService.class);
/*
    @Value("${usermanagementlogin}")
    private String usermanagementloginurl;

    @Value("${change_user_pass}")
    private String changeUserPass;

    @Value("${reset_user_pass}")
    private String resetUserPass;
*/
    @Autowired
    private RestTemplate myRestTemplate;

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
    public ISOMsg changeUserPassword(ISOMsg isomsg, HashMap<String, Object> dataMap, String newpass) {

        return isomsg;
    }

    @Override
    public ISOMsg resetUserPin(ISOMsg isomsg, HashMap<String, Object> dataMap) {

        return isomsg;
    }

    @Override
    public UserLoginReq createLoginRequest(ISOMsg isoMsg, HashMap<String, Object> dataMap) {
        UserLoginReq req = new UserLoginReq();
        String username = (String) dataMap.get("userName");
        String pass = (String) dataMap.get("userAccessCode");
        req.setUsername(username);
        req.setPin(pass);
        req.setSerialNumber((String) dataMap.get("serialNumber"));
        System.out.println("login req \n " + req.toString());

        return req;
    }


    @Override
    public ISOMsg processUserLogin(ISOMsg isomsg, HashMap<String, Object> dataMap) {

        Optional<LoginesponseWrapper> urw = Optional.ofNullable(userResponseWrp(createLoginRequest(isomsg, dataMap)));
        final String[] responsecode = new String[1];
        final String[] responseData = new String[6];
        final String[] posmessage = new String[1];
        urw.ifPresentOrElse(
                loginResponseWrpr -> {
                    usemanagelog.info("loginResponse \n {} ", loginResponseWrpr.toString());
                    // usemanagelog.info("loginResponseWrpr", loginResponseWrpr.getMessage());
                    switch ((int) loginResponseWrpr.getCode()) {
                        case 200:

                            responsecode[0] = "00";
                            //For now leave both ... confirm once we agree with POS guys
                            responseData[0] = loginResponseWrpr.getMessage();
                            final String[] userRoles = {""};
                            loginResponseWrpr.getData().getATTENDANTROLE().stream().forEach(attendantrole -> {
                                userRoles[0] += attendantrole.getId().toString().concat("|")
                                        .concat(attendantrole.getName().concat("|*"));
                            });

                            final String[] zoneData = {""};
                            loginResponseWrpr
                                    .getData()
                                    .getZONE()
                                    .forEach(
                                            (item) -> zoneData[0] += item.getId().toString().concat("|")
                                            .concat(item.getCode().concat("|"))
                                            .concat(item.getName().concat("|"))
                                            .concat(item.getCountyId().concat("|"))
                                            .concat(item.getCountyIds().getCountyCode()).concat("|")
                                            .concat(item.getCountyIds().getName()).concat("|")
                                            .concat("*")
                            );

                            /**
                             * ZoneID|ZONECODE|ZONENAME|ONECOIUNTYID|ZONECOUNTYCODE|ZONECOUNTYNAME|*
                             */

                            posmessage[0] =
                                    loginResponseWrpr.getCode().toString()
                                            .concat("#")
                                            .concat(loginResponseWrpr.getMessage())
                                            .concat("#")
                                            .concat(loginResponseWrpr.getData().getAccountNumber())
                                            .concat("#")
                                            .concat(loginResponseWrpr.getData().getMDA())
                                            .concat("#")
                                            .concat(loginResponseWrpr.getData().getATTENDANT())
                                            .concat("#")
                                            .concat(loginResponseWrpr.getData().getAGENTID())
                                            .concat("#")
                                            .concat(loginResponseWrpr.getData().getPHONENUMBER())
                                            .concat("#")
                                            .concat(userRoles[0])
                                            .concat("#")
                                            .concat(zoneData[0]);

                            /**
                             LOGINMESSAGE#ACNUMBER#MDA#ATTENDANT#AGENTID#PHONENUMBER#USERROLES#ZONEDATA
                             */
                            responsecode[0] = "00";
                            break;
                        case 503:
                            responsecode[0] = "92";
                            posmessage[0] = loginResponseWrpr.getMessage() + " - " + loginResponseWrpr.getCode();
                            break;
                        default:
                            responsecode[0] = "03";
                            posmessage[0] = loginResponseWrpr.getMessage() + " - " + loginResponseWrpr.getCode();
                            break;
                    }
                },
                () -> {
                    responsecode[0] = "03";
                }
        );


        usemanagelog.info("responsecode[0] \n {} ", responsecode[0]);
        isomsg.set(39, responsecode[0]);

        if (posmessage[0] != null)
            isomsg.set(72, posmessage[0]);
        return isomsg;
    }

}
