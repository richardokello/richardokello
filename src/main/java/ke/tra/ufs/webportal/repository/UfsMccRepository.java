package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsMcc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsMccRepository extends CrudRepository<UfsMcc, BigDecimal> {

    List<UfsMcc> findByNameAndIntrash(String name, String intrash);

    List<UfsMcc> findByValueAndIntrash(String value, String intrash);

}
