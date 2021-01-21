/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsModule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

/**
 * @author eli.muraya
 */
@RestController
@RequestMapping(value = "/module")
public class ModuleResource extends ChasisResource<UfsModule, Short, UfsEdittedRecord> {

    public ModuleResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper<List<String>>> fetchChanges(Short id) throws IllegalAccessException, IOException {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processDeclineConfirmation(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processDeclineDeletion(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processDeclineChanges(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processDeclineNew(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> declineActions(ActionWrapper<Short> actions) {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processConfirm(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processApproveDeletion(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processApproveChanges(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    protected void processApproveNew(Short id, UfsModule entity, String notes, String nickName) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> approveActions(ActionWrapper<Short> actions) throws ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> deleteEntity(ActionWrapper<Short> actions) {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper<UfsModule>> updateEntity(UfsModule t) throws IllegalAccessException, JsonProcessingException, ExpectationFailed {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsModule>> getEntity(Short id) {
        return super.getEntity(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @ApiIgnore
    public ResponseEntity<ResponseWrapper<UfsModule>> create(UfsModule t) {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

}
