package ke.tra.com.tsync.repository;


import ke.tra.com.tsync.entities.UfsPosUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UfsPosUserRepository extends JpaRepository<UfsPosUser, BigDecimal> {

    UfsPosUser findByUsername(String username);
    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndIntrash(String username, String inTrash);
    @Query(value="" +
            "SELECT * \n" +
            "    FROM UFS_POS_USER \n" +
            "    INNER JOIN TMS_DEVICE T\n" +
            "    ON UFS_POS_USER.DEVICE_ID = T.DEVICE_ID\n" +
            "WHERE UPPER(UFS_POS_USER.USERNAME) lIKE :prefix\n" +
            "AND T.ACTION_STATUS = 'Approved'\n" +
            "AND T.INTRASH='NO'\n" +
            "AND T.OUTLET_ID=(SELECT DISTINCT TD.OUTLET_ID FROM TMS_DEVICE TD WHERE TD.serial_NO=:serialNo)\n"+
            "AND UFS_POS_USER.INTRASH = 'NO'\n",
            nativeQuery=true
    )
    List<UfsPosUser> findByUsernamePrefix(@Param("prefix")String prefix, @Param("serialNo") String serialNo);

    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndActionStatusAndIntrash(String username, String actionStatus, String inTrash);

    Optional<UfsPosUser> findByUsernameIgnoreCaseAndIntrash(String username, String intrash);
    Optional<UfsPosUser> findByIdNumberAndIntrash(String idNumber, String intrash);

}
