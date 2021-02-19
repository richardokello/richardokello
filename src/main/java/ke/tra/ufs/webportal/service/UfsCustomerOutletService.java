package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;

import java.math.BigDecimal;
import java.util.List;

public interface UfsCustomerOutletService {

    UfsCustomerOutlet findByCustomerId(BigDecimal customerId);

    UfsCustomerOutlet findById(Long outletId);

    UfsCustomerOutlet saveOutlet(UfsCustomerOutlet outlet);

    public UfsCustomerOutlet findByOutletId(Long id);

    List<UfsCustomerOutlet> findByCustomerId(BigDecimal customerId, String intrash);
}
