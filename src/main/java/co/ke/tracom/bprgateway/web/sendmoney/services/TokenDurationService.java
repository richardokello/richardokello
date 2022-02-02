package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.web.sendmoney.data.TokenDuration;
import co.ke.tracom.bprgateway.web.sendmoney.entity.TokenDurationConfiguration;
import co.ke.tracom.bprgateway.web.sendmoney.repository.TokenDurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenDurationService {

    private final TokenDurationRepository repository;

    private TokenDurationConfiguration getConfigurationByName(String name) {
        Optional<TokenDurationConfiguration> byName = repository.findByName(name);
        return byName.orElse(null);
    }

    public Duration getInitialDurationByConfigurationName(String name) {
        TokenDurationConfiguration configuration = getConfigurationByName(name);
        if (configuration != null) {
            return TokenDuration.getDuration(configuration.getInitialDuration(), configuration.getInitialDurationLength());
        }
        return null;
    }

    public Duration getSecondDurationByConfigurationName(String name) {
        TokenDurationConfiguration configuration = getConfigurationByName(name);
        if (configuration != null) {
            return TokenDuration.getDuration(configuration.getSecondDuration(), configuration.getSecondDurationLength());
        }
        return null;
    }


}
