package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParReceiptProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface ParReceiptProfileRepository extends CrudRepository<ParReceiptProfile, BigDecimal> {

    ParReceiptProfile findAllByNameAndIntrash(String name, String intrash);
}
