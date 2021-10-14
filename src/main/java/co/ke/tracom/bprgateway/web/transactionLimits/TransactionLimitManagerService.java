package co.ke.tracom.bprgateway.web.transactionLimits;

import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
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

  public TransactionLimitManager findById(Long id) {
    return transactionLimitManagerRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction Limit does not exist"));
  }
}
