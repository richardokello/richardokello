package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsAuthentication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Authentication Repository
 *
 * @author eli.muraya
 *
 */
@Repository
public interface AuthenticationRepository extends CrudRepository<UfsAuthentication, Long> {

    /**
     * Used to fetch authentication by username
     *
     * @param username
     * @return
     */
    public UfsAuthentication findByusernameIgnoreCase(String username);

    /**
     * @param username
     * @return
     */
    public UfsAuthentication findByusername(String username);

    /**
     * @param userId
     * @return
     */
    public UfsAuthentication findByuserId(Long userId);

    /**
     * @param id
     * @return
     */
    public UfsAuthentication findByauthenticationId(Long id);

}
