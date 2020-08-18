package ke.tra.com.tsync.services.template;


import ke.tra.com.tsync.entities.wrappers.ActionWrapper;
import ke.tra.com.tsync.wrappers.ChangePin;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import ke.tra.com.tsync.wrappers.ResponseWrapper;

import java.math.BigDecimal;

public interface UserServiceTempl {

    /**
     * change pos pin
     * @Param wrapper
     */
    ResponseWrapper changePin(ChangePin wrapper);

    /**
     * login for first time
     * @param wrapper
     * @return
     */
    ResponseWrapper firstTimeLogin(PosUserWrapper wrapper);

    /**
     * lock pos user
     * @param accounts
     * @return
     */
    ResponseWrapper lockUser(ActionWrapper<BigDecimal> accounts);

    /**
     * unlock pos user
     * @param accounts
     * @return
     */
    ResponseWrapper unLockUser(ActionWrapper<BigDecimal> accounts);

    /**
     * send sms
     * @param phoneNumber
     * @param message
     * @return
     */
    ResponseWrapper sendSmsMessage(String phoneNumber,String message);

    /**
     * login
     * @param wrapper
     * @return
     */
    ResponseWrapper login(PosUserWrapper wrapper);


    ResponseWrapper resetPassword(PosUserWrapper wrapper);

    ResponseWrapper createPosUser(PosUserWrapper wrapper);

    ResponseWrapper deletePosUser(PosUserWrapper wrapper);

    ResponseWrapper terminalWasReset(PosUserWrapper wrapper);
}
