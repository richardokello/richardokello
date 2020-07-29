package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.repository.UfsContactPersonRepository;
import ke.tra.ufs.webportal.repository.UfsCustomerOutletRepository;
import ke.tra.ufs.webportal.service.ContactPersonService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ContactPersonServiceTemplate implements ContactPersonService {

    private final UfsContactPersonRepository contactPersonRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;

    public ContactPersonServiceTemplate(UfsContactPersonRepository contactPersonRepository,UfsCustomerOutletRepository customerOutletRepository) {
        this.contactPersonRepository = contactPersonRepository;
        this.customerOutletRepository = customerOutletRepository;
    }

    @Override
    public UfsContactPerson saveContactPerson(UfsContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public List<UfsContactPerson> getAllContactPersonByCustomerId(BigDecimal customerId) {

        List<UfsCustomerOutlet> customerOutlets =customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerId, AppConstants.NO);
        List<Long> outletIds = customerOutlets.stream().map(outlet -> outlet.getId()).collect(Collectors.toList());
        return contactPersonRepository.findByCustomerOutletIdIsInAndIntrash(outletIds,AppConstants.NO);
    }
}
