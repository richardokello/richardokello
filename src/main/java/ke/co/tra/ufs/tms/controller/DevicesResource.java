package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import ke.co.tra.ufs.tms.config.messageSource.Message;
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.AssignDeviceWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.ReleaseDeviceAction;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DeviceCustomerFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DevicesFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.repository.TmsDeviceTidMidRepository;
import ke.co.tra.ufs.tms.service.*;
import ke.co.tra.ufs.tms.service.templates.PosUserServiceTemplate;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.RestTemplateClient;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Owori Juma
 */
@RestController
@RequestMapping("/devices")
@Api(value = "Devices Resource")
public class DevicesResource {

    private final DeviceService deviceService;
    private final PosUserServiceTemplate userServiceTemplate;
    private final TmsDeviceParamService deviceParamService;
    private final LoggerServiceLocal loggerService;
    private final SupportRepository supportRepo;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SchedulerService schedulerService;
    private final ProductService productService;
    private final SupportService supportService;
    private final SharedMethods sharedMethods;
    private final RestTemplateClient restTemplateClient;
    private final ObjectMapper mapper;
    @Value("${app.tid.url}")
    private String tidUrl;
    private final UfsTerminalHistoryService terminalHistoryService;
    private final PosUserService posUserService;
    private final PasswordEncoder encoder;
    private final SysConfigService configService;
    private final NotifyService notifyService;
    private final TmsDeviceTidMidRepository tidMidRepository;
    private final Message message;

    public DevicesResource(DeviceService deviceService, PosUserServiceTemplate userServiceTemplate, LoggerServiceLocal loggerService, SupportRepository supportRepo,
                           SchedulerService schedulerService, ProductService productService, SupportService supportService, SharedMethods sharedMethods, TmsDeviceParamService deviceParamService, RestTemplateClient restTemplateClient, ObjectMapper mapper,
                           UfsTerminalHistoryService terminalHistoryService, PosUserService posUserService, PasswordEncoder encoder,
                           SysConfigService configService, NotifyService notifyService, TmsDeviceTidMidRepository tidMidRepository, Message message) {
        this.deviceService = deviceService;
        this.userServiceTemplate = userServiceTemplate;
        this.loggerService = loggerService;
        this.supportRepo = supportRepo;
        this.schedulerService = schedulerService;
        this.productService = productService;
        this.supportService = supportService;
        this.sharedMethods = sharedMethods;
        this.deviceParamService = deviceParamService;
        this.restTemplateClient = restTemplateClient;
        this.mapper = mapper;
        this.terminalHistoryService = terminalHistoryService;
        this.posUserService = posUserService;
        this.encoder = encoder;
        this.configService = configService;
        this.notifyService = notifyService;
        this.tidMidRepository = tidMidRepository;
        this.message = message;
    }

    @ApiOperation(value = "Fetch Devices")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<TmsDevice>>> getDevices(Pageable pg,
                                                                       @Valid @ApiParam(value = "Entity filters and search parameters") DevicesFilter filter) {
        log.debug("Filtering devices by from {} to {} ---> {}", filter.getFrom(), filter.getTo(), filter.getAction());
        ResponseWrapper<Page<TmsDevice>> response = new ResponseWrapper();
        response.setData(deviceService.getDevices(filter.getAction(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(), filter.getStatus(), pg));
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Create Device", notes = "used to create a device within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper<TmsDevice>> createDevice(@ApiParam(value = "Ignore status and deviceId it will be used when fetching Devices")
                                                                   @RequestBody @Valid List<AssignDeviceWrapper> onboardWrapper, BindingResult validation) {
        ResponseWrapper<TmsDevice> response = new ResponseWrapper<>();
        if (validation.hasErrors()) {
            log.error("Error=>", SharedMethods.getFieldMapErrors(validation));
            response.setCode(400);
            response.setMessage(message.setMessage(AppConstants.VALIDATION_ERROR));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        log.error("Saving devices with size {}", onboardWrapper.size());
        deviceService.saveDevices(onboardWrapper, sharedMethods, loggerService);

        response.setCode(201);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Fetch Devices by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper> getDeviceById(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        Optional<TmsDevice> optional = deviceService.getDevice(id);
        if (!optional.isPresent()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND + " "+ id));

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }

        TmsDevice tmsDevice = optional.get();
        response.setData(tmsDevice);
        response.setCode(HttpStatus.OK.value());
        response.setMessage(AppConstants.SUCCESS_STRING);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/{customerId}/devices", method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Devices", notes = "Get All Devices By Customer Id.")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceHeartbeat>>> getDevicesByCustomerId(Pageable pg, @PathVariable("customerId") BigDecimal customerId, @ApiParam(value = "Entity filters and search parameters") DevicesFilter filter) {
        ResponseWrapper response = new ResponseWrapper<>();
//        response.setData(this.deviceService.getDevicesByCustomerId(customerId, pg));
        response.setData(deviceService.getDevicesByCustomerId(customerId, filter.getAction(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(), filter.getStatus(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Approve Devices Actions")
    @RequestMapping(value = "/approve-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            TmsDevice dbMake = deviceService.getDevice(id).get();
            try {
                if (dbMake == null) {
                    loggerService.logApprove("Failed to approve device  (id " + id + "). Failed to locate make with specified id",
                            TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND)+" " + id );
                    continue;
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_APPROVED)) {
                    loggerService.logApprove("Failed to approve device  (id " + id + "). Record has already been approved",
                            TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.RECORD_HAS_ALREADY_BEEN)+" (" + id + ")");
                    continue;
                } else if (loggerService.isInitiator(TmsDevice.class.getSimpleName(), id, dbMake.getAction())) {
                    errors.add(message.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD)+" (" + dbMake.getSerialNo() + ")");
                    loggerService.logUpdate("Failed to approve device (" + dbMake.getSerialNo() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsDevice.class), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    dbMake.setStatus(AppConstants.STATUS_ACTIVE);
                    this.processApproveNew(dbMake, action.getNotes());
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE_FILE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    dbMake.setStatus(AppConstants.STATUS_ACTIVE);
                    this.processApproveUpdateFile(dbMake, action.getNotes());
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processApproveUpdate(dbMake, action.getNotes());
                    dbMake.setStatus(AppConstants.STATUS_ACTIVE);
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DECOMMISSION)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processApproveDecommission(dbMake, action.getNotes());
                    dbMake.setStatus(AppConstants.STATUS_INACTIVE);
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_RELEASE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {

                    this.processApproveRelease(dbMake, action.getNotes());
                    dbMake.setStatus(AppConstants.STATUS_INACTIVE);
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                    Integer releaseCount = dbMake.getReleaseDeviceCount() + 1;
                    dbMake.setReleaseDeviceCount(releaseCount);
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processApproveDeletion(dbMake, action.getNotes());
                    dbMake.setStatus(AppConstants.STATUS_INACTIVE);
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_TASK)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processApproveTask(dbMake, action.getNotes());
                    dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                } else {
                    loggerService.logUpdate("Failed to approve record (" + dbMake.getSerialNo() + "). Record doesn't have approve actions",
                            SharedMethods.getEntityName(TmsDevice.class), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_ACTION));
                }
                deviceService.saveDevice(dbMake);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveNew(TmsDevice entity, String notes) throws ExpectationFailed {
        //set Whitelisted device to assigned
        deviceService.updateWhitelistBySerialSync(entity.getSerialNo(),AppConstants.ASSIGNED);

        loggerService.logApprove("Done approving new Device serial (" + entity.getSerialNo() + ")",
                SharedMethods.getEntityName(TmsDevice.class), entity.getDeviceId(), AppConstants.STATUS_COMPLETED, notes);
        deviceService.updateCustomerTidMid(entity.getSerialNo());
        // send credentials for customer owner
        UfsPosUser posUser = posUserService.findByDeviceIdAndFirstTime(entity.getDeviceId(), (short) 1);
        if (Objects.nonNull(posUser)) {
            UfsCustomerOwners customerOwner = posUser.getCustomerOwners();
            if (customerOwner != null) {
                String cutomerRandomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION,
                        AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
                String messageS = message.setMessage(AppConstants.YOUR_USERNAME) +" " + posUser.getUsername() + ". "+message.setMessage(AppConstants.USER_PASSWORD) + cutomerRandomPin + " " + message.setMessage(AppConstants.TO_LOGIN_TO_POS);
                posUser.setPin(encoder.encode(cutomerRandomPin));
                posUser.setActionStatus(AppConstants.ACTION_STATUS_APPROVED);
                posUserService.savePosUser(posUser);

                if (customerOwner.getDirectorEmailAddress() != null) {
                    notifyService.sendEmail(customerOwner.getDirectorEmailAddress(), "Login Credentials", messageS);
                    notifyService.sendSms(customerOwner.getDirectorPrimaryContactNumber(), messageS);
                    loggerService.log("Sent login credentials for " + customerOwner.getDirectorName(), UfsPosUser.class.getSimpleName(),
                            posUser.getPosUserId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED);
                } else {
                    if (customerOwner.getDirectorPrimaryContactNumber() != null) {
                        // send sms
                        notifyService.sendSms(customerOwner.getDirectorPrimaryContactNumber(), messageS);
                        loggerService.log("Sent login credentials for " + customerOwner.getDirectorName(), UfsPosUser.class.getSimpleName(),
                                posUser.getPosUserId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED);
                    } else {
                        loggerService.log("Failed to send login credentials for " + customerOwner.getDirectorName() + ". No valid email or phone number.", UfsPosUser.class.getSimpleName(),
                                posUser.getPosUserId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_FAILED);
                    }
                }
            }
        }


    }

    private void processApproveUpdateFile(TmsDevice entity, String notes) throws ExpectationFailed {
        deviceService.generateParameterFile(entity, sharedMethods, loggerService);
        loggerService.logApprove("Done approving Device Serial (" + entity.getSerialNo() + ")",
                SharedMethods.getEntityName(TmsDevice.class), entity.getDeviceId(), AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveChanges(TmsDevice entity, String notes) throws ExpectationFailed {
        try {
            supportRepo.setMapping(UfsDeviceMake.class);
            if ((UfsDeviceMake) supportRepo.mergeChanges(entity.getDeviceId(), UfsModifiedRecord.class) == null) {
                throw new ExpectationFailed(message.setMessage(AppConstants.FAILED_TO_APPROVE));
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to approve record changes", ex);
            throw new ExpectationFailed(message.setMessage(AppConstants.CONTACT_ADMIN));
        }
        loggerService.logApprove("Done approving Device  (" + entity.getSerialNo() + ") changes",
                SharedMethods.getEntityName(UfsUserRole.class), entity.getDeviceId(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Update
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveUpdate(TmsDevice entity, String notes) throws ExpectationFailed {
        deviceService.updateCustomerTidMid(entity.getSerialNo());
        loggerService.logApprove("Done approving Updating Device Serial (" + entity.getSerialNo() + ")",
                SharedMethods.getEntityName(TmsDevice.class), entity.getDeviceId(), AppConstants.STATUS_COMPLETED, notes);
    }

    private void processApproveTask(TmsDevice entity, String notes) throws ExpectationFailed {
        loggerService.logApprove("Done approving Creating task  Device Serial (" + entity.getSerialNo() + ")",
                SharedMethods.getEntityName(TmsDevice.class), entity.getDeviceId(), AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveDecommission(TmsDevice entity, String notes) throws ExpectationFailed {
        //entity.setIntrash(AppConstants.YES);
        this.terminalHistoryService.saveHistory(new UfsTerminalHistory(entity.getSerialNo(), AppConstants.ACTIVITY_DECOMMISSION, "Terminal Approval Decommissioned Successfully", loggerService.getUser(), AppConstants.STATUS_APPROVED, loggerService.getFullName()));

        //set Whitelisted device to unassigned
        deviceService.updateReleaseWhitelistBySerialSync(entity.getSerialNo());
        deviceService.deleteAllByDeviceId(entity.getSerialNo());
        loggerService.logApprove("Done approving device (Serial no: " + entity.getSerialNo() + ") decommission.",
                SharedMethods.getEntityName(TmsDevice.class), entity.getSerialNo(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * @param entity
     * @param notes
     * @throws ExpectationFailed
     */
    private void processApproveDeletion(TmsDevice entity, String notes) throws ExpectationFailed {
        entity.setIntrash(AppConstants.YES);

        //set Whitelisted device to unassigned
        deviceService.updateReleaseWhitelistBySerialSync(entity.getSerialNo());

        deviceService.deleteAllByDeviceId(entity.getSerialNo());
        loggerService.logApprove("Done approving device (Serial no: " + entity.getSerialNo() + ") decommission.",
                SharedMethods.getEntityName(TmsDevice.class), entity.getSerialNo(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveRelease(TmsDevice entity, String notes) throws ExpectationFailed {
        try {
            supportService.delete(this.supportService.fetchByEntityAndEntityId(TmsDevice.class.getSimpleName(), entity.getDeviceId().toString()));
        } catch (InvalidDataAccessApiUsageException ex) {

        }
        this.terminalHistoryService.saveHistory(new UfsTerminalHistory(entity.getSerialNo(), AppConstants.ACTIVITY_RELEASE, "Terminal Approval Release Successfully", loggerService.getUser(), AppConstants.STATUS_APPROVED, loggerService.getFullName()));

        deviceService.deleteAllByDeviceId(entity.getSerialNo());
        //set Whitelisted device to unassigned
        deviceService.updateReleaseWhitelistBySerialSync(entity.getSerialNo());
        loggerService.logApprove("Done approving device (" + entity.getSerialNo() + ") release.",
                SharedMethods.getEntityName(TmsDevice.class), entity.getSerialNo(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    @RequestMapping(value = "/release", method = RequestMethod.PUT)
    @ApiOperation(value = "Release a device")
    public ResponseEntity<ResponseWrapper> releaseDevice(@RequestBody @Valid ReleaseDeviceAction action) {
        ResponseWrapper response = new ResponseWrapper();
        if (!(action.getStatus().equals(AppConstants.STATUS_REPAIR) || action.getStatus().equals(AppConstants.STATUS_REALLOCATE))) {
            response.setMessage(message.setMessage(AppConstants.VALIDATION_ERROR)+" " + AppConstants.STATUS_REPAIR + " or " + AppConstants.STATUS_REALLOCATE);
            response.setCode(400);
            return ResponseEntity.badRequest().body(response);
        }
        //check if records exists
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            TmsDevice device = deviceService.getDevice(id).get();
            if (device == null) {
                loggerService.logUpdate("Failed to release device  (id " + id + "). Failed to locate device with specified id",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND+ " " + id));
                continue;
            } else if (device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                loggerService.logUpdate("Failed to release device  (Serial Number: " + device.getSerialNo() + "). Device has unapproved actions (action: " + device.getAction() + ")",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.DEVICE_HAS_UNAPPROVED_ACTION)+" " + device.getSerialNo() );
                continue;
            } else if (device.getStatus().equalsIgnoreCase(AppConstants.STATUS_REPAIR) || device.getStatus().equalsIgnoreCase(AppConstants.STATUS_REALLOCATE)) {
                loggerService.logUpdate("Failed to release device  (Serial Number: " + device.getSerialNo() + "). Device has already been released",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.DEVICE_HAS_BEEN_RELEASED)+" "+ device.getSerialNo());
                continue;
            }
//            String data = "{\"status"
            supportService.clearAndSaveEditedChanges(new UfsModifiedRecord(TmsDevice.class.getSimpleName(), device.getStatus(), id.toString()));
            device.setStatus(action.getStatus());
            device.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            device.setAction(AppConstants.ACTIVITY_RELEASE);
            deviceService.saveDevice(device);
            this.terminalHistoryService.saveHistory(new UfsTerminalHistory(device.getSerialNo(), AppConstants.ACTIVITY_RELEASE, "Terminal Released Successfully", loggerService.getUser(), AppConstants.STATUS_UNAPPROVED, loggerService.getFullName()));

            loggerService.logCreateRelease("Done releasing device (Serial Number: " + device.getSerialNo() + ")",
                    TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @ApiOperation(value = "Decline Device Make Actions")
    @RequestMapping(value = "/decline-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            TmsDevice device = deviceService.getDevice(id).get();
            try {
                if (device == null) {
                    loggerService.logDelete("Failed to decline device (id " + id + ") actions. Failed to locate device with specified id",
                            UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND + " " + id));
                    continue;
                } else if (loggerService.isInitiator(TmsDevice.class.getSimpleName(), id, device.getAction())) {
                    errors.add(message.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD) + " "+device.getSerialNo());
                    loggerService.logUpdate("Failed to decline device (Serial Number: " + device.getSerialNo() + "). Maker can't decline their own record", SharedMethods.getEntityName(TmsDevice.class), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (device.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_RELEASE)
                        && (device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))) {
                    this.processDeclineRelease(device, action.getNotes());
                } else if (device.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    device.setStatus(AppConstants.SCAPI_ACC_STATUS_INACTIVE);
                    this.processDeclineNew(device, action.getNotes());
                } else if (device.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processDeclineChanges(device, action.getNotes());
                } else if (device.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DECOMMISSION)
                        && device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processDeclineDecommission(device, action.getNotes());
                } else if (device.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_TASK)
                        && device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    device.setActionStatus(AppConstants.STATUS_DECLINED);
                    loggerService.logUpdate("Successfully declined tasks on (Serial Number: " + device.getSerialNo() + ")", SharedMethods.getEntityName(TmsDevice.class), id, AppConstants.STATUS_DECLINED);
                } else {
                    loggerService.logUpdate("Failed to decline record (Serial Number: " + device.getSerialNo() + "). Record doesn't have approve actions",
                            SharedMethods.getEntityName(UfsDeviceMake.class), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_ACTION));
                }
                device.setActionStatus(AppConstants.STATUS_DECLINED);
                deviceService.saveDevice(device);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    private void processDeclineNew(TmsDevice device, String notes) {
        loggerService.log("Declined new Device with Serial :" + device.getSerialNo(),
                device.getClass().getSimpleName(), device.getDeviceId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED);
    }

    private void processDeclineChanges(TmsDevice entity, String notes) {
        UfsModifiedRecord mRecord = this.supportService.fetchByEntityAndEntityId(TmsDevice.class.getSimpleName(), entity.getDeviceId().toString());
        entity.setStatus(mRecord.getValues());
        supportService.delete(mRecord);
        loggerService.logApprove("Done declining device (" + entity.getSerialNo() + ") Changes.",
                SharedMethods.getEntityName(TmsDevice.class), entity.getSerialNo(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    private void processDeclineRelease(TmsDevice entity, String notes) throws ExpectationFailed {
        UfsModifiedRecord mRecord = this.supportService.fetchByEntityAndEntityId(TmsDevice.class.getSimpleName(), entity.getDeviceId().toString());
        entity.setStatus(mRecord.getValues());
        supportService.delete(mRecord);
        loggerService.logApprove("Done declining device (" + entity.getSerialNo() + ") release.",
                SharedMethods.getEntityName(TmsDevice.class), entity.getSerialNo(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineDecommission(TmsDevice entity, String notes) throws ExpectationFailed {
        loggerService.logApprove("Done declining device (Serial No. " + entity.getSerialNo() + ") decommission.",
                SharedMethods.getEntityName(UfsDeviceMake.class), entity.getDeviceId(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    @RequestMapping(value = "/decommission", method = RequestMethod.DELETE)
    @ApiOperation(value = "Decommission Device")
    public ResponseEntity<ResponseWrapper> decommission(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            TmsDevice device = deviceService.getDevice(id).get();
            if (device == null) {
                loggerService.logDelete("Failed to decommission device (id: " + id + "). Failed to locate device with specified id",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND) + id);
            } else if (device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                loggerService.logDelete("Failed to decommission device(Serial No. " + device.getSerialNo() + "). Record has unapproved actions",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_SERIAL_NO)+ " " + device.getSerialNo());
            } else {
                device.setAction(AppConstants.ACTIVITY_DECOMMISSION);
                device.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                this.terminalHistoryService.saveHistory(new UfsTerminalHistory(device.getSerialNo(), AppConstants.ACTIVITY_DECOMMISSION, "Terminal Decommissioned Successfully", loggerService.getUser(), AppConstants.STATUS_UNAPPROVED, loggerService.getFullName()));

                loggerService.logCreateDecommision("Deleted device (Serial No. " + device.getSerialNo() + ") successfully", TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
            }
        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Device")
    public ResponseEntity<ResponseWrapper> deleteDevice(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            TmsDevice device = deviceService.getDevice(id).get();
            if (device == null) {
                loggerService.logDelete("Failed to delete device (id: " + id + "). Failed to locate device with specified id",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND + " " + id));
            } else if (device.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                loggerService.logDelete("Failed to delete device(Serial No. " + device.getSerialNo() + "). Record has unapproved actions",
                        TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_SERIAL_NO)+" " + device.getSerialNo() );
            } else {
                device.setAction(AppConstants.ACTIVITY_DELETE);
                device.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted device (Serial No. " + device.getSerialNo() + ") successfully", TmsDevice.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
            }
        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @ApiOperation(value = "Cancel Schedule", notes = "used to Cancel Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Schedule with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/cancel")
    @Transactional
    public ResponseEntity<ResponseWrapper> cancelSchedule(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            TmsDeviceTask dbMake = schedulerService.findDeviceTask(id).get();
            if (dbMake == null) {
                loggerService.logUpdate("Failed to Device Task  (id " + id + "). Failed to locate Device Task with specified id",
                        TmsScheduler.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND) + " " + id);
                continue;
            }
            dbMake.setDownloadStatus("CANCELLED");
            schedulerService.saveDeviceTask(dbMake);
            loggerService.logDelete("Device Task (Task ID. " + id + ") successfully", TmsDeviceTask.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @ApiOperation(value = "Search Devices")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/search/{serialNo}")
    public ResponseEntity<ResponseWrapper<Page<TmsDevice>>> searchDevices(Pageable pg,
                                                                          @Valid @ApiParam(value = "Entity filters and search parameters") DevicesFilter filter, @PathVariable("serialNo") String serialNo) {
        ResponseWrapper<Page<TmsDevice>> response = new ResponseWrapper();
        response.setData(deviceService.getDevices(filter.getAction(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), serialNo, filter.getStatus(), pg));
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Search Merchants")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/merchant")
    public ResponseEntity<ResponseWrapper> searchMerchants(Pageable pg, @Valid @ApiParam(value = "Entity filters and search parameters") DeviceCustomerFilter filter) {
        ResponseWrapper<Page<DeviceCustomerDetails>> response = new ResponseWrapper();
        Page<DeviceCustomerDetails> customerDetails = deviceService.findAllDeviceCustomerDetails(filter.getActionStatus(), filter.getNeedle(), pg);
        response.setData(customerDetails);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Create Merchants")
    @RequestMapping(method = RequestMethod.POST, value = "/merchant")
    public ResponseEntity<ResponseWrapper<DeviceCustomerDetails>> createDeviceCustomerDetails(@Valid @RequestBody DeviceCustomerDetails customerDetails, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new AgentWrapper details failed due to validation errors", SharedMethods.getEntityName(DeviceCustomerDetails.class), customerDetails.getAgentMerchantId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage(message.setMessage(AppConstants.VALIDATION_ERROR));
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        //check if department name exists
        DeviceCustomerDetails dbDeprt = deviceService.findByAgentMerchantId(customerDetails.getAgentMerchantId());
        if (dbDeprt != null) {
            response.setCode(409);
            response.setMessage(message.setMessage(AppConstants.AGENT_WRAPPER_WITH_SIMILAR_TID));
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        customerDetails.setDeviceCustomerId(null);
        customerDetails.setStatus(AppConstants.STATUS_ACTIVE);
        customerDetails.setAction(AppConstants.ACTIVITY_CREATE);
        customerDetails.setActionStatus(AppConstants.STATUS_APPROVED);
        customerDetails.setInstrash(AppConstants.NO);
        deviceService.saveDeviceCustomerDetails(customerDetails);
        response.setCode(200);
        response.setData(customerDetails);
        loggerService.logCreate("Done creating Device Customer Details ", SharedMethods.getEntityName(DeviceCustomerDetails.class), customerDetails.getDeviceCustomerId(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Merchants")
    @RequestMapping(method = RequestMethod.PUT, value = "/merchant/{id}")
    public ResponseEntity<ResponseWrapper<DeviceCustomerDetails>> updateDeviceCustomerDetails(@Valid @RequestBody DeviceCustomerDetails customerDetails, @PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        //check if department name exists
        DeviceCustomerDetails dbDeprt = deviceService.findByAgentMerchantId(customerDetails.getAgentMerchantId());
        if (dbDeprt == null) {
            response.setCode(409);
            response.setMessage(message.setMessage(AppConstants.CUSTOMER_DETAILS_AGENT_NOT_FOUND));
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        dbDeprt.setFormValues(customerDetails.getFormValues());
        deviceService.saveDeviceCustomerDetails(dbDeprt);

        TmsDevice device = deviceService.getDevice(id).get();
        if (device != null) {
            TmsDeviceParam dParam = deviceParamService.findByDeviceId(device);
            if (dParam != null) {
                dParam.setValues(dbDeprt.getFormValues());
                deviceParamService.saveDeviceParam(dParam);
            } else {
                TmsDeviceParam deviceParam = new TmsDeviceParam();
                deviceParam.setDeviceId(device);
                deviceParam.setDeviceCustomerId(dbDeprt);
                deviceParam.setValues(dbDeprt.getFormValues());
                deviceParamService.saveDeviceParam(deviceParam);
            }

            device.setAction(AppConstants.ACTIVITY_UPDATE_FILE);
            device.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            deviceService.saveDevice(device);
        }

        response.setCode(200);
        response.setData(customerDetails);
        loggerService.logCreate("Done creating Device Customer Details ", SharedMethods.getEntityName(DeviceCustomerDetails.class), customerDetails.getDeviceCustomerId(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Search Merchants")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/merchant/{agentMerchantId}")
    public ResponseEntity<ResponseWrapper> getMerchants(@PathVariable("agentMerchantId") String agentMerchantId) {
        ResponseWrapper response = new ResponseWrapper();
        DeviceCustomerDetails details = deviceService.findByAgentMerchantId(agentMerchantId);
        if (details != null) {
            response.setData(details);
            return ResponseEntity.ok(response);
        } else {
            try {
                ResponseWrapper responseWrapper = restTemplateClient.getForEntity(ResponseWrapper.class, tidUrl, new String[]{agentMerchantId});
                return ResponseEntity.ok(responseWrapper);
            } catch (Exception e) {
                response.setMessage(message.setMessage(AppConstants.DATA_NOT_FOUND));
                return ResponseEntity.ok(response);
            }
        }
    }

    @ApiOperation(value = "Get Device Parameters By ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/params/{id}")
    public ResponseEntity<ResponseWrapper<TmsDeviceParam>> getDeviceParameters(@PathVariable("id") BigDecimal id) {
        ResponseWrapper<TmsDeviceParam> response = new ResponseWrapper();
        response.setData(deviceService.findParamByDeviceId(deviceService.getDevice(id).get()));
        return ResponseEntity.ok(response);
    }


}
