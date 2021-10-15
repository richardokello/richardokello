package co.ke.tracom.bprgateway.web.transactionLimits;

import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionLimitManagerService {
    private final TransactionLimitManagerRepository transactionLimitManagerRepository;

    public TransactionLimitManager fetchTransactionLimitsByMTIAndProcessingCode(
            String MTI, String ProcessingCode) throws NoSuchElementException {
        Optional<TransactionLimitManager> optionalTransactionLimitManager =
                transactionLimitManagerRepository.findByISOMsgMTIAndProcessingCode(MTI, ProcessingCode);

        return optionalTransactionLimitManager.orElseThrow();
    }

    public TransactionLimitManager findById(Long id) throws Exception {
        Optional<TransactionLimitManager> optional = transactionLimitManagerRepository.findById(id);
        if(optional.isEmpty()) throw new RuntimeException("Transaction Limit does not exist");
        return optional.get();
    }

    public TransactionLimit isLimitValid(Long id, double amount) throws Exception {
        TransactionLimitManager transactionLimit = findById(id);
        boolean isValid = amount <= transactionLimit.getUpperlimit() && amount >= transactionLimit.getLowerlimit();
        return new TransactionLimit(isValid, transactionLimit.getUpperlimit(), transactionLimit.getLowerlimit());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionLimit {
        private boolean isValid;
        private Long upper;
        private Long lower;
    }
}
