/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.resources;

import com.cm.projects.spring.resource.chasis.ChasisResource;
import com.cm.projects.spring.resource.chasis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEntity;
import ke.tra.ufs.webportal.entities.UfsTieredCommissionAmount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;

/**
 *
 * @author Kenny
 */
@Controller
@RequestMapping(value = "/tiered-commission-amount")
public class TieredCommissionAmountResource extends ChasisResource<UfsTieredCommissionAmount, Long, UfsEntity>{

    public TieredCommissionAmountResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
    
}
