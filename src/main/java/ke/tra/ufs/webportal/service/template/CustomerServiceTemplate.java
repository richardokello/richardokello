package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.repository.CustomerRepository;
import ke.tra.ufs.webportal.service.CustomerService;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceTemplate implements CustomerService {

    private  final CustomerRepository customerRepository;

    public CustomerServiceTemplate(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UfsCustomer findByCustomerId(Long id) {
        return customerRepository.findByCustomerId(id);
    }

    @Override
    public void saveCustomer(UfsCustomer customer) {
        this.customerRepository.save(customer);
    }
}
