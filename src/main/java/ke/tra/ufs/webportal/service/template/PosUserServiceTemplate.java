package ke.tra.ufs.webportal.service.template;


import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsPosUser;
import ke.tra.ufs.webportal.repository.UfsPosUserRepository;
import ke.tra.ufs.webportal.service.PosUserService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class PosUserServiceTemplate implements PosUserService {

    private final UfsPosUserRepository ufsPosUserRepository;

    public PosUserServiceTemplate(UfsPosUserRepository ufsPosUserRepository) {
        this.ufsPosUserRepository = ufsPosUserRepository;
    }



    @Override
    @Transactional
    @Async
    public ResponseEntity<ResponseWrapper> sendSmsMessage(String phoneNumber, String message) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }



    @Override
    public UfsPosUser findByUsername(String username, BigDecimal deviceId) {
        return ufsPosUserRepository.findByUsernameAndIntrashAndTmsDeviceId(username, AppConstants.NO, deviceId);
    }

    @Override
    public UfsPosUser savePosUser(UfsPosUser ufsPosUser) {
        return ufsPosUserRepository.save(ufsPosUser);
    }

    @Override
    public UfsPosUser findByContactPersonIdAndDeviceId(Long contactPersonId, BigDecimal tmsDeviceId) {
        return ufsPosUserRepository.findByContactPersonIdAndTmsDeviceIdAndIntrash(contactPersonId, tmsDeviceId, AppConstants.NO);
    }

    @Override
    public UfsPosUser findByContactPersonIdAndDeviceIdAndSerialNumber(Long contactPersonId, BigDecimal tmsDeviceId, String serialNumber) {
        return ufsPosUserRepository.findByContactPersonIdAndTmsDeviceIdAndSerialNumber(contactPersonId, tmsDeviceId, serialNumber);
    }

    @Override
    public UfsPosUser findByCustomerOwnersIdAndDeviceId(Long customerOwnersId, BigDecimal tmsDeviceId) {
        return ufsPosUserRepository.findByCustomerOwnersIdAndTmsDeviceIdAndIntrash(customerOwnersId, tmsDeviceId, AppConstants.NO);
    }

    @Override
    public Optional<UfsPosUser> findByCustomerNotNullAndDeviceId(BigDecimal deviceId) {
        return ufsPosUserRepository.findFirstByTmsDeviceIdAndCustomerOwnersNotNull(deviceId);
    }

    @Override
    public List<UfsPosUser> findByContactPersonId(Long contactPersonId) {
        return ufsPosUserRepository.findByContactPersonIdAndIntrash(contactPersonId,AppConstants.NO);
    }

}
