package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;

import java.math.BigDecimal;

public interface CustomerOwnersService {

    void save(CustomerOwnersCrime ownersCrime);

    UfsCustomerOwners findByCustomerIds(BigDecimal customerIds);

    void saveOwner(UfsCustomerOwners customerOwners);

}
