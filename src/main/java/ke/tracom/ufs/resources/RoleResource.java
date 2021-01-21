/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.ErrorList;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import ke.tracom.ufs.entities.*;
import ke.tracom.ufs.repositories.RolePermissionRepository;
import ke.tracom.ufs.repositories.UfsRoleRepository;
import ke.tracom.ufs.services.RolesService;
import ke.tracom.ufs.wrappers.LogExtras;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * @author eli.muraya
 */
@RestController
@RequestMapping("/role")
public class RoleResource extends ChasisResource<UfsRole, Long, UfsEdittedRecord> {

    private final RolePermissionRepository permRepository;
    private final UfsRoleRepository mlkRoleRepository;
    private final RolesService rolesService;
    private final LogExtras logExtras;

    public RoleResource(LoggerService loggerService, EntityManager entityManager, RolePermissionRepository permRepository, UfsRoleRepository mlkRoleRepository, RolesService rolesService,
                        LogExtras logExtras) {
        super(loggerService, entityManager);
        this.permRepository = permRepository;
        this.mlkRoleRepository = mlkRoleRepository;
        this.rolesService = rolesService;
        this.logExtras = logExtras;
    }

    /**
     * Used to process role permission collection
     *
     * @param t
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsRole>> create(@Valid @RequestBody UfsRole t) {
        ResponseEntity<ResponseWrapper<UfsRole>> response = super.create(t);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED)) || (t.getPermissions() == null) || t.getPermissions().isEmpty()) {
            return response;
        }

        List<UfsRolePermission> rolePermissions = new ArrayList<>();
        t.getPermissions().forEach(p -> {
            rolePermissions.add(new UfsRolePermission(p,t.getRoleId(),AppConstants.NO));
        });
        this.permRepository.saveAll(rolePermissions);
        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        for (Long id : actions.getIds()) {
            Optional<UfsRole> t = mlkRoleRepository.findById(id);
            if (t.isPresent()) {
                String action = t.get().getAction();
                String actionStatus = t.get().getActionStatus();
                if (action.equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    try {
                        this.processApproveChange(id, t.get(), actions.getNotes());
                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return super.approveActions(actions);
    }

    private void processApproveChange(Long id, UfsRole entity, String notes) throws ExpectationFailed, IOException, IllegalAccessException {
        entity = supportRepo.mergeChanges(id, entity);
        rolesService.deleteRolePermissions(this.rolesService.findByRole(entity));
        List<UfsRolePermission> rolePermissions = new ArrayList<>();
        UfsRole finalEntity = entity;

        entity.getPermissions().forEach(p -> {
            rolePermissions.add(new UfsRolePermission(p, finalEntity.getRoleId(), AppConstants.NO));
        });
        this.permRepository.saveAll(rolePermissions);
    }



    @RequestMapping("/permissions/{id}")
    public ResponseEntity<ResponseWrapper> permissions(@PathVariable("id") Long id) {
        ResponseWrapper response = new ResponseWrapper();
        List<Object> RolezPermz = new ArrayList();


        List<UfsRolePermission> RolesPerm = permRepository.findAllByroleId(id);

        if (RolesPerm.isEmpty()) {

            response.setData(RolezPermz);
            return ResponseEntity.ok(response);
        }
        RolesPerm.forEach(permission -> {

            Map<String, Object> p = new HashMap<>();
            p.put("permissionId", permission.getPermission().getEntityPermissionId());
            p.put("permissionName", permission.getPermission().getPermission());

            RolezPermz.add(p);
        });


        response.setData(RolezPermz);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Deleting A Role")
    @RequestMapping(value = "/delete-action", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 207,message = "Some records could not be processed successfully" )})
    @Transactional
    public ResponseEntity<ResponseWrapper> deleteRole(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();

        for(Long id:actions.getIds()){
            UfsRole role = rolesService.findRoleById(id);
            if(Objects.isNull(role)){
                this.loggerService.log("Failed to delete role.Record has unapproved actions", UfsRole.class.getSimpleName(), null, "Deletion", "Failed", "");
                errors.add("Role with id " + id + " doesn't exist");
            }else if(!role.getActionStatus().isEmpty() && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                this.loggerService.log("Failed to delete role. Record has unapproved actions", UfsRole.class.getSimpleName(), id, "Deletion", "Failed", "");
                errors.add("Record has unapproved actions");

            }else{
                role.setAction(AppConstants.ACTIVITY_DELETE);
                role.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                rolesService.saveRole(role);
                this.loggerService.log(role.getRoleName()+" Deleted Successfully by "+logExtras.getFullName(), UfsRole.class.getSimpleName(), id, "Deletion", "Completed", "");
            }

        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Some Actions could not be processed successfully check audit logs for more details");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }


}
