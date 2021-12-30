package ke.tracom.ufs.services.template;

import ke.tracom.ufs.repositories.AuthTokenReplicationRepository;
import ke.tracom.ufs.services.AuthTokenReplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenReplicationTemplate implements AuthTokenReplication {

    @Autowired
    private AuthTokenReplicationRepository authTokenReplicationRepository;

    @Override
    public void replicateAuthToken(String username) {
        this.authTokenReplicationRepository.replicateToken(username);
    }
}