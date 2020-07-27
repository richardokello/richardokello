package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.repository.UfsContactPersonRepository;
import ke.tra.ufs.webportal.service.ContactPersonService;
import org.springframework.stereotype.Service;


@Service
public class ContactPersonServiceTemplate implements ContactPersonService {

    private final UfsContactPersonRepository contactPersonRepository;

    public ContactPersonServiceTemplate(UfsContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public UfsContactPerson saveContactPerson(UfsContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }
}
