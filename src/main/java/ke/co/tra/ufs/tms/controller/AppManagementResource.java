package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.persistence.EntityManager;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.entities.TmsApp;
import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.ApplicationFilter;
import ke.co.tra.ufs.tms.entities.wrappers.TMSAppWrapper;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.AppManagementService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Owori J
 */
@Controller
@RequestMapping("/app-management")
@Api(value = "Application Management Resource")
public class AppManagementResource extends ChasisResourceLocal<TmsApp> {

    private final AppManagementService appManagementService;
    private final SharedMethods sharedMethods;
    private final SysConfigService configService;

    @ApiOperation(value = "Fetch Applications", notes = "Used to fetch all Applications by Model ID")
//    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsApp>>> fetchApplicationsByDevicesModel(@PathVariable("id") BigDecimal id, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        UfsDeviceModel deviceModel = appManagementService.findDeviceModel(id);
        if (deviceModel != null) {
            response.setData(appManagementService.findApps(deviceModel, pg));
        } else {
            loggerService.logCreate("Error Fetching (model id: " + id
                    + ") failed due to Model not found", SharedMethods.getEntityName(UfsDeviceModel.class), id, AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate model with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public AppManagementResource(AppManagementService appManagementService, LoggerServiceLocal loggerService,
                                 EntityManager entityManager, SupportRepository supportRepo, SharedMethods sharedMethods,
                                 SysConfigService configService) {
        super(loggerService, entityManager, supportRepo);
        this.appManagementService = appManagementService;
        this.loggerService = loggerService;
        this.sharedMethods = sharedMethods;
        this.configService = configService;
    }

    @Override
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<TmsApp>> create(@Valid @RequestBody TmsApp entity) {
        throw new UnsupportedOperationException("Sorry not used (Used to silence super method)");
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper<TmsApp>> createApp(@Valid TmsApp entity) throws IOException {
        ResponseWrapper response = new ResponseWrapper();
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
        if (entity.getApplication() == null) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error!, Application not attached, Please attach a valid application file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        /**
         * Check if app name already exists
         */
        TmsApp tmsApp = appManagementService.findTmsAppByAppName(entity.getAppName());
        if (tmsApp != null) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Error!, Application name already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        //validate file extension
        log.debug("File extensions {}", entity.getApplication().getOriginalFilename().substring(entity.getApplication().getOriginalFilename().lastIndexOf(".") + 1));
        //if (!entity.getApplication().getContentType().equalsIgnoreCase("application/x-zip-compressed")) {
        if (!entity.getApplication().getOriginalFilename().substring(entity.getApplication().getOriginalFilename().lastIndexOf(".") + 1).equals("zip")) {
            response.setCode(HttpStatus.FAILED_DEPENDENCY.value());
            response.setMessage("File extension is not supported, Only .Zip Extensions supported");
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
        }

        ZipFile zipFile = new ZipFile(sharedMethods.convert(entity.getApplication()));
        Enumeration zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            if (((ZipEntry) zipEntries.nextElement()).getName().contains("/")) {
                response.setCode(HttpStatus.FAILED_DEPENDENCY.value());
                response.setMessage("Zip file should not contain a  sub directory, should be files only");
                return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
            }
        }

        try {
            String uploadPath = rootPath + "app/" + entity.getProductId() + "/" + entity.getModelId() + '/';
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileUrl = sharedMethods.store(entity.getApplication(), uploadPath);
            entity.setNotesFilepath(fileUrl);
            return super.create(entity);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Encountered an error while writing file to directory", ex);
            loggerService.logCreate("Uploading Application failed. File cannot be written on the server",
                    SharedMethods.getEntityName(TmsApp.class),
                    null, AppConstants.STATUS_FAILED);
            response.setCode(500);
            response.setMessage("An internal server error occured while uploading device whitelist file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @Transactional
    public ResponseEntity<ResponseWrapper<TmsApp>> updateApp(@Valid TMSAppWrapper appWrapper) throws IOException {
        ResponseWrapper response = new ResponseWrapper();
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
        if (appWrapper.getApplication() == null) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error!, Application not attached, Please attach a valid application file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        TmsApp entity = appManagementService.findTmsApp(appWrapper.getAppId()).get();
        if (entity == null) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error!,  App ID not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        //validate file extension
        log.debug("File extensions {}", appWrapper.getApplication().getOriginalFilename().substring(appWrapper.getApplication().getOriginalFilename().lastIndexOf(".") + 1));
        //if (!entity.getApplication().getContentType().equalsIgnoreCase("application/x-zip-compressed")) {
        if (!appWrapper.getApplication().getOriginalFilename().substring(appWrapper.getApplication().getOriginalFilename().lastIndexOf(".") + 1).equals("zip")) {
            response.setCode(HttpStatus.FAILED_DEPENDENCY.value());
            response.setMessage("File extension is not supported, Only .Zip Extensions supported");
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
        }

        ZipFile zipFile = new ZipFile(sharedMethods.convert(appWrapper.getApplication()));
        Enumeration zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            if (((ZipEntry) zipEntries.nextElement()).getName().contains("/")) {
                response.setCode(HttpStatus.FAILED_DEPENDENCY.value());
                response.setMessage("Zip file should not contain a  sub directory, should be files only");
                return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
            }
        }

        try {
            String uploadPath = rootPath + "app/" + appWrapper.getProductId() + "/" + appWrapper.getModelId() + '/';
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileUrl = sharedMethods.store(appWrapper.getApplication(), uploadPath);
            entity.setAppName(appWrapper.getAppName());
            entity.setAppVersion(appWrapper.getAppVersion());
            entity.setDescription(appWrapper.getDescription());
            entity.setModelId(appWrapper.getModelId());
            entity.setProductId(appWrapper.getProductId());

            entity.setNotesFilepath(fileUrl);
            entity.setAction(AppConstants.ACTIVITY_UPDATE);
            entity.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            appManagementService.saveTmsApp(entity);
            response.setCode(200);
            response.setMessage("Successfully Updated");
            response.setData(entity);


            loggerService.logUpdate("Update TmsApp Successfully",
                    SharedMethods.getEntityName(TmsApp.class),
                    entity.getAppId(), AppConstants.STATUS_COMPLETED);

            return ResponseEntity.status(HttpStatus.OK).body(response);
//            return super.create(entity);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Encountered an error while writing file to directory", ex);
            loggerService.logUpdate("Uploading Application failed. File cannot be written on the server",
                    SharedMethods.getEntityName(TmsApp.class),
                    null, AppConstants.STATUS_FAILED);
            response.setCode(500);
            response.setMessage("An internal server error occured while uploading device whitelist file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ApiOperation(value = "Fetch Application")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<TmsApp>>> getApplication(Pageable pg,
                                                                        @Valid @ApiParam(value = "Entity filters and search parameters") ApplicationFilter filter) {
        ResponseWrapper<Page<TmsApp>> response = new ResponseWrapper();
        response.setData(this.appManagementService.getApplications(filter.getActionStatus(),
                filter.getFrom(), filter.getTo(), filter.getProductId(), filter.getModelId(), filter.getNeedle(), pg));
//        response.setData(deviceService.getMake(filter.getActionStatus(), filter.getNeedle(), pg));
        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<BigDecimal> actions) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errors = new ArrayList<>();
        for (BigDecimal id : actions.getIds()) {
            TmsApp tmsApp = appManagementService.findTmsAppById(id);
            if (loggerService.isInitiator(TmsApp.class.getSimpleName(), id, tmsApp.getAction())) {
                errors.add("Maker cannot approve own records with Id[" + id + "]");
                continue;
            }

            if (tmsApp.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && tmsApp.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                tmsApp.setActionStatus(AppConstants.STATUS_APPROVED);
                appManagementService.saveTmsApp(tmsApp);
//                responseWrapper.setMessage("request was successful");
//                responseWrapper.setCode(HttpStatus.OK.value());
//                return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
            }
        }

        if (errors.size() > 0) {
            responseWrapper.setMessage(String.join(", ", errors));
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.CONFLICT);
        }

        return super.approveActions(actions);
    }
}
