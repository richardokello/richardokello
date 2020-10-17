package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBankRegion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsBankRegionRepository extends CrudRepository<UfsBankRegion, BigDecimal> {

    List<UfsBankRegion> findByIntrash(String intrash);
    UfsBankRegion findByRegionNameAndIntrash(String regionName,String intrash);
    UfsBankRegion findByCodeAndIntrash(String code, String intrash);

}
