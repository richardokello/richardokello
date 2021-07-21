package co.ke.tracom.bprgateway.web.transactions.services;

import co.ke.tracom.bprgateway.web.transactions.entities.AllTransactions;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.repository.AllTransactionsRepository;
import co.ke.tracom.bprgateway.web.transactions.repository.T24TXNQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionService {
    private final AllTransactionsRepository allTransactionsRepository;
    private final T24TXNQueueRepository t24TXNQueueRepository;

    public void saveCardLessTransactionToAllTransactionTable(T24TXNQueue tot24, String transactionType, String MTI,
                                                             double amount, String processingStatus, String TID, String MID) {
        AllTransactions allTransactions =
                new AllTransactions()
                        .setField000(MTI) // Transaction type
                        .setField002("") // card number
                        .setField003(tot24.getProcode()) // Processing code
                        .setField004(String.valueOf(amount)) // Amount
                        .setField005(tot24.getTotalchargeamt()) // Amount Charges
                        .setField037(tot24.getGatewayref()) // RRN
                        .setField039(processingStatus) // Processing status
                        .setField041(TID) // TID
                        .setField042(MID) // MID
                        .setField061(tot24.getT24reference()) // Response to POS
                        .setField121("CARDLESS")
                        .setField123(transactionType)
                        .setInsertTime(new Date())
                        .setCreditAccountNo(tot24.getCreditacctno())
                        .setDebitAccountNo(tot24.getDebitacctno())
                        .setT24PosRef(tot24.getGatewayref()) // RRN
                        .setT24Reference(tot24.getT24reference())
                        .setT24ResponseCode(processingStatus);

        allTransactionsRepository.save(allTransactions);
    }

    public void updateT24TransactionDTO(T24TXNQueue tot24) {
        t24TXNQueueRepository.save(tot24);
    }
}
