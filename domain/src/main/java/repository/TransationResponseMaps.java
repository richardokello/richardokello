package repository;

import entities.PosirisTrnxErrorCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TransationResponseMaps extends JpaRepository<PosirisTrnxErrorCodes, BigDecimal> {

    Optional<PosirisTrnxErrorCodes> findFirstByCodeEquals(String code);
}
