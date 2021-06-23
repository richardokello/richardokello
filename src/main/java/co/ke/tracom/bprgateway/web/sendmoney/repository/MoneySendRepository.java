package co.ke.tracom.bprgateway.web.sendmoney.repository;

import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoneySendRepository extends CrudRepository<MoneySend, Long> {

    Optional<MoneySend> findByRecevernumberAndMstokenAndAmount(
        String    receiverNo, String msToken, String amount
    );
}
