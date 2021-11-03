/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsEntity;
import ke.co.tra.ufs.tms.entities.UfsEntityPermission;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.entities.UfsRolePermissionMap;
import ke.co.tra.ufs.tms.entities.UfsUserRole;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.RoleFilters;
import ke.co.tra.ufs.tms.entities.wrappers.filters.UserFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.RolesService;
import ke.co.tra.ufs.tms.service.SupportService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.CustomEntry;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import ke.co.tra.ufs.tms.utils.exports.CsvFlexView;
import ke.co.tra.ufs.tms.utils.exports.ExcelFlexView;
import ke.co.tra.ufs.tms.utils.exports.PdfFlexView;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 *
 * @author Owori Juma
 */
@RestController
@RequestMapping("/roles")
@Api(value = "Roles Management")
public class RolesResource {

    private final RolesService rolesService;
    private final LoggerServiceLocal loggerService;
    private final SupportRepository supportRepo;
    private final SupportService supportService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public RolesResource(RolesService rolesService, LoggerServiceLocal loggerService,
            SupportRepository supportRepo, SupportService supportService) {
        this.rolesService = rolesService;
        this.loggerService = loggerService;
        this.supportRepo = supportRepo;
        this.supportRepo.setMapping(UfsUserRole.class);
        this.supportService = supportService;
    }

    @ApiOperation(value = "Fetch Entities", notes = "Used to fetch system entities")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(value = "/entities", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsEntity>>> fetchEntities(Pageable pg) {
//        loggerService.logRead("Fetching CMS Entities", AppConstants.ENTITY);
        ResponseWrapper response = new ResponseWrapper();
        response.setData(rolesService.fetchEntities(pg));
//        loggerService.logRead("Done Fetching CMS Entities Outputting Results", AppConstants.ENTITY);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Entity Permission", notes = "Used to fetch system entity permission")
    @RequestMapping(value = "/entities/{entityId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsEntityPermission>>> fetchEntityPermissions(@PathVariable("entityId") Long entityId,
            Pageable pg) {
//        loggerService.logRead("Fetching Entity(Id: " + entityId + ") Permissions", AppConstants.ENTITY);
        ResponseWrapper response = new ResponseWrapper();
        response.setData(rolesService.fetchEntityPermission(new UfsEntity(BigDecimal.valueOf(entityId)), pg));
//        loggerService.logRead("Done Fetching Entity(Id: " + entityId + ") Permissions", AppConstants.ENTITY);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Add Role", notes = "Used to add a new user role")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Role with similar name exists")
        ,
        @ApiResponse(code = 201, message = "Created successfully")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsUserRole>> addRole(@RequestBody @ApiParam(value = "Ignore id it will be used later when fetching role")
            @Valid UfsUserRole role, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            response.setMessage("Sorry validation error(s) occured now");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else if (role.getRolePermissions() == null || role.getRolePermissions().isEmpty()) {
            response.setCode(400);
            response.setData(new CustomEntry("rolePermissions", "rolePermissions is required and cannot be empty"));
            response.setMessage("Sorry validation error(s) occured");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        //check if role exists
        UfsUserRole dbRole = rolesService.fetchRole(role.getRoleName());
        if (dbRole != null) {
            loggerService.logCreate("Adding new user role (" + role.getRoleName()
                    + ") failed due to role name conflict", SharedMethods.getEntityName(UfsUserRole.class),
                    role.getRoleId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Sorry role with similar name exists");
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        role.setRoleId(null);
        role.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        role.setAction(AppConstants.ACTIVITY_CREATE);
        role.setIntrash(AppConstants.NO);
        role = rolesService.saveRole(role);

        //process role permissions
        List<UfsRolePermissionMap> permissions = new ArrayList<>();
        for (BigDecimal permission : role.getRolePermissions()) {
            permissions.add(new UfsRolePermissionMap(new UfsEntityPermission(permission), role, AppConstants.NO));
        }
        role.setPermissionMaps(permissions);
        role = rolesService.saveRole(role);
        response.setData(role);

        loggerService.logCreate("Done Adding new user role (" + role.getRoleName()
                + ")", SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Approve Roles", notes = "Used to approve All role actions (delete, update and new record)")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles could not be approved")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        boolean hasErrors = false;
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to approve role (Role Id: "
                        + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                hasErrors = true;
                continue;
            }
            if (loggerService.isInitiator(SharedMethods.getEntityName(UfsUserRole.class), id, role.getAction())) {
                hasErrors = true;
                loggerService.logUpdate("Failed to approve user role (" + role.getRoleName() + "). You can't approve/decline your own record", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                if (!this.processApproveNew(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                if (!this.processApproveChanges(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DEACTIVATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processApproveDeactivation(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processApproveDeletion(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else {
                loggerService.logUpdate("Failed to approve role (" + role.getRoleName() + "). Role doesn't have approve actions", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                hasErrors = true;
            }
        }

        if (hasErrors) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some Actions could not be processed successfully check audit logs for more details");
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Approve new records
     *
     * @param role
     * @param notes
     * @return
     */
    private boolean processApproveNew(UfsUserRole role, String notes) {
        role.setActionStatus(AppConstants.STATUS_APPROVED);
        loggerService.logApprove("Approving new TMS User (" + role.getRoleName() + ")",
                SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Approve edit changes
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processApproveChanges(UfsUserRole role, String notes) {
        try {
            supportRepo.setMapping(UfsUserRole.class);
            role = (UfsUserRole) supportRepo.mergeChanges(role.getRoleId(), UfsModifiedRecord.class);
            if (role == null) {
                return false;
            }
//            supportService.getRecords(UfsRolePermissionMap.class.getSimpleName(), "\"roleId\":" + role.getRoleId()).forEach(record -> {
//                supportService.delete(record);
//            });
            role.setActionStatus(AppConstants.STATUS_APPROVED);
            this.rolesService.saveRole(role);
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
//        citAgentService.saveCitAgent(citAgent);
        loggerService.logApprove("Done approving Role (" + role.getRoleName() + ") changes", SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId().toString(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Approve Deactivation
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processApproveDeactivation(UfsUserRole role, String notes) {
        role.setActionStatus(AppConstants.STATUS_APPROVED);

//        citAgentService.saveCitAgent(citAgent);
        loggerService.logApprove("Approved Role deactivation (" + role.getRoleName() + ") successfully", SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Approve Deletion
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processApproveDeletion(UfsUserRole role, String notes) {
        try {
            rolesService.trashRole(role.getRoleId());
            loggerService.logApprove("Done approving user role (" + role.getRoleName() + ") deletion.", SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(),
                    AppConstants.STATUS_COMPLETED, notes);
        } catch (NotFoundException ex) {
            loggerService.logApprove("Failed to approve user role (" + role.getRoleName() + ") deletion. "
                    + "Role doesn't exist",
                    SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_FAILED, notes);
            return false;
        }
        return true;
    }

    @ApiOperation(value = "Decline Role", notes = "Used to decline All role actions (delete, update and new record)")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles could not be declined")
    })
    @RequestMapping(value = "/decline-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        boolean hasErrors = false;
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to decline role (Role Id: " + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                hasErrors = true;
                continue;
            }
            if (loggerService.isInitiator(SharedMethods.getEntityName(UfsUserRole.class), id, role.getAction())) {
                hasErrors = true;
                loggerService.logUpdate("Failed to decline user role (" + role.getRoleName() + "). You can't approve/decline your own record", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                if (!this.processDeclineNew(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                if (!this.processDeclineChanges(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DEACTIVATE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processDeclineDeactivation(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (role.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                    && role.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processDeclineDeletion(role, action.getNotes())) {
                    hasErrors = true;
                }
            } else {
                hasErrors = true;
                loggerService.logUpdate("Failed to decline role (" + role.getRoleName() + "). Role doesn't have decline actions",
                        SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_FAILED, action.getNotes());
            }
        }

        if (hasErrors) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some Actions could not be processed successfully check audit logs for more details");
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Decline new record
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processDeclineNew(UfsUserRole role, String notes) {
        role.setActionStatus(AppConstants.STATUS_DECLINED);
        loggerService.logApprove("Declined Role (" + role.getRoleName() + ") successfully",
                SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Decline changes
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processDeclineChanges(UfsUserRole role, String notes) {
        supportRepo.setMapping(UfsUserRole.class);
        role = (UfsUserRole) supportRepo.declineChanges(role.getRoleId(), UfsModifiedRecord.class);
        role.setActionStatus(AppConstants.STATUS_DECLINED);

        loggerService.logApprove("Done Declining Role (" + role.getRoleName() + ") changes",
                SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId().toString(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Decline Deactivation
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processDeclineDeactivation(UfsUserRole role, String notes) {
        role.setActionStatus(AppConstants.STATUS_DECLINED);
//        citAgentService.saveCitAgent(citAgent);
        loggerService.logApprove("Approved Role deactivation (" + role.getRoleName() + ") successfully", SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    /**
     * Decline deletion
     *
     * @param citAgent
     * @param notes
     * @return
     */
    private boolean processDeclineDeletion(UfsUserRole role, String notes) {
        role.setActionStatus(AppConstants.STATUS_DECLINED);
        loggerService.logApprove("Done declining role (" + role.getRoleName() + ") deletion",
                SharedMethods.getEntityName(UfsUserRole.class), role.getRoleId(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    @ApiOperation(value = "Update Role", notes = "Used to update user role")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Role not found")
        ,
        @ApiResponse(code = 409, message = "Role with similar name exists")
        ,
        @ApiResponse(code = 417, message = "If current object has not been modified")
    })
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsUserRole>> updateRole(@RequestBody
            @Valid UfsUserRole role, BindingResult validation) throws IllegalAccessException, JsonProcessingException {
        String entityName = SharedMethods.getEntityName(UfsUserRole.class);
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            response.setMessage("Sorry validation error(s) occured");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        //check if role with similar name exists
        UfsUserRole dbRole = rolesService.fetchRoleButNot(role.getRoleName(), role.getRoleId());
        if (dbRole != null) {
            loggerService.logUpdate("Updating user role (" + role.getRoleName()
                    + ") failed due to role name conflict", entityName, role.getRoleId().toString(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Sorry role with similar name exists");
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        dbRole = rolesService.fetchRole(role.getRoleId());
        if (dbRole == null) {
            loggerService.logUpdate("Updating user role (" + role.getRoleName()
                    + ") failed due to role not found", entityName, role.getRoleId().toString(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry role could not found");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        if (dbRole.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry the record has pending Unapproved actions");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        }

        if (role.getRolePermissions() != null) {
            List<UfsRolePermissionMap> perms = new ArrayList<>();
            for (BigDecimal p : role.getRolePermissions()) {
                UfsRolePermissionMap permMap;
                permMap = new UfsRolePermissionMap(new UfsEntityPermission(p), dbRole, AppConstants.NO);
                perms.add(permMap);
            }
            org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
            mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            log.debug("Removing {} role permissions ", dbRole.getPermissionMaps().size());
            
            
            for (UfsRolePermissionMap perm : dbRole.getPermissionMaps()) {
                try {
                    log.debug("Wrote role permission as string {}", mapper.writeValueAsString(perm));
                    supportService.saveEditedChanges(new UfsModifiedRecord(perm.getClass().getSimpleName(),
                            mapper.writeValueAsString(perm), perm.getRolePermMapId().toString()));
                    rolesService.deletePermissionMapPermanently(perm.getRolePermMapId());
                } catch (IOException ex) {
                    log.error(AppConstants.AUDIT_LOG, "Encountered an error while processing role permissions", ex);
                }
            }
            rolesService.saveRolePermissions(perms);
        }

        dbRole.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        dbRole.setAction(AppConstants.ACTIVITY_UPDATE);
//        --
        supportRepo.setMapping(UfsUserRole.class);
        if (supportRepo.handleEditRequest(role, UfsModifiedRecord.class) == false) {
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry user role has not been changed");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        } else {
            response.setData(role);
        }
        loggerService.logUpdate("Done Adding new user role (" + role.getRoleName() + ")",
                entityName, role.getRoleId().toString(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Role Changes", notes = "Used to fetch role changes")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Failed to locate role with the specified id")
        ,
        @ApiResponse(code = 304, message = "No changes found")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/changes")
    public ResponseEntity<ResponseWrapper<List<String>>> viewChanges(@PathVariable("id") BigDecimal id) throws IllegalAccessException, IOException {
        ResponseWrapper<List<String>> response = new ResponseWrapper();
        UfsUserRole oldEntity = rolesService.fetchRole(id);
        if (oldEntity == null) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry CIT AgentWrapper doesn't exist");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        List<String> changes = new ArrayList<>();

        supportRepo.setMapping(UfsUserRole.class);
        UfsUserRole updateEntity = (UfsUserRole) supportRepo._fetchChanges(id, UfsModifiedRecord.class);
        if (updateEntity != null) {
            String chg = supportRepo.fetchStringChanges(oldEntity, updateEntity);
            if (!chg.isEmpty()) {
                changes.add(chg);
            }
        }
        response.setData(changes);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Deprecated
    public ResponseEntity<ResponseWrapper<Map>> viewChanges(@PathVariable("roleId") Long roleId) throws IllegalAccessException, IOException {
//        loggerService.logRead("Fetching user role (" + roleId + ") changes", AppConstants.ENTITY_ROLE);
        ResponseWrapper response = new ResponseWrapper();
        UfsUserRole olderRole = rolesService.fetchRole(BigDecimal.valueOf(roleId));
        if (olderRole == null) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry role doesn't exist");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        supportRepo.setMapping(UfsUserRole.class);
        System.out.println("Old Bean: " + olderRole.getClass());
        UfsUserRole updateRole = (UfsUserRole) supportRepo._fetchChanges(BigDecimal.valueOf(roleId), UfsModifiedRecord.class);
        if (updateRole == null) {
            response.setMessage("No changes found");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        response.setData(supportRepo.fetchChanges(olderRole, updateRole));
//        loggerService.logRead("Done fetching user role (" + roleId + ") changes", AppConstants.ENTITY_ROLE);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Approve Role Changes", notes = "Used to approve user role changes")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Some Role(s) don't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-changes")
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper<UfsUserRole>> approveChanges(@RequestBody @Valid ActionWrapper<BigDecimal> action/*ArrayList<Long> roleIds, 
            @RequestParam("notes") String notes*/) throws IOException, IllegalArgumentException, IllegalAccessException {
        ResponseWrapper response = new ResponseWrapper();
        supportRepo.setMapping(UfsUserRole.class);
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal roleId : action.getIds()) {
            loggerService.logUpdate("Approving user role (role id: " + roleId + ") changes",
                    SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_UNAPPROVED, action.getNotes());
            UfsUserRole role = (UfsUserRole) supportRepo.mergeChanges(roleId, UfsModifiedRecord.class);
            if (role == null) {
                loggerService.logUpdate(" Role  (role id: " + roleId + ") could not be located"
                        + " OR doesn't have changes ",
                        SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(roleId);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_UPDATE);
            role.setActionStatus(AppConstants.STATUS_APPROVED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Done approving user role (role id: " + roleId + ") changes",
                    SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_UNAPPROVED, action.getNotes());

        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located or doesn't have changes " + errors);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.OK);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Decline Role Changes", notes = "Used to decline user role changes")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Some Role(s) don't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/decline-changes")
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper<UfsUserRole>> declineChanges(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        supportRepo.setMapping(UfsUserRole.class);
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal roleId : action.getIds()) {
            loggerService.logUpdate("Declining user role (role id: " + roleId + ") changes",
                    SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_PENDING, action.getNotes());
            UfsUserRole role = (UfsUserRole) supportRepo.declineChanges(roleId, UfsModifiedRecord.class);
            if (role == null) {
                loggerService.logUpdate("User role {role id: " + roleId + " could not be located",
                        SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(roleId);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_UPDATE);
            role.setActionStatus(AppConstants.STATUS_DECLINED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Done Declining user role (role id: " + roleId + ") changes",
                    SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_UNAPPROVED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Approve Roles", notes = "Used to approve a new user roles")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles were declined")
    })
    @RequestMapping(value = "/approve", method = RequestMethod.PUT)
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> approveRole(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logUpdate("Approving new user role (role id: " + id + ")",
                    SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_UNAPPROVED, action.getNotes());
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to approve new user role (role id: "
                        + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_APPROVE);
            role.setActionStatus(AppConstants.STATUS_APPROVED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Approved new user role (role id: " + id + ")",
                    SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Roles", notes = "Used to fetch roles")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ResponseWrapper<UfsUserRole>>> getRoles(Pageable pg,
            @Valid RoleFilters filter) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(rolesService.fetchRoles(filter.getActionStatus(), filter.getFrom(),
                filter.getTo(), filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Export Roles to PDF")
    @RequestMapping(method = RequestMethod.GET, value = "/export.pdf")
    public ModelAndView exportRolesPdf(Pageable pg, @Valid UserFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "GAB Roles";
        String title = "USERS";
        List<UfsUserRole> result = rolesService.fetchRoles(filter.getActionStatus(), filter.getFrom(),
                filter.getTo(), filter.getNeedle(), pg).getContent();
        PdfFlexView view = new PdfFlexView(UfsUserRole.class, result,
                fileName, title);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Export Roles to Excel")
    @RequestMapping(method = RequestMethod.GET, value = "/export.xls")
    public ModelAndView exportRolesXls(Pageable pg, @Valid UserFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "GAB Roles";
        List<UfsUserRole> result = rolesService.fetchRoles(filter.getActionStatus(), filter.getFrom(),
                filter.getTo(), filter.getNeedle(), pg).getContent();
        ExcelFlexView view = new ExcelFlexView(UfsUserRole.class, result, fileName);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Export Roles to CSV")
    @RequestMapping(method = RequestMethod.GET, value = "/export.csv")
    public ModelAndView exportRolesCsv(Pageable pg, @Valid UserFilter filter) {
        pg = new PageRequest(0, Integer.MAX_VALUE, pg.getSort());
        String fileName = "GAB Roles";
        List<UfsUserRole> result = rolesService.fetchRoles(filter.getActionStatus(), filter.getFrom(),
                filter.getTo(), filter.getNeedle(), pg).getContent();
        CsvFlexView view = new CsvFlexView(UfsUserRole.class, result, fileName);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Fetch Role", notes = "Fetch role by id")
    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<UfsUserRole>> getRole(@PathVariable("roleId") Long roleId) {
//        loggerService.logRead("Fetching role (id " + roleId + ")", AppConstants.ENTITY_ROLE);
        ResponseWrapper response = new ResponseWrapper();
        response.setData(rolesService.fetchRole(BigDecimal.valueOf(roleId)));
//        loggerService.logRead("Done Fetching role (id " + roleId + ")", AppConstants.ENTITY_ROLE);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Decline Roles", notes = "Used to decline roles")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles were not declined")
    })
    @RequestMapping(value = "/decline", method = RequestMethod.PUT)
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> declineRole(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to decline user role (role id: " + id + "). Role doesn't exist",
                        SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DECLINE);
//            role.setActionStatus(AppConstants.STATUS_COMPLETED);
//            role.setIntrash(AppConstants.YES);
            rolesService.saveRole(role);
            loggerService.logUpdate("Declined user role (role id: " + id + ") successfully",
                    SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Deactivate Roles", notes = "")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles were not deactivated")
    })
    @RequestMapping(value = "/deactivate", method = RequestMethod.PUT)
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> deactivateRole(@RequestBody ActionWrapper<BigDecimal> action
    ) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to deactivate user role (role id: "
                        + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DEACTIVATE);
            role.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Deactivated user role (role id: " + id + ") successfully",
                    SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Approve Roles Deactivation", notes = "")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles were not approved")
    })
    @RequestMapping(value = "/approve-deactivation", method = RequestMethod.PUT)
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> approveRoleDeactivation(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to approve  user role deactivation (role id: "
                        + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DEACTIVATE);
            role.setActionStatus(AppConstants.STATUS_APPROVED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Approved user role deactivation (role id: "
                    + id + ") successfully", SharedMethods.getEntityName(UfsUserRole.class), id,
                    AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Decline Roles Deactivation", notes = "")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Returned when some roles were not declined")
    })
    @RequestMapping(value = "/decline-deactivation", method = RequestMethod.PUT)
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> declineRoleDeactivation(@RequestBody
            @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(id);
            if (role == null) {
                loggerService.logUpdate("Failed to approve  user role deactivation (role id: "
                        + id + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), id,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DEACTIVATE);
            role.setActionStatus(AppConstants.STATUS_DECLINED);
            rolesService.saveRole(role);
            loggerService.logUpdate("Approved user role deactivation (role id: "
                    + id + ") successfully", SharedMethods.getEntityName(UfsUserRole.class), id,
                    AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete User Role", notes = "Used to delete user role")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Some Role(s) don't exist")
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<ResponseWrapper> deleteRole(@RequestBody @Valid ActionWrapper<BigDecimal> action) throws IOException, IllegalArgumentException, IllegalAccessException {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal roleId : action.getIds()) {
            UfsUserRole role = rolesService.fetchRole(roleId);
            if (role == null) {
                loggerService.logDelete("Failed to delete user role (role id: "
                        + roleId + "). Role doesn't exist", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(roleId);
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DELETE);
            role.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            loggerService.logDelete("Done deleting user role (" + role.getRoleName() + ")",
                    SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Approve User Role Deletion", notes = "Used to approve user role delete request")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Role doesn't exist")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/approve")
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> approveRoleDelete(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal roleId : action.getIds()) {
            loggerService.logUpdate("Approving user role (role id: " + roleId + ") deletion", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                    AppConstants.STATUS_PENDING, action.getNotes());
            try {
                rolesService.trashRole(roleId);
                loggerService.logUpdate("Done approving user role (role id: " + roleId + ") deletion.", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                        AppConstants.STATUS_COMPLETED, action.getNotes());
            } catch (NotFoundException ex) {
                errors.add(roleId);
                loggerService.logUpdate("Failed to approve user role (role id " + roleId + ") deletion. "
                        + "Role doesn't exist",
                        SharedMethods.getEntityName(UfsUserRole.class), roleId, AppConstants.STATUS_FAILED, action.getNotes());
            }
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Decline User Role Deletion", notes = "Used to decline user role delete request")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Role doesn't exist")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/decline")
    @Deprecated
    @ApiIgnore
    public ResponseEntity<ResponseWrapper> declineRoleDelete(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal roleId : action.getIds()) {
            loggerService.logUpdate("Declining user role (role id: " + roleId + ") deletion", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                    AppConstants.STATUS_UNAPPROVED, action.getNotes());
            UfsUserRole role = rolesService.fetchRole(roleId);
            if (role == null) {
                errors.add(roleId);
                loggerService.logUpdate("Failed to decline user role (role id: "
                        + roleId + "). Role doesn't exist.", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                        AppConstants.STATUS_UNAPPROVED, action.getNotes());
                continue;
            }
            role.setAction(AppConstants.ACTIVITY_DELETE);
            role.setActionStatus(AppConstants.STATUS_DECLINED);
            loggerService.logUpdate("Done declining user role (role id: " + roleId + ") deletion", SharedMethods.getEntityName(UfsUserRole.class), roleId,
                    AppConstants.STATUS_UNAPPROVED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Some role id(s) could not be located: " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Search Roles", notes = "Used to search roles")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    @Deprecated
    @ApiIgnore
    public ResponseEntity<Page<ResponseWrapper<UfsUserRole>>> searchRoles(Pageable pg, @RequestParam("needle") String needle) {
//        loggerService.logRead("Searching roles", AppConstants.ENTITY_ROLE, AppConstants.STATUS_UNAPPROVED);
        ResponseWrapper response = new ResponseWrapper();
        response.setData(rolesService.searchRoles(needle, pg));
//        loggerService.logRead("Done Searching roles", AppConstants.ENTITY_ROLE, AppConstants.STATUS_UNAPPROVED);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
