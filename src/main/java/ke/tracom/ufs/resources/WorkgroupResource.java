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
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsWorkgroup;
import ke.tracom.ufs.entities.UfsWorkgroupRole;
import ke.tracom.ufs.repositories.UfsWorkgroupRoleRepository;
import ke.tracom.ufs.repositories.WorkgroupRepository;
import ke.tracom.ufs.services.WorkGroupService;
import ke.tracom.ufs.wrappers.LogExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * @author Cornelius M
 * @edits_by eli
 */
@Controller
@RequestMapping("/workgroup")
public class WorkgroupResource extends ChasisResource<UfsWorkgroup, Long, UfsEdittedRecord> {

    @Autowired
    private WorkgroupRepository workgroupRepo;
    @Autowired
    private UfsWorkgroupRoleRepository workgroupRoleRepository;

    private final WorkGroupService workGroupService;
    private final LogExtras logExtras;

    public WorkgroupResource(LoggerService loggerService, EntityManager entityManager, WorkGroupService workGroupService, LogExtras logExtras) {
        super(loggerService, entityManager);
        this.workGroupService = workGroupService;
        this.logExtras = logExtras;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsWorkgroup>> create(@Valid @RequestBody UfsWorkgroup mlkWorkgroup) {
        ResponseEntity<ResponseWrapper<UfsWorkgroup>> response = super.create(mlkWorkgroup);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED)) || (mlkWorkgroup.getWorkgroupRolesIds() == null) || mlkWorkgroup.getWorkgroupRolesIds().isEmpty()) {
            return response;
        }

        List<UfsWorkgroupRole> workgroupRoles = new ArrayList<>();
        mlkWorkgroup.getWorkgroupRolesIds().forEach(id -> {
            workgroupRoles.add(new UfsWorkgroupRole(mlkWorkgroup.getGroupId(), id));
        });
        workgroupRoleRepository.saveAll(workgroupRoles);

        return response;
    }

    @RequestMapping("/roles/{id}")
    public ResponseEntity<ResponseWrapper> roles(@PathVariable("id") Long id) {
        ResponseWrapper response = new ResponseWrapper();
        List<Object> workgRoles = new ArrayList();


        List<UfsWorkgroupRole> workgroupRoles = workgroupRoleRepository.findAllByGroupId(id);

        if (workgroupRoles.isEmpty()) {

            response.setData(workgRoles);
            return ResponseEntity.ok(response);
        }
        workgroupRoles.forEach(role -> {

            Map<String, Object> r = new HashMap<>();
            r.put("roleId", role.getRole().getRoleId());
            r.put("roleName", role.getRole().getRoleName());

            workgRoles.add(r);
        });


        response.setData(workgRoles);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {

        for (Long id : actions.getIds()) {
            Optional<UfsWorkgroup> t = workgroupRepo.findById(id);
            if (t.isPresent()) {
                if (t.get().getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && t.get().getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    try {

                        //getting the roleIds from editted record
                        UfsWorkgroup entity = supportRepo.mergeChanges(id, t.get());
                        List<Long> existingIds = new ArrayList<>();
                        List<Long> newIds = entity.getWorkgroupRolesIds();

                        //Getting existing roleIds from the database
                        workgroupRoleRepository.findAllByGroupId(id).forEach(item -> {
                            existingIds.add(item.getRoleId());
                        });

                        List<Long> isPresent = new ArrayList<>();
                        List<Long> toDelete = new ArrayList<>();
                        List<Long> toCreate = new ArrayList<>();

                        existingIds.forEach(num -> {
                            //Checking if existing roleIds array exists in the new roleIds array if yes add to isPresent array if no add to isDelete array
                            if (newIds != null) {
                                if (newIds.contains(num)) {
                                    isPresent.add(num);
                                }
                                if (!newIds.contains(num)) {
                                    toDelete.add(num);
                                }
                            }
                        });

                        //add the new roleIds to toCreate array if its not in isPresent array and not present in toDelete array
                        newIds.forEach(num -> {
                            if (!isPresent.contains(num) && !toDelete.contains(num)) {
                                toCreate.add(num);
                            }
                        });

                        //delete the roleWorkgroups if the toDelete array is not null
                        if (!toDelete.isEmpty()) {
                            List<UfsWorkgroupRole> waitingDeletion = workgroupRoleRepository.findAllByGroupIdAndAndRoleIdIn(id, toDelete);
                            workgroupRoleRepository.deleteAll(waitingDeletion);
                        }

                        //create the roleWorkgroups if the toCreate array is not null

                        if (!toCreate.isEmpty()) {
                            List<UfsWorkgroupRole> items = new ArrayList<>();

                            toCreate.forEach(isicgroupId -> {
                                UfsWorkgroupRole item = new UfsWorkgroupRole();
                                item.setGroupId(id);
                                item.setRoleId(isicgroupId);
                                item.setIntrash(AppConstants.NO);
                                items.add(item);
                            });

                            workgroupRoleRepository.saveAll(items);
                        }

                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


        return super.approveActions(actions);
    }

    @ApiOperation(value = "Deleting A Workgroup")
    @RequestMapping(value = "/delete-action", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 207, message = "Some records could not be processed successfully")})
    @Transactional
    public ResponseEntity<ResponseWrapper> deleteWorkgroup(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();

        for (Long id : actions.getIds()) {
            UfsWorkgroup workgroup = workGroupService.findWorkgroupById(id);
            if (Objects.isNull(workgroup)) {
                this.loggerService.log("Failed to delete workgroup.Record has unapproved actions", UfsWorkgroup.class.getSimpleName(), null, "Deletion", "Failed", "");
                errors.add("Workgroup with id " + id + " doesn't exist");
            } else if (!workgroup.getActionStatus().isEmpty() && workgroup.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                this.loggerService.log("Failed to delete workgroup. Record has unapproved actions", UfsWorkgroup.class.getSimpleName(), id, "Deletion", "Failed", "");
                errors.add("Record has unapproved actions");

            } else {
                workgroup.setAction(AppConstants.ACTIVITY_DELETE);
                workgroup.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                workGroupService.saveWorkgroup(workgroup);
                this.loggerService.log(workgroup.getGroupName() + " Deleted Successfully by " + logExtras.getFullName(), UfsWorkgroup.class.getSimpleName(), id, "Deletion", "Completed", "");
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
