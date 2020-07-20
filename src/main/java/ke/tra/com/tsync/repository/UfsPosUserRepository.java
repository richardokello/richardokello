package ke.tra.com.tsync.repository;


import ke.tra.com.tsync.entities.UfsPosUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsPosUserRepository extends JpaRepository<UfsPosUser, BigDecimal> {

    UfsPosUser findByUsername(String username);

    List<UfsPosUser> findByUsernameStartsWithIgnoreCaseAndActionStatusAndIntrash(String username, String actionStatus, String inTrash);

    UfsPosUser findByUsernameIgnoreCaseAndIntrash(String username, String intrash);
    UfsPosUser findByUserIds(Long ids);

}
