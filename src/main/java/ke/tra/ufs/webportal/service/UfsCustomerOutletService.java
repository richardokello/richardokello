package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UfsCustomerOutletService {

    UfsCustomerOutlet findByCustomerId(BigDecimal customerId);

    UfsCustomerOutlet findById(Long outletId);

    UfsCustomerOutlet saveOutlet(UfsCustomerOutlet outlet);

    public UfsCustomerOutlet findByOutletId(Long id);

    List<UfsCustomerOutlet> findByCustomerId(BigDecimal customerId, String intrash);

    List<UfsCustomerOutlet> findByCustomerIdIn(List<BigDecimal> customerId, String intrash);

    void deleteByCustomerId(BigDecimal customerId);

    public Page<UfsCustomerOutlet> getOutletByCustomerId(String actionStatus, String customerIds, Date from, Date to, String needle, Pageable pg);
}
