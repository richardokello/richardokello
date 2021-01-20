package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.UfsCustomerOwners;

public interface CustomerOwnerService {

    UfsCustomerOwners findByCustomerOwnerId(Long customerOwnerId);
}
