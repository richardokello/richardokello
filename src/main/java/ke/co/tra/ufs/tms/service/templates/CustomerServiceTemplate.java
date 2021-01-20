package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.UfsCustomer;
import ke.co.tra.ufs.tms.repository.UfsCustomerRepository;
import ke.co.tra.ufs.tms.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomerServiceTemplate implements CustomerService {

    private final UfsCustomerRepository ufsCustomerRepository;

    public CustomerServiceTemplate(UfsCustomerRepository ufsCustomerRepository) {
        this.ufsCustomerRepository = ufsCustomerRepository;
    }

    @Override
    public Optional<UfsCustomer> findById(Long customerId) {
        return ufsCustomerRepository.findById(customerId);
    }

}
