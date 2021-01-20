package ke.co.tra.ufs.tms.service.templates;


import ke.co.tra.ufs.tms.entities.UfsCustomerOwners;
import ke.co.tra.ufs.tms.repository.UfsCustomerOwnerRepository;
import ke.co.tra.ufs.tms.service.CustomerOwnerService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerOwnerServiceTemplate implements CustomerOwnerService {

    private final UfsCustomerOwnerRepository customerOwnerRepository;

    public CustomerOwnerServiceTemplate(UfsCustomerOwnerRepository customerOwnerRepository) {
        this.customerOwnerRepository = customerOwnerRepository;
    }

    @Override
    @Transactional
    public UfsCustomerOwners findByCustomerOwnerId(Long customerOwnerId) {
        return customerOwnerRepository.findByIdAndIntrash(customerOwnerId, AppConstants.NO);
    }
}
