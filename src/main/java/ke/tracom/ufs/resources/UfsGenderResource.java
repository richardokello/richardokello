/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsGender;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

/**
 *
 * @author emuraya
 */
@RestController
@RequestMapping("/gender")
public class UfsGenderResource extends ChasisResource<UfsGender, Long, UfsEdittedRecord> {

    public UfsGenderResource(LoggerServiceTemplate loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
