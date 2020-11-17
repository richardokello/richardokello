package repository;
import entities.PosirisTrnxErrorCodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionResponseMaps extends JpaRepository<PosirisTrnxErrorCodes, BigDecimal> {
    Optional<PosirisTrnxErrorCodes> findFirstByCodeEquals(String code);
}
