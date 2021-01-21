/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.tracom.ufs.entities.UfsAuthenticationType;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 *
 * @author emuraya
 */
@Controller
@RequestMapping(value = "/auth-types")
public class UfsAuthTypesResource extends ChasisResource<UfsAuthenticationType, BigDecimal, UfsEdittedRecord> {

    public UfsAuthTypesResource(LoggerServiceTemplate loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
