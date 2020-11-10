/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.wrappers.PosUserWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.LoginesponseWrapper;
import ke.tra.com.tsync.wrappers.ufslogin.UserLoginReq;
import org.jpos.iso.ISOMsg;


/**
 *
 * @author Tracom
 */
public interface UserManagementTmpl {

    ISOMsg processUserLogin(ISOMsg isomsg, PosUserWrapper wrapper);
    LoginesponseWrapper userResponseWrp(UserLoginReq userLoginReq);
    ISOMsg changeUserPassword(ISOMsg isomsg,PosUserWrapper wrapper);
    ISOMsg resetUserPin(ISOMsg isomsg,PosUserWrapper wrapper);
    ISOMsg createUser(ISOMsg isoMsg, PosUserWrapper wrapper);
    ISOMsg deleteUser(ISOMsg isoMsg, PosUserWrapper wrapper);

}
