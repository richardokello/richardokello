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
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.WhiteListAssignWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.WhitelistDetails;
import ke.co.tra.ufs.tms.entities.wrappers.WhitelistWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.WhitelistFilter;
import ke.co.tra.ufs.tms.repository.AuthenticationRepository;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.service.UfsTerminalHistoryService;
import ke.co.tra.ufs.tms.service.templates.LoggerServiceVersion;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exports.CsvFlexView;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Cornelius M
 */
@RequestMapping(value = "/device/whitelist")
@Api(value = "Whitelist Resource")
@Controller
public class WhitelistResource extends ChasisResourceLocal<TmsWhitelist> {

    private final DeviceService deviceService;
    private final SysConfigService configService;
    private final SharedMethods sharedMethods;
    private final UfsTerminalHistoryService terminalHistoryService;
    private final AuthenticationRepository urepo;

    public WhitelistResource(LoggerServiceLocal loggerService, EntityManager entityManager,
                             SupportRepository supportRepo, DeviceService deviceService, SysConfigService configService,
                             SharedMethods sharedMethods,UfsTerminalHistoryService terminalHistoryService,AuthenticationRepository urepo) {
        super(loggerService, entityManager, supportRepo);
        this.deviceService = deviceService;
        this.configService = configService;
        this.sharedMethods = sharedMethods;
        this.terminalHistoryService = terminalHistoryService;
        this.urepo = urepo;
    }

    @Override
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<TmsWhitelist>> create(@Valid @RequestBody TmsWhitelist entity) {
        throw new UnsupportedOperationException("Sorry not used (Used to silence super method)");
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    @ApiOperation(value = "Whitelist A Device")
    public ResponseEntity<ResponseWrapper> create(@Valid WhitelistWrapper payload, HttpServletRequest request) {
        ResponseWrapper response = new ResponseWrapper();
        //check if model exists
        UfsDeviceModel model = this.deviceService.getModel(payload.getModelId());

        if (model == null) {
            response.setCode(404);
            response.setMessage("Device model doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (model.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            response.setCode(417);
            response.setMessage("Device model has unapproved actions");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }

        if (payload.getFile() == null && (payload.getSerialNo() != null && !payload.getSerialNo().isEmpty())) {
            TmsWhitelist device = this.deviceService.getWhitelist(payload.getSerialNo());
            if (device != null) {
                response.setCode(HttpStatus.CONFLICT.value());
                response.setMessage("Serial number " + payload.getSerialNo() + " already exists");
                return new ResponseEntity(response, HttpStatus.CONFLICT);
            }
            device = this.deviceService.saveWhitelist(new TmsWhitelist(payload.getSerialNo(), model.getModelId(),
                    AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_UNAPPROVED, AppConstants.NO, payload.getPurchaseDate(), payload.getProductNo(),
                    payload.getLocation(), payload.getDeliveredBy(), payload.getReceivedBy(), payload.getUfsBankId()));

             this.terminalHistoryService.saveHistory(new UfsTerminalHistory(payload.getSerialNo(),AppConstants.ACTIVITY_CREATE,"Terminal Whitelisted Successfully",loggerService.getUser(),AppConstants.STATUS_UNAPPROVED,loggerService.getFullName()));

            loggerService.logCreate("Whitelisted Device (Serial number: " + payload.getSerialNo() + ")",
                    TmsWhitelist.class.getSimpleName(), device.getId(), AppConstants.STATUS_COMPLETED);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (payload.getFile() != null) {
            System.out.println("File uploaded --" + payload.getFile().getContentType());

            //Validate file extension
            if (!(payload.getFile().getContentType().equalsIgnoreCase("text/csv")
                    || payload.getFile().getContentType().equalsIgnoreCase("application/vnd.ms-excel")
                    || payload.getFile().getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                response.setCode(400);
                response.setMessage("Unsupported file type. Expects a CSV file");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
            String fileName = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
            TmsWhitelistBatch batch = new TmsWhitelistBatch();
            batch.setFileName(fileName);
            batch.setModel(model);
//            batch.setUploadedBy(user);
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username;
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }

                System.out.println("LOGGED IN USER : " + username);
                UfsAuthentication userAuth = urepo.findByusernameIgnoreCase(username);
                Long userId = userAuth.getUserId();

                String fileUrl = sharedMethods.store(payload.getFile(), fileName);
                batch.setFilePath(fileUrl);
                batch.setProcessingStatus(AppConstants.STATUS_PENDING);
                batch = deviceService.saveBatch(batch);
                if (payload.getFile().getContentType().equalsIgnoreCase("text/csv") || payload.getFile().getOriginalFilename().endsWith(".csv")) {
                    log.error("Uploading =======> csv");
                    this.deviceService.processWhitelistUpload(batch, configService,
                            sharedMethods, loggerService, payload.getFile().getBytes(),
                            request.getRemoteAddr(), StringUtils.abbreviate(request.getHeader("user-agent"), 100),userId, payload);
                } else {
                    log.error("Uploading =======> xlsx");
                    this.deviceService.processWhitelistUploadXlxs(batch, configService,
                            sharedMethods, loggerService, payload.getFile(),
                            request.getRemoteAddr(), StringUtils.abbreviate(request.getHeader("user-agent"), 100),userId, payload);
                }
            } catch (IOException ex) {
                log.error(AppConstants.AUDIT_LOG, "Encountered an error while writing file to directory", ex);
                loggerService.logCreate("Uploading Device serial numbers failed. File cannot be written on the server",
                        SharedMethods.getEntityName(TmsWhitelistBatch.class),
                        null, AppConstants.STATUS_FAILED);
                batch.setProcessingStatus(AppConstants.STATUS_FAILED);
                response.setCode(500);
                response.setMessage("An internal server error occured while uploading device whitelist file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.setCode(400);
            response.setMessage("Expects a serial number or a CSV file of serial numbers");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiOperation(value = "Download Whitelist template")
    @RequestMapping(value = "/whitelist-template.csv", method = RequestMethod.GET)
    public ModelAndView exportVehicleTemplate(HttpServletRequest request) {
        CsvFlexView view;
        String fileName = "Whitelist Template";
        view = new CsvFlexView(WhitelistDetails.class, new ArrayList(),
                fileName);
        return new ModelAndView(view);
    }

    @ApiOperation(value = "Fetch Whitelist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<TmsWhitelist>>> get(@Valid WhitelistFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.deviceService.getWhitelist(filter.getActionStatus(),
                filter.getModelId(), filter.getFrom(), filter.getTo(), filter.getNeedle(), filter.getSerialNo(), filter.getAssignStr(),filter.getUfsBankId(), pg));
        return ResponseEntity.ok(response);

    }



    @ApiOperation(value = "Fetch Whitelist By Device ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/serial/{id}")
    public ResponseEntity<ResponseWrapper<TmsWhitelist>> getbySerial(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceService.getWhitelistDevicebySerial(deviceService.getDevice(id).get().getSerialNo()));
        return ResponseEntity.ok(response);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/assign")
    public ResponseEntity<ResponseWrapper> assignDeviceBySerial(@Valid @RequestBody WhiteListAssignWrapper serials) {
        ResponseWrapper<List<TmsWhitelist>> wrapper = new ResponseWrapper<>();

        if (serials.getSerials().size() < 5) {
            // process async
            wrapper.setData(deviceService.updateWhitelistBySerialSync(serials.getSerials()));
        } else {
            // process sync
            deviceService.updateWhitelistBySerialAsync(serials.getSerials());
            wrapper.setMessage("Processing in the background");
        }

        return ResponseEntity.ok(wrapper);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<TmsWhitelist>> updateEntity(@Valid @RequestBody TmsWhitelist tmsWhitelist) throws IllegalAccessException, JsonProcessingException {
        return super.updateEntity(tmsWhitelist);
    }


    @ApiOperation(value = "Fetch all Deleted Records",notes = "")
    @ApiImplicitParams({@ApiImplicitParam( name = "size",dataType = "integer",required = false,value = "Pagination size e.g 20",paramType = "query"),
    @ApiImplicitParam(name = "page",dataType = "integer",required = false,value = "Page number e.g 0", paramType = "query"),
    @ApiImplicitParam(name = "sort",dataType = "string",required = false,value = "Field name e.g actionStatus,asc/desc",paramType = "query")})
    @RequestMapping(method = RequestMethod.GET, value = "/deleted")
    public ResponseEntity<ResponseWrapper<Page<TmsWhitelist>>> findAllDeleted(@Valid WhitelistFilter filter, Pageable pg){
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.deviceService.getDeletedWhitelistDevices(filter.getActionStatus(),
                filter.getModelId(), filter.getFrom(), filter.getTo(), filter.getNeedle(), filter.getSerialNo(), filter.getAssignStr(), filter.getUfsBankId(), pg));
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity deleteEntity(@Valid @RequestBody ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ArrayList<>();

        for (BigDecimal id:actions.getIds() ) {
          TmsWhitelist tmsWhitelist = deviceService.getWhitelistById(id);
          if(tmsWhitelist.getAssignStr().equalsIgnoreCase("1")){
              errors.add(tmsWhitelist.getSerialNo());
          }

        }
        if (errors.isEmpty()) {
            return super.deleteEntity(actions);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("The Device(s) Should Be Decommissioned First Before Deletion");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }

    }
}
