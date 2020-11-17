package repository;

import entities.UfsPosUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UfsPosUserRepository extends JpaRepository<UfsPosUser, BigDecimal> {
    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndIntrash(String username, String inTrash);
    @Query(value="" +
            "SELECT * \n" +
            "    FROM UFS_POS_USER \n" +
            "    INNER JOIN TMS_DEVICE T\n" +
            "    ON UFS_POS_USER.DEVICE_ID = T.DEVICE_ID\n" +
            "WHERE UPPER(UFS_POS_USER.USERNAME) lIKE :prefix\n" +
            "AND T.INTRASH='NO'\n" +
            "AND T.OUTLET_ID=(SELECT DISTINCT TD.OUTLET_ID FROM TMS_DEVICE TD WHERE TD.serial_NO=:serialNo)\n"+
            "AND UFS_POS_USER.INTRASH = 'NO'\n",
            nativeQuery=true
    )
    List<UfsPosUser> findByUsernamePrefix(@Param("prefix")String prefix, @Param("serialNo") String serialNo);

    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndActionStatusAndIntrash(String username, String actionStatus, String inTrash);

    Optional<UfsPosUser> findByUsernameIgnoreCaseAndIntrashAndSerialNumber(String username, String intrash, String serialNo);
    Optional<UfsPosUser> findByUsernameIgnoreCaseAndIntrash(String username, String intras);

    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndSerialNumberAndIntrashAndPosRole(String prefix, String serialNumber, String intrash, String posRole);
    List<UfsPosUser> findAllByUsernameIgnoreCaseAndIntrash(String username, String intrash);


}
