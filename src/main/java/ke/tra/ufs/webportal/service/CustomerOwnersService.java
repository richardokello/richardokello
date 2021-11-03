package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CustomerOwnersService {

    void save(CustomerOwnersCrime ownersCrime);

    UfsCustomerOwners findByCustomerIds(BigDecimal customerIds);

    UfsCustomerOwners findByUsername(String username);

    List<UfsCustomerOwners> findByUsernameIn(Set<String> username);

    List<UfsCustomerOwners> findOwnersByCustomerIds(BigDecimal customerIds);

    UfsCustomerOwners saveOwner(UfsCustomerOwners customerOwners);

    void deactivateByOwnersList(List<UfsCustomerOwners> customerOwners);


}
