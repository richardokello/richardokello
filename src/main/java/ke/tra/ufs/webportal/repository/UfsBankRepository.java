package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBanks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsBankRepository extends CrudRepository<UfsBanks,Long> {

    UfsBanks findByBankNameOrBankCodeAndIntrash(String bankName, String bankCode, String intrash);
}
