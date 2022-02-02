package co.ke.tracom.bprgateway.web.sendmoney.repository;

import co.ke.tracom.bprgateway.web.sendmoney.entity.TokenDurationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenDurationRepository extends JpaRepository<TokenDurationConfiguration,Long> {
    Optional<TokenDurationConfiguration> findByName(String name);
}
