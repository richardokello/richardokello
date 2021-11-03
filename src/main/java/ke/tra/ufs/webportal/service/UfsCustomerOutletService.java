package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;

import java.math.BigDecimal;
import java.util.List;

public interface UfsCustomerOutletService {

    UfsCustomerOutlet findByCustomerId(BigDecimal customerId);

    UfsCustomerOutlet findById(Long outletId);

    UfsCustomerOutlet saveOutlet(UfsCustomerOutlet outlet);

<<<<<<< HEAD
    List<UfsCustomerOutlet> findByCustomerIdIn(List<BigDecimal> customerId, String intrash);
=======
    public UfsCustomerOutlet findByOutletId(Long id);
>>>>>>> brb-webportal

    List<UfsCustomerOutlet> findByCustomerId(BigDecimal customerId, String intrash);

    List<UfsCustomerOutlet> findByCustomerIdIn(List<BigDecimal> customerId, String intrash);

    void deleteByCustomerId(BigDecimal customerId);
}
