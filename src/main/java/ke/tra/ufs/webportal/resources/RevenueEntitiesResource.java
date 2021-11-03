/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEntity;
import ke.tra.ufs.webportal.entities.UfsRevenueEntities;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;

/**
 *
 * @author Kenny
 */
@Controller
@RequestMapping(value = "/revenue-entities")
public class RevenueEntitiesResource extends ChasisResource<UfsRevenueEntities, Long, UfsEntity> {

    public RevenueEntitiesResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
    
}
