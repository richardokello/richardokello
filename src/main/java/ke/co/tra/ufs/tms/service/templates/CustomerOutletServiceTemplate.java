package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.UfsCustomerOutlet;
import ke.co.tra.ufs.tms.repository.UfsCustomerOutletRepository;
import ke.co.tra.ufs.tms.service.CustomerOutletService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CustomerOutletServiceTemplate implements CustomerOutletService {

    private final UfsCustomerOutletRepository ufsCustomerOutletRepository;

    public CustomerOutletServiceTemplate(UfsCustomerOutletRepository ufsCustomerOutletRepository) {
        this.ufsCustomerOutletRepository = ufsCustomerOutletRepository;
    }

    @Override
    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerId) {
        return this.ufsCustomerOutletRepository.findBycustomerIds(customerId);
    }

    @Override
    public UfsCustomerOutlet findByOutletId(Long id) {
        return ufsCustomerOutletRepository.findByIdAndIntrash(id, AppConstants.NO);
    }

}
