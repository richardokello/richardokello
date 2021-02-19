package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.UfsCustomerOutlet;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerOutletService {

    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerId);

    public UfsCustomerOutlet findByOutletId(Long id);


    List<UfsCustomerOutlet> findOutletsByCustomerIdsAndIntrash(BigDecimal customerId, String intrash);
}
