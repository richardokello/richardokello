package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.PosirisTrnxErrorCodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransationResponseMaps extends JpaRepository<PosirisTrnxErrorCodes, BigDecimal> {

    Optional<PosirisTrnxErrorCodes> findFirstByCodeEquals(String code);
}
