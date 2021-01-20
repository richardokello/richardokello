package ke.co.tra.ufs.tms.service;



import ke.co.tra.ufs.tms.entities.UfsContactPerson;

import java.math.BigDecimal;
import java.util.List;

public interface ContactPersonService {

    UfsContactPerson saveContactPerson(UfsContactPerson contactPerson);

    List<UfsContactPerson> getAllContactPersonByCustomerId(BigDecimal customerId);
}
