package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalConfigFileRequest;
import ke.co.tra.ufs.tms.entities.wrappers.MenuFileRequest;
import ke.co.tra.ufs.tms.entities.wrappers.ScheduleWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.CommonFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.*;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 * @author Owori Juma
 */
@RestController
@RequestMapping("/schedule")
@Api(value = "Scheduling Resource")
public class ScheduleResource {

    private final DeviceService deviceService;
    private final LoggerServiceLocal loggerService;
    private final SupportRepository supportRepo;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SchedulerService schedulerService;
    private final ProductService productService;
    private final SysConfigService configService;
    private final SharedMethods sharedMethods;
    private final AppManagementService appManagementService;
    private final BusinessUnitService businessUnitService;
    private final ParBinProfileService parBinProfileService;
    private final ParMenuProfileService parMenuProfileService;
    private final ParGlobalMasterProfileService parGlobalMasterProfileService;
    private final ParFileMenuService parFileMenuService;
    private final ParFileConfigService parFileConfigService;

    public ScheduleResource(DeviceService deviceService, LoggerServiceLocal loggerService, SupportRepository supportRepo, SchedulerService schedulerService,
                            ProductService productService, SysConfigService configService, SharedMethods sharedMethods, AppManagementService appManagementService,
                            BusinessUnitService businessUnitService, ParBinProfileService parBinProfileService, ParMenuProfileService parMenuProfileService,
                            ParGlobalMasterProfileService parGlobalMasterProfileService, ParFileMenuService parFileMenuService, ParFileConfigService parFileConfigService) {
        this.deviceService = deviceService;
        this.loggerService = loggerService;
        this.supportRepo = supportRepo;
        this.schedulerService = schedulerService;
        this.productService = productService;
        this.configService = configService;
        this.sharedMethods = sharedMethods;
        this.appManagementService = appManagementService;
        this.businessUnitService = businessUnitService;
        this.parBinProfileService = parBinProfileService;
        this.parMenuProfileService = parMenuProfileService;
        this.parGlobalMasterProfileService = parGlobalMasterProfileService;
        this.parFileMenuService = parFileMenuService;
        this.parFileConfigService = parFileConfigService;
    }

    @ApiOperation(value = "Fetch Schedules")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<TmsScheduler>>> getScheduler(Pageable pg,
                                                                            @Valid @ApiParam(value = "Entity filters and search parameters") CommonFilter filter) {
        ResponseWrapper<Page<TmsScheduler>> response = new ResponseWrapper();
        response.setData(schedulerService.getSchedules(filter.getActionStatus(), filter.getNeedle(), pg));
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Fetch Pending Manual Tasks")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/manual")
    public ResponseEntity<ResponseWrapper<Page<TmsDeviceTask>>> getPendingManualTask(Pageable pg,
                                                                                     @Valid @ApiParam(value = "Entity filters and search parameters") CommonFilter filter) {
        ResponseWrapper<Page<TmsDeviceTask>> response = new ResponseWrapper();
        response.setData(schedulerService.findPendingManual(pg));
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Fetch Schedules by ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<TmsScheduler>> getSchedulerById(@PathVariable("id") BigDecimal id) {
        ResponseWrapper<TmsScheduler> response = new ResponseWrapper();
        response.setData(schedulerService.getSchedule(id).get());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Create Schedule", notes = "used to create a device within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Schedule with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createSchedule(@ApiParam(value = "Ignore status and scheduleId it will be used when fetching")
                                                          @Valid ScheduleWrapper scheduleWrapper, BindingResult validation) {

        log.debug("ScheduleWrapper request {}", scheduleWrapper);

        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Schedule failed due to validation errors", SharedMethods.getEntityName(TmsScheduler.class), scheduleWrapper.getAppId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        long filecount = 0;
        if (scheduleWrapper.getDownloadType().equals("App Only")) {
            filecount = 1;
        } else {
            if (scheduleWrapper.getMasterProfileId() != null) {
                filecount += 1;
            }

            if (scheduleWrapper.getFile() != null) {
                filecount += scheduleWrapper.getFile().length + 1;
            }
        }

        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();

        TmsScheduler scheduler = new TmsScheduler();
        scheduler.setAction(AppConstants.ACTIVITY_CREATE);
        scheduler.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        scheduler.setAppId(scheduleWrapper.getAppId());
        scheduler.setScheduledTime(scheduleWrapper.getScheduleTime());
        scheduler.setModelId(deviceService.getModel(scheduleWrapper.getModelId()));
        scheduler.setScheduleType(AppConstants.AUTO_SCHEDULE);
        scheduler.setStatus(AppConstants.STATUS_NEW);
        scheduler.setNoFiles(filecount);
        scheduler.setDownloadType(AppConstants.DOWNLOAD_APP_AND_FILES);
        scheduler.setIntrash(AppConstants.NO);
        scheduler.setProductId(productService.getProduct(scheduleWrapper.getProductId()).get());

        //save the manual schedule
        schedulerService.saveSchedule(scheduler);

        String path = "/" + scheduleWrapper.getProductId() + '/' + scheduleWrapper.getModelId() + '/' + scheduler.getScheduleId() + '/';
        scheduler.setDirPath(path);
        schedulerService.saveSchedule(scheduler);
        //create

        //Add to estates and scheduled files
        createScheduledFiles(scheduleWrapper, path, scheduler);
        createScheduledEstates(scheduleWrapper, scheduler);

        rootPath = rootPath + scheduleWrapper.getProductId() + '/' + scheduleWrapper.getModelId() + '/' + scheduler.getScheduleId() + '/';

        try {
            transferAndCopyFiles(scheduleWrapper, rootPath, scheduler);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(OnboardingResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setData(scheduler);
        loggerService.logCreate("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
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
            TmsScheduler dbMake = schedulerService.getSchedule(id).get();
            if (dbMake == null) {
                loggerService.logUpdate("Failed to schedule  (id " + id + "). Failed to locate schedule with specified id",
                        TmsScheduler.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Schedule with id " + id + " doesn't exist");
                continue;
            }
            dbMake.setAction(AppConstants.ACTIVITY_CANCEL);
            dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            schedulerService.saveSchedule(dbMake);

            loggerService.logCreate("Cancelling schedule (id: " + id
                    + ") in Progress", SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_UNAPPROVED);
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

    private void transferAndCopyFiles(ScheduleWrapper scheduleWrapper, String rootPath, TmsScheduler scheduler) throws IOException {
        try {
            File dir = new File(rootPath);
            if (dir.exists()) {
                sharedMethods.deleteDirectory(rootPath);
            }

            if (scheduleWrapper.getFile() != null) {
                for (MultipartFile mf : scheduleWrapper.getFile()) {
                    sharedMethods.store(mf, rootPath);
                }
            }


            if (scheduleWrapper.getMasterProfileId() != null) {
                Optional<ParGlobalMasterProfile> optionalMaster = parGlobalMasterProfileService.findById(scheduleWrapper.getMasterProfileId());

                if (optionalMaster.isPresent()) {
                    ParGlobalMasterProfile parGlobalMasterProfile = optionalMaster.get();

                    // generate menu profile
                    parFileMenuService.generateMenuFileAsync(new MenuFileRequest(scheduleWrapper.getModelId(), parGlobalMasterProfile.getMenuProfileId()), rootPath);

                    // generate all global configs related to master profile
                    for (ParGlobalMasterChildProfile config : parGlobalMasterProfile.getChildProfiles()) {
                        //parFileConfigService.generateGlobalConfigFileAsync(new GlobalConfigFileRequest(scheduleWrapper.getModelId(), config.getId()), rootPath);
                        parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(), scheduleWrapper.getModelId(), rootPath);
                    }
                }
            }

            loggerService.logCreate("Saving new App Files", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);

            if (!scheduleWrapper.getDownloadType().equals("Files Only")) {
                TmsApp tmsApp = appManagementService.findTmsApp(scheduleWrapper.getAppId()).get();
                String fromPath = tmsApp.getNotesFilepath();

                File f = new File(fromPath);
                sharedMethods.moveFile(fromPath, rootPath + f.getName(), rootPath);

                loggerService.logCreate("Tranfered App " + f.getName(), SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createScheduledFiles(ScheduleWrapper scheduleWrapper, String rootPath, TmsScheduler scheduler) {
        if (scheduleWrapper.getFile() != null) {
            for (MultipartFile file : scheduleWrapper.getFile()) {
                TmsScheduleFile scheduleFile = new TmsScheduleFile();
                scheduleFile.setFileName(file.getOriginalFilename());
                scheduleFile.setFilePath(rootPath);
                scheduleFile.setIntrash(AppConstants.NO);
                scheduleFile.setScheduleId(scheduler);
                scheduleFile.setIsApp("NO");
                schedulerService.saveScheduleFile(scheduleFile);
            }
        }
        if (scheduleWrapper.getDownloadType().equals(AppConstants.DOWNLOAD_APP_AND_FILES) || scheduleWrapper.getDownloadType().equals(AppConstants.DOWNLOAD_APP_ONLY)) {
            TmsApp tmsApp = appManagementService.findTmsApp(scheduleWrapper.getAppId()).get();
            String fromPath = tmsApp.getNotesFilepath();
            File f = new File(fromPath);

            TmsScheduleFile scheduleFile = new TmsScheduleFile();
            scheduleFile.setFileName(f.getName());
            scheduleFile.setFilePath(fromPath);
            scheduleFile.setIntrash(AppConstants.NO);
            scheduleFile.setScheduleId(scheduler);
            scheduleFile.setIsApp("YES");
            schedulerService.saveScheduleFile(scheduleFile);
        }
    }

    private void createScheduledEstates(ScheduleWrapper scheduleWrapper, TmsScheduler scheduler) {
        for (BigDecimal id : scheduleWrapper.getUnitItemId()) {
            TmsEstateItem businessUnitItem = businessUnitService.getUnitItem(id).get();
            TmsScheduleEstate scheduleEstate = new TmsScheduleEstate();
            scheduleEstate.setIntrash(AppConstants.NO);
            scheduleEstate.setScheduleId(scheduler);
            scheduleEstate.setUnitItemId(businessUnitItem);
            schedulerService.saveScheduleEstate(scheduleEstate);
        }
    }

    @ApiOperation(value = "Approve Schedule Actions")
    @RequestMapping(value = "/approve-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            TmsScheduler dbMake = schedulerService.getSchedule(id).get();
            if (dbMake == null) {
                loggerService.logUpdate("Failed to schedule  (id " + id + "). Failed to locate schedule with specified id",
                        TmsScheduler.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Schedule with id " + id + " doesn't exist");
                continue;
            }else if (loggerService.isInitiator(TmsScheduler.class.getSimpleName(), id, dbMake.getAction())) {
                errors.add("Sorry maker can't approve their own record (" + dbMake.getScheduleId() + ")");
                loggerService.logUpdate("Failed to approve schedule with id (" + dbMake.getScheduleId() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_FAILED);
                continue;
            }

            if (dbMake.getAction().equals(AppConstants.ACTIVITY_CREATE)) {
                schedulerService.createTaskItems(dbMake);
            } else if (dbMake.getAction().equals(AppConstants.ACTIVITY_CANCEL)) {
                schedulerService.cancelTaskItems(dbMake);
                loggerService.logCreate("Cancelling schedule (id: " + id
                        + ") in Progress", SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_COMPLETED);
            } else {
                schedulerService.updateTaskItems(dbMake);
            }

            dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
            schedulerService.saveSchedule(dbMake);

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


    @ApiOperation(value = "Decline Schedule Actions", notes = "Used to decline All Schedule actions (delete, update and new record)")
    @ApiResponses(value = {
            @ApiResponse(code = 207, message = "Returned when some Schedule could not be declined")
    })
    @RequestMapping(value = "/decline-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        boolean hasErrors = false;
        for (BigDecimal id : action.getIds()) {
            TmsScheduler dbMake = schedulerService.getSchedule(id).get();
            if (dbMake == null) {
                loggerService.logUpdate("Failed to schedule (Id: " + id + "). schedule doesn't exist", SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_FAILED);
                hasErrors = true;
                continue;
            }
            if (loggerService.isInitiator(SharedMethods.getEntityName(TmsScheduler.class), id, dbMake.getAction())) {
                hasErrors = true;
                loggerService.logUpdate("Failed to decline schedule (" + dbMake.getScheduleId() + "). You can't approve/decline your own record", SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                    && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                if (!this.processDeclineNew(dbMake, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                    && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                if (!this.processDeclineUpdate(dbMake, action.getNotes())) {
                    hasErrors = true;
                }
            } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CANCEL)
                    && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processDeclineCancel(dbMake, action.getNotes())) {
                    hasErrors = true;
                }
            } else {
                loggerService.logUpdate("Failed to decline schedule (" + dbMake.getScheduleId() + "). schedule doesn't have decline actions",
                        SharedMethods.getEntityName(TmsScheduler.class), id, AppConstants.STATUS_FAILED, action.getNotes());
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

    @ApiOperation(value = "Update Schedule")
    @RequestMapping(method = RequestMethod.POST, value = "/update-schedule")
    public ResponseEntity<ResponseWrapper> updateSchedule(@Valid ScheduleWrapper scheduleWrapper, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Schedule failed due to validation errors", SharedMethods.getEntityName(TmsScheduler.class), scheduleWrapper.getAppId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        TmsScheduler scheduler = schedulerService.getSchedule(scheduleWrapper.getScheduleId()).get();
        if (scheduler == null) {
            loggerService.logUpdate("Failed to Update Schedule (Schedule id: " + scheduleWrapper.getScheduleId() + "). Schedule doesn't exist",
                    SharedMethods.getEntityName(TmsScheduler.class), scheduleWrapper.getScheduleId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate schedule with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        long filecount = 0;
        if (scheduleWrapper.getDownloadType().equals("App Only")) {
            filecount = 1;
        } else {
            if (scheduleWrapper.getMasterProfileId() != null) {
                filecount += 1;
            }

            if (scheduleWrapper.getFile() != null) {
                filecount += scheduleWrapper.getFile().length + 1;
            }
        }

        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();

        scheduler.setAction(AppConstants.ACTIVITY_APPROVE);
        scheduler.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        scheduler.setAppId(scheduleWrapper.getAppId());
        scheduler.setScheduledTime(scheduleWrapper.getScheduleTime());
        scheduler.setModelId(deviceService.getModel(scheduleWrapper.getModelId()));
        scheduler.setStatus(AppConstants.STATUS_PENDING);
        scheduler.setNoFiles(filecount);
        scheduler.setProductId(productService.getProduct(scheduleWrapper.getProductId()).get());

        //save the manual schedule
        schedulerService.saveSchedule(scheduler);

        rootPath = "" + scheduleWrapper.getProductId() + '/' + scheduleWrapper.getModelId() + '/' + scheduler.getScheduleId() + '/';
        scheduler.setDirPath(rootPath);
        schedulerService.saveSchedule(scheduler);

        //Add to estates and scheduled files
        updateScheduledFiles(scheduleWrapper, rootPath, scheduler);
        updateScheduledEstates(scheduleWrapper, scheduler);

        try {
            rootPath = rootPath + scheduleWrapper.getProductId() + '/' + scheduleWrapper.getModelId() + '/' + scheduler.getScheduleId() + '/';
            transferAndCopyFiles(scheduleWrapper, rootPath, scheduler);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(OnboardingResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setData(scheduler);
        loggerService.logCreate("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    private void updateScheduledFiles(ScheduleWrapper scheduleWrapper, String rootPath, TmsScheduler scheduler) {
        List<TmsScheduleFile> scheduleFiles = schedulerService.findScheduleFilesByScheduleId(scheduler);
        scheduleFiles.forEach((shed) -> {
            schedulerService.deleteScheduleFile(shed);
        });

        if (scheduleWrapper.getFile() != null) {
            for (MultipartFile file : scheduleWrapper.getFile()) {
                TmsScheduleFile scheduleFile = new TmsScheduleFile();
                scheduleFile.setFileName(file.getOriginalFilename());
                scheduleFile.setFilePath(rootPath);
                scheduleFile.setIntrash(AppConstants.NO);
                scheduleFile.setScheduleId(scheduler);
                scheduleFile.setIsApp("NO");
                schedulerService.saveScheduleFile(scheduleFile);
            }
        }
        if (scheduleWrapper.getDownloadType().equals(AppConstants.DOWNLOAD_APP_AND_FILES) || scheduleWrapper.getDownloadType().equals(AppConstants.DOWNLOAD_APP_ONLY)) {
            TmsApp tmsApp = appManagementService.findTmsApp(scheduleWrapper.getAppId()).get();
            String fromPath = tmsApp.getNotesFilepath();
            File f = new File(fromPath);
            TmsScheduleFile scheduleFile = new TmsScheduleFile();
            scheduleFile.setFileName(f.getName());
            scheduleFile.setFilePath(fromPath);
            scheduleFile.setIntrash(AppConstants.NO);
            scheduleFile.setScheduleId(scheduler);
            scheduleFile.setIsApp("YES");
            schedulerService.saveScheduleFile(scheduleFile);
        }
    }

    private void updateScheduledEstates(ScheduleWrapper scheduleWrapper, TmsScheduler scheduler) {
        List<TmsScheduleEstate> estates = schedulerService.findByscheduleId(scheduler);
        estates.forEach((shed) -> {
            schedulerService.deleteScheduleEstate(shed);
        });

        for (BigDecimal id : scheduleWrapper.getUnitItemId()) {
            TmsEstateItem businessUnitItem = businessUnitService.getUnitItem(id).get();
            TmsScheduleEstate scheduleEstate = new TmsScheduleEstate();
            scheduleEstate.setIntrash(AppConstants.NO);
            scheduleEstate.setScheduleId(scheduler);
            scheduleEstate.setUnitItemId(businessUnitItem);
            schedulerService.saveScheduleEstate(scheduleEstate);
        }
    }

    private boolean processDeclineNew(TmsScheduler dbMake, String notes) {
        loggerService.logApprove("Declining new TmsSchedule (Id: " + dbMake.getScheduleId() + ")",
                SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(), AppConstants.STATUS_PENDING, notes);
        dbMake.setActionStatus(AppConstants.STATUS_DECLINED);
        schedulerService.saveSchedule(dbMake);
        loggerService.logApprove("Declined new TmsSchedule (Id: " + dbMake.getScheduleId() + ") successfully", SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    private boolean processDeclineUpdate(TmsScheduler dbMake, String notes) {
        loggerService.logApprove("Declining Update TmsSchedule (Id: " + dbMake.getScheduleId() + ")",
                SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(), AppConstants.STATUS_PENDING, notes);
        dbMake.setActionStatus(AppConstants.STATUS_DECLINED);
        schedulerService.saveSchedule(dbMake);
        loggerService.logApprove("Declined Update TmsSchedule (Id: " + dbMake.getScheduleId() + ") successfully", SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    private boolean processDeclineCancel(TmsScheduler dbMake, String notes) {
        loggerService.logApprove("Declining Update TmsSchedule (Id: " + dbMake.getScheduleId() + ")",
                SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(), AppConstants.STATUS_PENDING, notes);
        dbMake.setActionStatus(AppConstants.STATUS_DECLINED);
        schedulerService.saveSchedule(dbMake);
        loggerService.logApprove("Declined Update TmsSchedule (Id: " + dbMake.getScheduleId() + ") successfully", SharedMethods.getEntityName(TmsScheduler.class), dbMake.getScheduleId(),
                AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

}
