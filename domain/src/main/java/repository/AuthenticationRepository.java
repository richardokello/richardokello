package repository;

import entities.UfsAuthentication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
     * @param id
     * @return
     */
    public UfsAuthentication findByauthenticationId(Long id);

}
