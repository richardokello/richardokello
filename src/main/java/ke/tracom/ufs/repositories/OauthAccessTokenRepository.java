package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.OauthAccessToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OauthAccessTokenRepository extends CrudRepository<OauthAccessToken, String> {

    @Modifying
    @Query("DELETE FROM #{#entityName} u WHERE u.userName LIKE ?1%")
    void deleteAllByUsername(String username);


}
