package co.ke.tracom.bprgateway.web.sendmoney.repository;

import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneySendRepository extends JpaRepository<MoneySend, Long> {
    Optional<MoneySend> findByRecevernumberAndMstoken(String receiverNo, String msToken);
    //Optional<List<MoneySend>> findBySendmoneytokenexpiretimeBeforeAndFulfilmentstatusEquals(long sendmoneytokenexpiretime, int fulfilmentstatus);
    @Query(nativeQuery = true,value ="select *\n" +
            "from BPRMONEYSEND\n" +
            "where rownum <= ?3 and FULFILMENTSTATUS=?1 and SECONDTOKENEXPIRYTIME is null" +
            " and SENDMONEYTOKENEXPIRETIME < ?2 and " +
            "SENDMONEYTOKENSTARTTIME is not null order by MONEYSENDID desc")
    Optional<List<MoneySend>> findByFulfilmentstatusEquals(int i,long now,int limit);
}

