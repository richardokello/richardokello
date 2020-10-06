package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import ke.tra.ufs.webportal.repository.UfsCustomerOwnerCrimeRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOwnerRepository;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CustomerOwnerServiceTemplate implements CustomerOwnersService {

    private final UfsCustomerOwnerCrimeRepository customerOwnerCrimeRepository;
    private final UfsCustomerOwnerRepository ownerRepository;

    public CustomerOwnerServiceTemplate(UfsCustomerOwnerCrimeRepository customerOwnerCrimeRepository,UfsCustomerOwnerRepository ownerRepository) {
        this.customerOwnerCrimeRepository = customerOwnerCrimeRepository;
        this.ownerRepository = ownerRepository;
    }


    @Override
    public void save(CustomerOwnersCrime ownersCrime) {
        customerOwnerCrimeRepository.save(ownersCrime);
    }

    @Override
    public UfsCustomerOwners findByCustomerIds(BigDecimal customerIds) {
        return ownerRepository.findByCustomerIdsAndIntrash(customerIds,AppConstants.INTRASH_NO);
    }

    @Override
    public UfsCustomerOwners findByUsername(String username) {
        return ownerRepository.findByUserNameAndIntrash(username,AppConstants.INTRASH_NO);
    }

    @Override
    public List<UfsCustomerOwners> findOwnersByCustomerIds(BigDecimal customerIds) {
        return ownerRepository.findByIntrashAndCustomerIds(AppConstants.INTRASH_NO,customerIds);
    }

    @Override
    public void saveOwner(UfsCustomerOwners customerOwners) {
        ownerRepository.save(customerOwners);
    }


}
