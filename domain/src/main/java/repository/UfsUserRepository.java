package repository;
import entities.UfsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsUserRepository extends JpaRepository<UfsUser,Long> {
    UfsUser findByPhoneNumberAndIntrash(String phoneNumber, String intrash);
    UfsUser findByPhoneNumber(String phoneNumber);

}
