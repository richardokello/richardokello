package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParReceiptItems;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface ParReceiptItemsRepository extends CrudRepository<ParReceiptItems, BigDecimal> {

    ParReceiptItems findByNameAndIntrash(String name, String intrash);
}
