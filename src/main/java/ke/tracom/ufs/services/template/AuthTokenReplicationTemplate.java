package ke.tracom.ufs.services.template;

import ke.tracom.ufs.repositories.AuthTokenReplicationRepository;
import ke.tracom.ufs.services.AuthTokenReplication;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenReplicationTemplate implements AuthTokenReplication {

    private final AuthTokenReplicationRepository authTokenReplicationRepository;

    @Override
    @Async
    public void replicateAuthToken(String username) {
        this.authTokenReplicationRepository.replicateToken(username);
    }
}
