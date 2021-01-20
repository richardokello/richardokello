package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.UfsCustomer;

import java.util.Optional;

public interface CustomerService {


    /**
     *
     * @param customerId
     * @return
     */
    public Optional<UfsCustomer> findById(Long customerId);


}
