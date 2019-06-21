package ke.tra.ufs.webportal.resources;

import com.cm.projects.spring.resource.chasis.ChasisResource;
import com.cm.projects.spring.resource.chasis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsBankBranches;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value = "/bank-branches")
public class BankBranchesResources extends ChasisResource<UfsBankBranches, Long, UfsEdittedRecord> {
    public BankBranchesResources(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
