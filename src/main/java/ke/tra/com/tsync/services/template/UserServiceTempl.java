package ke.tra.com.tsync.services.template;

import ke.tra.com.tsync.wrappers.ChangePin;
import ke.tra.com.tsync.wrappers.EmailAndMessageWrapper;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import ke.tra.com.tsync.wrappers.ResponseWrapper;


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
     * Disable pos user
     * @param wrapper
     * @return
     */
    ResponseWrapper enableUser(PosUserWrapper wrapper);

    /**
     * unlock pos user
     * @param wrapper
     * @return
     */
    ResponseWrapper disableUsers(PosUserWrapper wrapper);

    /**
     * send sms
     * @param request
     */
    ResponseWrapper sendMessage(EmailAndMessageWrapper request);

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
