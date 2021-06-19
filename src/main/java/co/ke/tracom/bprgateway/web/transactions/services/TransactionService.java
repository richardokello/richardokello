package co.ke.tracom.bprgateway.web.transactions.services;

import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.repository.AllTransactionsRepository;
import co.ke.tracom.bprgateway.web.transactions.repository.T24TXNQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionService {
    private final AllTransactionsRepository allTransactionsRepository;
    private final T24TXNQueueRepository t24TXNQueueRepository;

    public void saveCardLessTransactionToAllTransactionTable(T24TXNQueue tot24, String agent_deposit_to_customer) {

    }

    public void updateT24TransactionDTO(T24TXNQueue tot24) {
        t24TXNQueueRepository.save(tot24);
    }
}
