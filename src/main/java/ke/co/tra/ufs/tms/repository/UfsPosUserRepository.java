package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.UfsPosUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UfsPosUserRepository extends JpaRepository<UfsPosUser, BigDecimal> {

    UfsPosUser findByUsername(String username);

    UfsPosUser findByUsernameAndIntrashAndTmsDeviceId(String username, String intrash, BigDecimal deviceId);

    UfsPosUser findByContactPersonIdAndTmsDeviceIdAndIntrash(Long contactPersonId, BigDecimal tmsDeviceId, String intrash);

    UfsPosUser findByCustomerOwnersIdAndTmsDeviceIdAndIntrash(Long customerOwnersId, BigDecimal tmsDeviceId, String intrash);

    UfsPosUser findByTmsDeviceIdAndFirstTimeUserAndIntrash(BigDecimal tmsDeviceId,Short FirstTimeUser,String intrash);


    UfsPosUser findByPosUserIdAndIntrash(BigDecimal posUserId, String intrash);

    /**
     * @param action
     * @param actionStatus
     * @param needle
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.action LIKE ?1% AND u.actionStatus LIKE ?2% "
            + "AND (u.activeStatus LIKE %?3% OR u.firstName LIKE %?3% OR u.otherName LIKE %?3% OR u.serialNumber LIKE %?3%)  AND lower(u.intrash) = lower(?4)")
    Page<UfsPosUser> findAll(String action, String actionStatus, String needle, String intrash, Pageable pg);

    /**
     * @param action
     * @param actionStatus
     * @param needle
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.action LIKE ?1% AND u.actionStatus LIKE ?2% "
            + "AND (u.activeStatus LIKE %?3% OR u.firstName LIKE %?3% OR u.otherName LIKE %?3% OR u.serialNumber LIKE %?3%)  AND lower(u.intrash) = lower(?4)"
            + "AND u.tmsDeviceId = ?5  AND u.customerOwnersId = ?6")
    Page<UfsPosUser> findAll(String action, String actionStatus, String needle, String intrash,BigDecimal tmsDeviceId,Long customerOwnersId, Pageable pg);


    List<UfsPosUser> findByContactPersonIdAndIntrash(Long contactPersonId, String intrash);


}
