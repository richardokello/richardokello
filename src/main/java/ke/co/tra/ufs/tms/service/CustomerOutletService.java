package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.UfsCustomerOutlet;

import java.math.BigDecimal;

public interface CustomerOutletService {

    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerId);

    public UfsCustomerOutlet findByOutletId(Long id);
}
