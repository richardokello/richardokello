package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    UfsCustomer findByCustomerId(Long id);

    UfsCustomer saveCustomer(UfsCustomer customer);

    List<UfsCustomer> getAllTerminatedAgents(String action,String actionStatus);

    List<UfsCustomerOwners> getAllCustomerOwners();

    UfsCustomerOutlet findByOutletCode(String outletCode);

    UfsCustomerOutlet findByCustomerIds(BigDecimal customerIds);

    List<UfsCustomerOutlet> findOutletsByCustomerIds(BigDecimal customerIds);

    void saveOutlet(UfsCustomerOutlet customerOutlet);

    void updateCustomersMids();

    void updateCustomerMidPerId(UfsCustomer customer);

    boolean findIfMidIsActive(String mid, String intrashNo);
}
