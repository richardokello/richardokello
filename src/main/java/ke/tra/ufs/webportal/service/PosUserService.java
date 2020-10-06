package ke.tra.ufs.webportal.service;


import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.UfsPosUser;
import ke.tra.ufs.webportal.entities.UfsUser;
import ke.tra.ufs.webportal.entities.wrapper.UserPinWrapper;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface PosUserService {



    /**
     * send sms
     *
     * @param phoneNumber
     * @param message
     * @return
     */
    ResponseEntity<ResponseWrapper> sendSmsMessage(String phoneNumber, String message);



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
     * @param contactPersonId
     * @return
     */
    UfsPosUser findByContactPersonIdAndDeviceIdAndSerialNumber(Long contactPersonId, BigDecimal tmsDeviceId,String serialNumber);

    /**
     * @param customerOwnersId
     * @return
     */
    UfsPosUser findByCustomerOwnersIdAndDeviceId(Long customerOwnersId, BigDecimal tmsDeviceId);

    Optional<UfsPosUser> findByCustomerNotNullAndDeviceId(BigDecimal deviceId);

}
