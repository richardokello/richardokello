package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;

import java.math.BigDecimal;
import java.util.List;

public interface UfsCustomerOutletService {

    UfsCustomerOutlet findByCustomerId(BigDecimal customerId);

    UfsCustomerOutlet saveOutlet(UfsCustomerOutlet outlet);

    List<UfsCustomerOutlet> findByCustomerId(BigDecimal customerId, String intrash);
}
