package ke.tra.ufs.webportal.repository;


import ke.tra.ufs.webportal.entities.TransactionTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kenny
 */
@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionTypes, Long> {
}
