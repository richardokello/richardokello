package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.UfsCustomerOwners;

import java.util.List;

public interface CustomerOwnerService {

    UfsCustomerOwners findByCustomerOwnerId(Long customerOwnerId);

    List<UfsCustomerOwners> findByCustomerId(Long customerId);
}
