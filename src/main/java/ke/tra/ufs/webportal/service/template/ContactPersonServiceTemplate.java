package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.repository.UfsContactPersonRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.service.ContactPersonService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ContactPersonServiceTemplate implements ContactPersonService {

    private final UfsContactPersonRepository contactPersonRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;

    public ContactPersonServiceTemplate(UfsContactPersonRepository contactPersonRepository, UfsCustomerOutletRepository customerOutletRepository) {
        this.contactPersonRepository = contactPersonRepository;
        this.customerOutletRepository = customerOutletRepository;
    }

    @Override
    public UfsContactPerson saveContactPerson(UfsContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public Optional<UfsContactPerson> findContactPersonById(Long id) {
        return contactPersonRepository.findById(id);
    }

    @Override
    public UfsContactPerson findContactPersonByIdAndIntrash(Long id) {
        return contactPersonRepository.findByIdAndIntrash(id, AppConstants.NO);
    }

    @Override
    public UfsContactPerson findByUsername(String username) {
        return contactPersonRepository.findByUserNameAndIntrash(username, AppConstants.NO);
    }

    @Override
    public List<UfsContactPerson> getAllContactPersonByCustomerId(BigDecimal customerId) {

        List<UfsCustomerOutlet> customerOutlets = customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerId, AppConstants.NO);
        List<Long> outletIds = customerOutlets.stream().map(UfsCustomerOutlet::getId).collect(Collectors.toList());
        return contactPersonRepository.findByCustomerOutletIdIsInAndIntrash(outletIds, AppConstants.NO);
    }

    @Override
    public List<UfsContactPerson> findByUsernameIn(Set<String> usernames) {
        return contactPersonRepository.findByUserNameInAndIntrash(usernames, AppConstants.NO);
    }
}
