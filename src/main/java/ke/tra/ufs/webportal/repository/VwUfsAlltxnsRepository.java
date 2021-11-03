package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.views.VwUfsAlltxns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;


@Repository
public interface VwUfsAlltxnsRepository extends JpaRepository<VwUfsAlltxns, BigInteger> {

    Long countAllByTransactiontypeAndTidIn(String transactionType, Set<String> tids);

    List<VwUfsAlltxns> findAllByTidInAndTransactiontypeIsNotNull(Set<String> tids);

    List<VwUfsAlltxns> findAllByTidInAndTransactiontype(Set<String> tids, String transactionType);

}
