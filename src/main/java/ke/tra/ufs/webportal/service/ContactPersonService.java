package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsContactPerson;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContactPersonService {

    UfsContactPerson saveContactPerson(UfsContactPerson contactPerson);

    Optional<UfsContactPerson> findContactPersonById(Long id);

    UfsContactPerson findContactPersonByIdAndIntrash(Long id);

    UfsContactPerson findByUsername(String  username);

    List<UfsContactPerson> getAllContactPersonByCustomerId(BigDecimal customerId);
}
