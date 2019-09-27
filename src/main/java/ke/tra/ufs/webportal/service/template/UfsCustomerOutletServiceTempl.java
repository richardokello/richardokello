package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.service.UfsCustomerOutletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UfsCustomerOutletServiceTempl implements UfsCustomerOutletService {

   private final UfsCustomerOutletRepository customerOutletRepository;

    public UfsCustomerOutletServiceTempl(UfsCustomerOutletRepository customerOutletRepository) {
        this.customerOutletRepository = customerOutletRepository;
    }

    @Override
    public UfsCustomerOutlet findByCustomerId(BigDecimal customerId) {
        return this.customerOutletRepository.findByCustomerIds(customerId);
    }
}
