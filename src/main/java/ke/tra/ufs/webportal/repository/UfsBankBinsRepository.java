package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBankBins;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UfsBankBinsRepository extends CrudRepository<UfsBankBins, Long> {

    public List<UfsBankBins> findAllByBankIds(Long bankIds);
    List<UfsBankBins> findAllByBankIdsAndIdIn(Long bankIds, List<Long> list);
}
