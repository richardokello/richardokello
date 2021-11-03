package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsBanks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsBankRepository extends CrudRepository<UfsBanks,Long> {

    UfsBanks findByBankCodeAndIntrash(String bankCode, String intrash);
}
