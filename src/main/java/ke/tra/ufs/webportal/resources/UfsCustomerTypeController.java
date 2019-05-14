/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.resources;

import com.cm.projects.spring.resource.chasis.ChasisResource;
import com.cm.projects.spring.resource.chasis.utils.LoggerService;
import javax.persistence.EntityManager;
import ke.tra.ufs.webportal.entities.UfsCustomerType;
import ke.tra.ufs.webportal.entities.UfsEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Kenny
 */
@Controller
@RequestMapping(value = "/customer-type")
public class UfsCustomerTypeController extends ChasisResource<UfsCustomerType, Long, UfsEntity>{

    public UfsCustomerTypeController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
    
}
