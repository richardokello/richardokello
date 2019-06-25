package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.repository.UfsCustomerOwnerCrimeRepository;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerOwnerServiceTemplate implements CustomerOwnersService {

    private final UfsCustomerOwnerCrimeRepository customerOwnerCrimeRepository;

    public CustomerOwnerServiceTemplate(UfsCustomerOwnerCrimeRepository customerOwnerCrimeRepository) {
        this.customerOwnerCrimeRepository = customerOwnerCrimeRepository;
    }


    @Override
    public void save(CustomerOwnersCrime ownersCrime) {
        customerOwnerCrimeRepository.save(ownersCrime);
    }
}
