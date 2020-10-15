package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsContactPerson;

import java.math.BigDecimal;
import java.util.List;

public interface ContactPersonService {

    UfsContactPerson saveContactPerson(UfsContactPerson contactPerson);

    UfsContactPerson findContactPersonById(Long id);

    UfsContactPerson findByUsername(String  username);

    List<UfsContactPerson> getAllContactPersonByCustomerId(BigDecimal customerId);
}
