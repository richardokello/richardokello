package ke.tra.ufs.webportal.resources;


import com.cm.projects.spring.resource.chasis.ChasisResource;
import com.cm.projects.spring.resource.chasis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsGls;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value = "/gls")
public class UfsGlsResource extends ChasisResource<UfsGls,Long, UfsEdittedRecord> {

    public UfsGlsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
