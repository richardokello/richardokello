package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.UfsPosUser;
import ke.co.tra.ufs.tms.entities.UfsUser;
import ke.co.tra.ufs.tms.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.wrappers.UserPinWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PosUserService {

    /**
     * reset pin
     *
     * @param ids
     * @return
     */
    ResponseEntity<ResponseWrapper> resetPin(ActionWrapper<BigDecimal> ids);

    /**
     * generate username and password for pos devices
     *
     * @Param entity
     */
    void createPosUser(TmsDevice entity);

    /**
     * change pos pin
     *
     * @Param wrapper
     */
    ResponseEntity<ResponseWrapper> changePin(UserPinWrapper wrapper);

    /**
     * login for first time
     *
     * @param wrapper
     * @return
     */
    ResponseEntity<ResponseWrapper> firstTimeLogin(UserPinWrapper wrapper);

    /**
     * lock pos user
     *
     * @param accounts
     * @return
     */
    ResponseEntity<ResponseWrapper> lockUser(ActionWrapper<BigDecimal> accounts);

    /**
     * unlock pos user
     *
     * @param accounts
     * @return
     */
    ResponseEntity<ResponseWrapper> unLockUser(ActionWrapper<BigDecimal> accounts);

    /**
     * send sms
     *
     * @param phoneNumber
     * @param message
     * @return
     */
    ResponseEntity<ResponseWrapper> sendSmsMessage(String phoneNumber, String message);

    /**
     * login
     *
     * @param wrapper
     * @return
     */
    ResponseEntity<ResponseWrapper> login(UserPinWrapper wrapper);

    /**
     * Create UfsUser
     *
     * @param entity
     * @return
     */
    UfsUser createUfsUser(TmsDevice entity);

    /**
     * @param username
     * @return
     */
    UfsPosUser findByUsername(String username, BigDecimal deviceId);

    /**
     * @param ufsPosUser
     * @return
     */
    UfsPosUser savePosUser(UfsPosUser ufsPosUser);

    /**
     * @param contactPersonId
     * @return
     */
    UfsPosUser findByContactPersonIdAndDeviceId(Long contactPersonId, BigDecimal tmsDeviceId);

    /**
     * @param customerOwnersId
     * @return
     */
    UfsPosUser findByCustomerOwnersIdAndDeviceId(Long customerOwnersId, BigDecimal tmsDeviceId);

    /**
     * @param tmsDeviceId
     * @param firstTimeUser
     * @return
     */
    UfsPosUser findByDeviceIdAndFirstTime(BigDecimal tmsDeviceId, Short firstTimeUser);


    /**
     * @param action
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsPosUser> getPosUsers(String action, String actionStatus, String needle, Pageable pg);
    /**
     * @param action
     * @param actionStatus
     * @param needle
     * @param tmsDeviceId
     * @param customerOwnersId
     * @param pg
     * @return
     */
    public Page<UfsPosUser> getPosUsers(String action, String actionStatus, String needle,BigDecimal tmsDeviceId,Long customerOwnersId, Pageable pg);

    public UfsPosUser findByPosUserId(BigDecimal posUserId);

    List<UfsPosUser> findByContactPersonId(Long contactPersonId);


}
