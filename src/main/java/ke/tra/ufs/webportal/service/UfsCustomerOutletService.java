package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;

import java.math.BigDecimal;

public interface UfsCustomerOutletService {

    UfsCustomerOutlet findByCustomerId(BigDecimal customerId);
}
