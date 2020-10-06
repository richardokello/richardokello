package ke.tra.ufs.webportal.repository;


import ke.tra.ufs.webportal.entities.UfsPosUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UfsPosUserRepository extends JpaRepository<UfsPosUser, BigDecimal> {

    UfsPosUser findByUsername(String username);

    UfsPosUser findByUsernameAndIntrashAndTmsDeviceId(String username, String intrash, BigDecimal deviceId);

    UfsPosUser findByContactPersonIdAndTmsDeviceIdAndIntrash(Long contactPersonId, BigDecimal tmsDeviceId, String intrash);

    UfsPosUser findByContactPersonIdAndTmsDeviceIdAndSerialNumber(Long contactPersonId, BigDecimal tmsDeviceId, String serialNumber);

    UfsPosUser findByCustomerOwnersIdAndTmsDeviceIdAndIntrash(Long customerOwnersId, BigDecimal tmsDeviceId, String intrash);

    Optional<UfsPosUser> findFirstByTmsDeviceIdAndCustomerOwnersNotNull(BigDecimal deviceId);

}
