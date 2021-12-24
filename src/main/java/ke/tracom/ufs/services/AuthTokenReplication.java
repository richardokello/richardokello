package ke.tracom.ufs.services;

import org.springframework.scheduling.annotation.Async;

public interface AuthTokenReplication {

    void replicateAuthToken(String username);
}
