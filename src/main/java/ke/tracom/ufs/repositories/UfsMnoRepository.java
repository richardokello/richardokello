package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsMno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsMnoRepository extends JpaRepository<UfsMno, BigDecimal> {

    UfsMno findByMnoNameAndIntrash(String name,String intrash);
}
