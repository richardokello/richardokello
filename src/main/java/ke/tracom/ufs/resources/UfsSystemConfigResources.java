/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsSysConfig;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

/**
 * @author emuraya
 */
@RestController
@RequestMapping("/system-config")
public class UfsSystemConfigResources extends ChasisResource<UfsSysConfig, Long, UfsEdittedRecord> {


    public UfsSystemConfigResources(LoggerServiceTemplate loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsSysConfig>> create(@Valid UfsSysConfig ufsSysConfig) {
        throw new UnsupportedOperationException("Not Allowed");
    }

    @Override
    public ResponseEntity<ResponseWrapper> deleteEntity(@Valid ActionWrapper<Long> actions) {
        throw new UnsupportedOperationException("Not Allowed");
    }
}
