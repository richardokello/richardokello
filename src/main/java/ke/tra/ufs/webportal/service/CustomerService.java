package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomer;

public interface CustomerService {

    UfsCustomer findByCustomerId(Long id);

    void saveCustomer(UfsCustomer customer);
}
