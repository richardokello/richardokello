package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import ke.tra.ufs.webportal.repository.CustomerRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOwnerRepository;
import ke.tra.ufs.webportal.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceTemplate implements CustomerService {

    private  final CustomerRepository customerRepository;
    private final UfsCustomerOwnerRepository customerOwnerRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;

    public CustomerServiceTemplate(CustomerRepository customerRepository,UfsCustomerOwnerRepository customerOwnerRepository,
                                   UfsCustomerOutletRepository customerOutletRepository) {
        this.customerRepository = customerRepository;
        this.customerOwnerRepository = customerOwnerRepository;
        this.customerOutletRepository = customerOutletRepository;
    }

    @Override
    public UfsCustomer findByCustomerId(Long id) {
        return customerRepository.findByCustomerId(id);
    }

    @Override
    public void saveCustomer(UfsCustomer customer) {
        this.customerRepository.save(customer);
    }


    @Override
    public List<UfsCustomer> getAllTerminatedAgents(String action,String actionStatus) {
        return this.customerRepository.findByActionAndActionStatus(action,actionStatus);
    }

    @Override
    public List<UfsCustomerOwners> getAllCustomerOwners() {
        return ((List<UfsCustomerOwners>) this.customerOwnerRepository.findAll());
    }

    @Override
    public UfsCustomerOutlet findByOutletCode(String outletCode) {
        return customerOutletRepository.findByOutletCode(outletCode);
    }
}
