package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.OauthAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface OauthAccessTokenRepository extends CrudRepository<OauthAccessToken, String> {

    OauthAccessToken findByuserName(String userName);
}
