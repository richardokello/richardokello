/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.wrappers.ufslogin.LoginesponseWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.UserLoginReq;
import org.jpos.iso.ISOMsg;

import java.util.HashMap;

/**
 *
 * @author Tracom
 */
public interface UserManagementTmpl {

    ISOMsg processUserLogin(ISOMsg isomsg,HashMap<String,Object> dataMap );
    UserLoginReq createLoginRequest(ISOMsg isoMsg, HashMap<String,Object> dataMap);
    LoginesponseWrapper userResponseWrp(UserLoginReq userLoginReq);
    ISOMsg changeUserPassword(ISOMsg isomsg,HashMap<String, Object> dataMap, String newpass);
    ISOMsg resetUserPin(ISOMsg isomsg,HashMap<String, Object> dataMap);

}
