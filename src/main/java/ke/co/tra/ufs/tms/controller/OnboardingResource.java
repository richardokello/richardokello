package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.*;
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.*;
import ke.co.tra.ufs.tms.repository.TmsDeviceSimcardRepository;
import ke.co.tra.ufs.tms.repository.TmsDeviceTidMidRepository;
import ke.co.tra.ufs.tms.service.*;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.AlreadyExists;
import ke.co.tra.ufs.tms.utils.exceptions.GeneralBadRequest;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Owori Juma
 */
@RestController
@RequestMapping("/onboarding")
@Api(value = "Application Onboarding")
@CommonsLog
public class OnboardingResource {

    private final LoggerServiceLocal loggerService;
    private final DeviceService deviceService;
    private final BusinessUnitService businessUnitService;
    private final TmsDeviceParamService deviceParamService;
    private final SysConfigService configService;
    private final SharedMethods sharedMethods;
    private final AppManagementService appManagementService;
    private final SchedulerService schedulerService;
    private final ProductService productService;
    private final ParBinProfileService parBinProfileService;
    private final ParMenuProfileService parMenuProfileService;
    private final CustomerService customerService;
    private final CustomerOutletService customerOutletService;
    private final CustomerOwnerService customerOwnerService;
    private final PosUserService posUserService;
    private final PasswordEncoder encoder;
    private final ParGlobalMasterProfileService parGlobalMasterProfileService;
    private final ParFileMenuService parFileMenuService;
    private final ParFileConfigService parFileConfigService;
    private final CustomerConfigFileService customerConfigFileService;
    private final ParDeviceSelectedOptionsService parDeviceSelectedOptionsService;
    private final NotificationService notificationService;
    private final TmsDeviceSimcardRepository deviceSimcardRepository;
    private final TmsDeviceTidMidRepository tidMidRepository;
    private final UfsTerminalHistoryService terminalHistoryService;
    private final MNOService mnoService;
    private final ContactPersonService contactPersonService;

    public OnboardingResource(LoggerServiceLocal loggerService, DeviceService deviceService, BusinessUnitService businessUnitService,
                              TmsDeviceParamService deviceParamService, SysConfigService configService, SharedMethods sharedMethods, AppManagementService appManagementService,
                              SchedulerService schedulerService, ProductService productService, ParBinProfileService parBinProfileService,
                              ParMenuProfileService parMenuProfileService, CustomerService customerService,
                              CustomerOutletService customerOutletService, CustomerOwnerService customerOwnerService,
                              PosUserService posUserService, PasswordEncoder encoder, ParGlobalMasterProfileService parGlobalMasterProfileService, ParFileMenuService parFileMenuService,
                              ParFileConfigService parFileConfigService, CustomerConfigFileService customerConfigFileService, ParDeviceSelectedOptionsService parDeviceSelectedOptionsService,
                              NotificationService notificationService, TmsDeviceSimcardRepository deviceSimcardRepository, TmsDeviceTidMidRepository tidMidRepository, UfsTerminalHistoryService terminalHistoryService,
                              MNOService mnoService, ContactPersonService contactPersonService) {
        this.loggerService = loggerService;
        this.deviceService = deviceService;
        this.businessUnitService = businessUnitService;
        this.deviceParamService = deviceParamService;
        this.configService = configService;
        this.sharedMethods = sharedMethods;
        this.appManagementService = appManagementService;
        this.schedulerService = schedulerService;
        this.productService = productService;
        this.parBinProfileService = parBinProfileService;
        this.parMenuProfileService = parMenuProfileService;
        this.customerService = customerService;
        this.customerOutletService = customerOutletService;
        this.customerOwnerService = customerOwnerService;
        this.posUserService = posUserService;
        this.encoder = encoder;
        this.parGlobalMasterProfileService = parGlobalMasterProfileService;
        this.parFileMenuService = parFileMenuService;
        this.parFileConfigService = parFileConfigService;
        this.customerConfigFileService = customerConfigFileService;
        this.parDeviceSelectedOptionsService = parDeviceSelectedOptionsService;
        this.notificationService = notificationService;
        this.deviceSimcardRepository = deviceSimcardRepository;
        this.tidMidRepository = tidMidRepository;
        this.terminalHistoryService = terminalHistoryService;
        this.mnoService = mnoService;
        this.contactPersonService = contactPersonService;
    }

    @ApiOperation(value = "Create Device", notes = "used to create a device within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createDevice(@ApiParam(value = "Ignore status and deviceId it will be used when fetching Devices")
                                                        @RequestBody @Valid OnboardWrapper onboardWrapper, BindingResult validation) throws NotFoundException, AlreadyExists, IOException {
        ResponseWrapper response = new ResponseWrapper();
        String outletName = null;
        String merchantName = null;


        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Device failed due to validation errors", SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage("Creating new Device failed due to validation errors");
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids()) && onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
            for (TmsDeviceTidsMids tidmid : onboardWrapper.getTmsDeviceTidsMids()) {
                //check where TID and MID exists
                if (deviceService.checkIfTidExists(tidmid.getTid())) {
                    String message = "Creating new Device failed due to the provided"
                            + "TID that already Exists (Device: " + onboardWrapper.getSerialNo() + ")";
                    loggerService.logCreate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
                    throw new AlreadyExists(message, HttpStatus.BAD_REQUEST);
                }
            }
        }

        ValidateMid(onboardWrapper);


        TmsDevice tmsDevice = new TmsDevice();
        tmsDevice.setModelId(deviceService.getModel(onboardWrapper.getModelId()));
        tmsDevice.setPartNumber(onboardWrapper.getPartNumber());
        tmsDevice.setDeviceTypeId(onboardWrapper.getDeviceTypeId());
        tmsDevice.setImeiNo(onboardWrapper.getImeiNo());
        tmsDevice.setMasterProfileId(onboardWrapper.getMasterProfileId());

        if (onboardWrapper.getEstateId() != null) {
            try {
                tmsDevice.setEstateId(businessUnitService.getUnitItem(onboardWrapper.getEstateId()).get());
            } catch (Exception e) {
            }
        }
        tmsDevice.setSerialNo(onboardWrapper.getSerialNo());
        if (Objects.nonNull(onboardWrapper.getOutletIds()) || onboardWrapper.getOutletIds() != null) {

            outletName = customerOutletService.findByOutletId(onboardWrapper.getOutletIds().longValue()).getOutletName();
            merchantName = customerOutletService.findByOutletId(onboardWrapper.getOutletIds().longValue()).getCustomerId().getBusinessName();

            tmsDevice.setOutletIds(onboardWrapper.getOutletIds());
            tmsDevice.setDeviceFieldName(outletName);
            tmsDevice.setBankBranchIds(customerOutletService.findByOutletId(onboardWrapper.getOutletIds().longValue()).getBankBranchIds());
        }
        tmsDevice.setCustomerOwnerName(onboardWrapper.getCustomerOwnerName());
        if (onboardWrapper.getTenantIds() != null) {
            tmsDevice.setTenantIds(onboardWrapper.getTenantIds());
        }
        tmsDevice.setStatus(AppConstants.STATUS_ACTIVE);
        try {
            this.validateDeviceAddons(tmsDevice, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }

        tmsDevice.setReleaseDeviceCount(0);
        tmsDevice.setAction(AppConstants.ACTIVITY_CREATE);
        tmsDevice.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        tmsDevice.setIntrash(AppConstants.NO);
        tmsDevice = deviceService.saveDevice(tmsDevice);
        TmsDevice savedTmsDevice = tmsDevice;


        List<TmsDeviceSimcardWrapper> deviceSims = new ArrayList<>();
        /*Saving SimDetails*/
        if (Objects.nonNull(onboardWrapper.getTmsDeviceSimcards()) && onboardWrapper.getTmsDeviceSimcards().size() > 0) {
            onboardWrapper.getTmsDeviceSimcards().forEach(obj -> {
                TmsDeviceSimcard tmsDeviceSimcard = new TmsDeviceSimcard();
                tmsDeviceSimcard.setDeviceId(savedTmsDevice);
                tmsDeviceSimcard.setMnoIds(obj.getMnoIds());
                tmsDeviceSimcard.setMsisdn(obj.getMsisdn());
                tmsDeviceSimcard.setSerialNo(obj.getSerialNo());
                deviceService.saveTmsDeviceSimcard(tmsDeviceSimcard);
                String mnoProvider = this.mnoService.findMno(obj.getMnoIds()).get().getMnoName();
                deviceSims.add(new TmsDeviceSimcardWrapper(mnoProvider, obj.getSerialNo()));
            });
        }

        List<String> tids = new ArrayList<>();
        List<String> tidMidError = new ArrayList<>();
        /*Saving TIDs And MIDs*/
        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids()) && onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
            onboardWrapper.getTmsDeviceTidsMids().forEach(obj -> {
                TmsDeviceTidsMids tmsDeviceTidMids = new TmsDeviceTidsMids();
                tmsDeviceTidMids.setDeviceIds(savedTmsDevice.getDeviceId().longValue());

                tmsDeviceTidMids.setTid(obj.getTid());
                tids.add(obj.getTid());
                tmsDeviceTidMids.setMid(obj.getMid());
                tmsDeviceTidMids.setCurrencyIds(obj.getCurrencyIds());
                tmsDeviceTidMids.setSwitchIds(obj.getSwitchIds());
                deviceService.saveDeviceTids(tmsDeviceTidMids);
            });
        }

        if (tidMidError.size() > 0) {
            response.setMessage("Creating new Device failed due to the provided TID or MID that already Exists" +
                    new ObjectMapper().writeValueAsString(tidMidError));
            response.setCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }

        //save the tids
        tmsDevice.setTid(String.join(";", tids));
        deviceService.saveDevice(tmsDevice);

        saveAllSelectedOptions(savedTmsDevice.getDeviceId(), onboardWrapper.getDeviceOptionsIds());

        /*Create First Time Pos User*/
        UfsCustomerOwners customerOwners = customerOwnerService.findByCustomerOwnerId(onboardWrapper.getCustomerOwnerId());

        if (Objects.isNull(customerOwners)) {
            throw new NotFoundException("Agent Owner Not Found", HttpStatus.NOT_FOUND);
        } else if (customerOwners.getUserName() != null) {
            UfsPosUser posUserExisting = posUserService.findByCustomerOwnersIdAndDeviceId(onboardWrapper.getCustomerOwnerId(), savedTmsDevice.getDeviceId());
            if (Objects.nonNull(posUserExisting)) {
                throw new AlreadyExists("User Already Exists For That Device", HttpStatus.MULTI_STATUS);
            } else {
                //generate random pin
                String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
                log.info("The generated pin is: " + randomPin);

                //create the user
                UfsPosUser posUser = new UfsPosUser();
                posUser.setUsername(customerOwners.getUserName());
                posUser.setPin(encoder.encode(randomPin));
                posUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
                posUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                posUser.setTmsDeviceId(savedTmsDevice.getDeviceId());
                posUser.setCustomerOwnersId(onboardWrapper.getCustomerOwnerId());
                posUser.setPosRole(AppConstants.POS_SUPERVISOR_ROLE);
                posUser.setPhoneNumber(customerOwners.getDirectorPrimaryContactNumber());
                posUser.setIdNumber(customerOwners.getDirectorIdNumber());
                posUser.setSerialNumber(onboardWrapper.getSerialNo());
                posUser.setFirstTimeUser((short) 1);
                posUser.setMerchantName(merchantName);
                posUser.setOutletName(outletName);

                String[] name = customerOwners.getDirectorName().split("\\s+");
                if (name.length > 0) {
                    posUser.setFirstName(name[0]);
                    if (name.length > 1) {
                        posUser.setOtherName(name[1]);
                    }
                }

                posUserService.savePosUser(posUser);
            }

        }


        if (onboardWrapper.getAppId() != null || onboardWrapper.getMasterProfileId() != null) {
            createSchedule(onboardWrapper, tmsDevice, "/devices/" + tmsDevice.getDeviceId() + "/");
        }

        response.setData(tmsDevice);
        loggerService.logCreate("Creating new Device", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        this.terminalHistoryService.saveHistory(new UfsTerminalHistory(tmsDevice.getSerialNo(), AppConstants.ACTIVITY_ASSIGN_DEVICE, "Terminal Assigned Successfully with TIDS:" + String.join(";", tids) + " And Device Sim Details: " + new ObjectMapper().writeValueAsString(deviceSims), loggerService.getUser(), AppConstants.STATUS_UNAPPROVED, loggerService.getFullName()));


        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create and Assign merchant device", notes = "used to create a device within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/assign-merchant-device")
    @Transactional
    public ResponseEntity<ResponseWrapper> createDeviceForMerchant(@ApiParam(value = "Ignore status and deviceId it will be used when fetching Devices")
                                                                   @RequestBody @Valid MerchantDeviceOnboard onboardWrapper, BindingResult validation) throws NotFoundException, AlreadyExists, IOException {
        ResponseWrapper response = new ResponseWrapper();
        String outletName = null;
        String merchantName = null;

        UfsCustomer customer = customerService.findById(onboardWrapper.getCustomerId()).orElse(null);
        if (customer == null) {
            String message = "Creating new Device failed due to No Customer found with ID:" + onboardWrapper.getCustomerId();
            loggerService.logCreate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
            response.setCode(404);
            response.setMessage(message);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        if (customer.getMid() == null) {
            String message = "Creating new Device failed because the customer has no MID attached to his profile, Kindly update customers MID the retry";
            loggerService.logCreate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage(message);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Device failed due to validation errors", SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage("Creating new Device failed due to validation errors");
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids()) && onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
            for (TmsDeviceTidsMids tidmid : onboardWrapper.getTmsDeviceTidsMids()) {
                //check where TID and MID exists
                if (deviceService.checkIfTidExists(tidmid.getTid())) {
                    String message = "Creating new Device failed due to the provided"
                            + "TID that already Exists (Device: " + onboardWrapper.getSerialNo() + ")";
                    loggerService.logCreate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
                    throw new AlreadyExists(message, HttpStatus.BAD_REQUEST);
                }
            }
        }

        TmsDevice tmsDevice = new TmsDevice();
        tmsDevice.setModelId(deviceService.getModel(onboardWrapper.getModelId()));
        tmsDevice.setMasterProfileId(onboardWrapper.getMasterProfileId());

        if (onboardWrapper.getEstateId() != null) {
            try {
                tmsDevice.setEstateId(businessUnitService.getUnitItem(onboardWrapper.getEstateId()).get());
            } catch (Exception e) {
            }
        }
        tmsDevice.setSerialNo(onboardWrapper.getSerialNo());


        Long outletId = null;

        if (onboardWrapper.getOutletIds() != null) {
            outletId = onboardWrapper.getOutletIds();
        } else {
            List<UfsCustomerOutlet> ufsCustomerOutlets = customerOutletService.findOutletsByCustomerIdsAndIntrash(new BigDecimal(customer.getId()), AppConstants.NO);
            if (ufsCustomerOutlets.size() > 0) {
                outletId = ufsCustomerOutlets.get(0).getId();
            }
        }

        if (outletId != null) {
            outletName = customerOutletService.findByOutletId(outletId).getOutletName();
            merchantName = customerOutletService.findByOutletId(outletId).getCustomerId().getBusinessName();

            tmsDevice.setOutletIds(new BigDecimal(outletId));
            tmsDevice.setDeviceFieldName(outletName);
            tmsDevice.setBankBranchIds(customerOutletService.findByOutletId(outletId).getBankBranchIds());
        }

        tmsDevice.setCustomerOwnerName((outletName != null) ? outletName : customer.getBusinessName());

        tmsDevice.setStatus(AppConstants.STATUS_ACTIVE);
        try {
            this.validateDeviceAddons(tmsDevice, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }

        tmsDevice.setReleaseDeviceCount(0);
        tmsDevice.setAction(AppConstants.ACTIVITY_CREATE);
        tmsDevice.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        tmsDevice.setIntrash(AppConstants.NO);
        tmsDevice = deviceService.saveDevice(tmsDevice);
        TmsDevice savedTmsDevice = tmsDevice;


        List<TmsDeviceSimcardWrapper> deviceSims = new ArrayList<>();
        /*Saving SimDetails*/
        if (Objects.nonNull(onboardWrapper.getTmsDeviceSimcards()) && onboardWrapper.getTmsDeviceSimcards().size() > 0) {
            onboardWrapper.getTmsDeviceSimcards().forEach(obj -> {
                TmsDeviceSimcard tmsDeviceSimcard = new TmsDeviceSimcard();
                tmsDeviceSimcard.setDeviceId(savedTmsDevice);
                tmsDeviceSimcard.setMnoIds(obj.getMnoIds());
                tmsDeviceSimcard.setMsisdn(obj.getMsisdn());
                tmsDeviceSimcard.setSerialNo(obj.getSerialNo());
                deviceService.saveTmsDeviceSimcard(tmsDeviceSimcard);
                String mnoProvider = this.mnoService.findMno(obj.getMnoIds()).get().getMnoName();
                deviceSims.add(new TmsDeviceSimcardWrapper(mnoProvider, obj.getSerialNo()));
            });
        }

        List<String> tids = new ArrayList<>();
        List<String> tidMidError = new ArrayList<>();
        /*Saving TIDs And MIDs*/
        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids()) && onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
            onboardWrapper.getTmsDeviceTidsMids().forEach(obj -> {
                TmsDeviceTidsMids tmsDeviceTidMids = new TmsDeviceTidsMids();
                tmsDeviceTidMids.setDeviceIds(savedTmsDevice.getDeviceId().longValue());

                tmsDeviceTidMids.setTid(obj.getTid());
                tids.add(obj.getTid());
                if (obj.getMid() != null) {
                    tmsDeviceTidMids.setMid(obj.getMid());
                } else {
                    tmsDeviceTidMids.setMid(customer.getMid());
                }
                tmsDeviceTidMids.setCurrencyIds(obj.getCurrencyIds());
                tmsDeviceTidMids.setSwitchIds(obj.getSwitchIds());
                deviceService.saveDeviceTids(tmsDeviceTidMids);
            });
        }

        if (tidMidError.size() > 0) {
            response.setMessage("Creating new Device failed due to the provided TID that already Exists" +
                    new ObjectMapper().writeValueAsString(tidMidError));
            response.setCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }

        //save the tids
        tmsDevice.setTid(String.join(";", tids));
        deviceService.saveDevice(tmsDevice);

        saveAllSelectedOptions(savedTmsDevice.getDeviceId(), onboardWrapper.getDeviceOptionsIds());


        List<UfsCustomerOwners> ufsCustomerOwnerz = customerOwnerService.findByCustomerId(customer.getId());

        Long ownerId = null;
        if (ufsCustomerOwnerz.size() > 0) {
            ownerId = ufsCustomerOwnerz.get(0).getId();
        }

        List<UfsContactPerson> contactPersons = contactPersonService.getAllContactPersonByCustomerId(new BigDecimal(customer.getId()));
        Long contactPersonId = null;
        if (contactPersons.size() > 0) {
            contactPersonId = contactPersons.get(0).getId();
        }

        /*Create First Time Pos User*/
        UfsCustomerOwners customerOwners = customerOwnerService.findByCustomerOwnerId(ownerId);

        if (Objects.isNull(customerOwners)) {
            //throw new NotFoundException("Agent Owner Not Found", HttpStatus.NOT_FOUND);
        } else if (customerOwners.getUserName() != null) {
            UfsPosUser posUserExisting = posUserService.findByCustomerOwnersIdAndDeviceId(ownerId, savedTmsDevice.getDeviceId());
            if (Objects.nonNull(posUserExisting)) {
                loggerService.logCreate("Failed to create user for device with serial no :" + onboardWrapper.getSerialNo(), SharedMethods.getEntityName(UfsPosUser.class), posUserExisting.getPosUserId(), AppConstants.STATUS_FAILED);
            } else {
                //generate random pin
                String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
                log.error("The generated pin is: " + randomPin);

                //create the user
                UfsPosUser posUser = new UfsPosUser();
                posUser.setUsername(customerOwners.getUserName());
                posUser.setPin(encoder.encode(randomPin));
                posUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
                posUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                posUser.setTmsDeviceId(savedTmsDevice.getDeviceId());
                posUser.setCustomerOwnersId(ownerId);
                posUser.setPosRole(AppConstants.POS_SUPERVISOR_ROLE);
                posUser.setPhoneNumber(customerOwners.getDirectorPrimaryContactNumber());
                posUser.setIdNumber(customerOwners.getDirectorIdNumber());
                posUser.setSerialNumber(onboardWrapper.getSerialNo());
                if (contactPersonId != null) {
                    posUser.setContactPersonId(contactPersonId);
                }
                posUser.setFirstTimeUser((short) 1);
                posUser.setMerchantName(merchantName);
                posUser.setOutletName(outletName);

                String[] name = customerOwners.getDirectorName().split("\\s+");
                if (name.length > 0) {
                    posUser.setFirstName(name[0]);
                    if (name.length > 1) {
                        posUser.setOtherName(name[1]);
                    }
                }
                posUserService.savePosUser(posUser);
            }

        }


        if (onboardWrapper.getAppId() != null || onboardWrapper.getMasterProfileId() != null) {
            createSchedule(onboardWrapper, tmsDevice, "/devices/" + tmsDevice.getDeviceId() + "/");
        }

        response.setData(tmsDevice);
        loggerService.logCreate("Creating new Device", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        this.terminalHistoryService.saveHistory(new UfsTerminalHistory(tmsDevice.getSerialNo(), AppConstants.ACTIVITY_ASSIGN_DEVICE, "Terminal Assigned Successfully with TIDS:" + String.join(";", tids) + " And Device Sim Details: " + new ObjectMapper().writeValueAsString(deviceSims), loggerService.getUser(), AppConstants.STATUS_UNAPPROVED, loggerService.getFullName()));


        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    private void saveAllSelectedOptions(BigDecimal deviceId, List<BigDecimal> deviceOptionsIds) {
        // save device options
        if (Objects.nonNull(deviceOptionsIds) && deviceOptionsIds.size() > 0) {
            List<ParDeviceSelectedOptions> selectedOptions = deviceOptionsIds.stream()
                    .map(option -> {
                        ParDeviceSelectedOptions deviceOptions = new ParDeviceSelectedOptions();
                        deviceOptions.setDeviceOptionId(option);
                        deviceOptions.setDeviceId(deviceId);
                        return deviceOptions;
                    })
                    .collect(Collectors.toList());
            parDeviceSelectedOptionsService.saveAll(selectedOptions);
        }
    }

    @ApiOperation(value = "Update Device", notes = "used to create a device within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/update-device")
    @Transactional
    public ResponseEntity<ResponseWrapper> updateDevice(@ApiParam(value = "Ignore status and mnoId it will be used when fetching MNOs")
                                                        @Valid @RequestBody OnboardWrapper onboardWrapper, BindingResult validation) throws IOException, AlreadyExists {

        ResponseWrapper response = new ResponseWrapper();
        String outletName;

        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Device failed due to validation errors", SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage("Validation Error Occured " + new ObjectMapper().writeValueAsString(SharedMethods.getFieldMapErrors(validation)));
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        TmsDevice tmsDevice = deviceService.getDevice(onboardWrapper.getDeviceId()).get();


        if (tmsDevice == null) {
            loggerService.logCreate("Failed to Update Device (Device id: " + tmsDevice.getDeviceId() + "). device doesn't exist",
                    SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate Device with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        if (tmsDevice.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logCreate("Failed to Update Device (Device id: " + tmsDevice.getDeviceId() + "). Has Unapproved Actions",
                    SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Failed to Update Device. Record Has Unapproved Actions");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids()) && onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
            for (TmsDeviceTidsMids tidmid : onboardWrapper.getTmsDeviceTidsMids()) {
                //check where TID and MID exists
                if (deviceService.checkIfTidExistsByDeviceIds(tidmid.getTid(), tidmid.getDeviceIds())) {
                    String message = "Updating Device failed due to the provided"
                            + "TID that already Exists (Device: " + onboardWrapper.getSerialNo() + ") and matched to another device";
                    loggerService.logUpdate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
                    throw new AlreadyExists(message, HttpStatus.BAD_REQUEST);
                }
            }
        }

        ValidateMid(onboardWrapper);

        if (Objects.nonNull(onboardWrapper.getTmsDeviceSimcards())) {
            deviceSimcardRepository.deleteAllByDeviceId(tmsDevice);
            onboardWrapper.getTmsDeviceSimcards().forEach(obj -> {
                TmsDeviceSimcard tmsDeviceSimcard = new TmsDeviceSimcard();
                tmsDeviceSimcard.setDeviceId(tmsDevice);
                tmsDeviceSimcard.setMnoIds(obj.getMnoIds());
                tmsDeviceSimcard.setMsisdn(obj.getMsisdn());
                tmsDeviceSimcard.setSerialNo(obj.getSerialNo());
                deviceService.saveTmsDeviceSimcard(tmsDeviceSimcard);
            });
        }

        List<String> tids = new ArrayList<>();
        if (Objects.nonNull(onboardWrapper.getTmsDeviceTidsMids())) {
            tidMidRepository.deleteAllByDeviceId(tmsDevice);
            onboardWrapper.getTmsDeviceTidsMids().forEach(obj -> {
                tids.add(obj.getTid());
                TmsDeviceTidsMids tmsDeviceTidMids = new TmsDeviceTidsMids();
                tmsDeviceTidMids.setDeviceIds(onboardWrapper.getDeviceId().longValue());
                tmsDeviceTidMids.setTid(obj.getTid());
                if (obj.getMid() != null) {
                    tmsDeviceTidMids.setMid(obj.getMid());
                }
                tmsDeviceTidMids.setCurrencyIds(obj.getCurrencyIds());
                tmsDeviceTidMids.setSwitchIds(obj.getSwitchIds());
                deviceService.saveDeviceTids(tmsDeviceTidMids);
            });
        }

        tmsDevice.setModelId(deviceService.getModel(onboardWrapper.getModelId()));
        if (onboardWrapper.getEstateId() != null) {
            tmsDevice.setEstateId(businessUnitService.getUnitItem(onboardWrapper.getEstateId()).get());
        }


        tmsDevice.setModelId(deviceService.getModel(onboardWrapper.getModelId()));
        tmsDevice.setPartNumber(onboardWrapper.getPartNumber());
        tmsDevice.setDeviceTypeId(onboardWrapper.getDeviceTypeId());
        tmsDevice.setImeiNo(onboardWrapper.getImeiNo());
        tmsDevice.setMasterProfileId(onboardWrapper.getMasterProfileId());

        if (onboardWrapper.getEstateId() != null) {
            try {
                tmsDevice.setEstateId(businessUnitService.getUnitItem(onboardWrapper.getEstateId()).get());
            } catch (Exception e) {
            }
        }
        tmsDevice.setSerialNo(onboardWrapper.getSerialNo());
        if (Objects.nonNull(onboardWrapper.getOutletIds()) || onboardWrapper.getOutletIds() != null) {

            outletName = customerOutletService.findByOutletId(onboardWrapper.getOutletIds().longValue()).getOutletName();

            tmsDevice.setOutletIds(onboardWrapper.getOutletIds());
            tmsDevice.setDeviceFieldName(outletName);
            tmsDevice.setBankBranchIds(customerOutletService.findByOutletId(onboardWrapper.getOutletIds().longValue()).getBankBranchIds());
        }
        tmsDevice.setCustomerOwnerName(onboardWrapper.getCustomerOwnerName());

        tmsDevice.setAction(AppConstants.ACTIVITY_UPDATE);
        tmsDevice.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        tmsDevice.setIntrash(AppConstants.NO);
        //save the tids
        tmsDevice.setTid(String.join(";", tids));
        deviceService.saveDevice(tmsDevice);

        // update device options
        parDeviceSelectedOptionsService.deleteAll(tmsDevice.getDeviceId());
        saveAllSelectedOptions(tmsDevice.getDeviceId(), onboardWrapper.getDeviceOptionsIds());

        if (onboardWrapper.getMasterProfileId() != null) {
            createSchedule(onboardWrapper, tmsDevice, "/devices/" + tmsDevice.getDeviceId() + "/");
        }

        response.setData(tmsDevice);
        loggerService.logUpdate("Updating Device", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
        response.setCode(200);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    private void ValidateMid(@RequestBody @ApiParam("Ignore status and mnoId it will be used when fetching MNOs") @Valid OnboardWrapper onboardWrapper) throws AlreadyExists {
        if (Objects.nonNull(onboardWrapper.getOutletIds()) || onboardWrapper.getOutletIds() != null) {
            if (onboardWrapper.getTmsDeviceTidsMids().size() > 0) {
                for (TmsDeviceTidsMids tidmid : onboardWrapper.getTmsDeviceTidsMids()) {
                    if (deviceService.checkIfMidExistsOnOtherCustomer(tidmid.getMid(), onboardWrapper.getOutletIds())) {
                        String message = "Creating new Device failed due to the provided"
                                + "MID that already Exists (Device: " + onboardWrapper.getSerialNo() + ")";
                        loggerService.logCreate(message, SharedMethods.getEntityName(TmsDevice.class), onboardWrapper.getSerialNo(), AppConstants.STATUS_FAILED);
                        throw new AlreadyExists(message, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
    }

    @ApiOperation(value = "Add Device Tasks", notes = "used to add a device tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/add-task")
    @Transactional
    public ResponseEntity<ResponseWrapper> addDeviceTasks(
            @Valid AddTaskWrapper addTaskWrapper, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new task failed due to validation errors", SharedMethods.getEntityName(TmsDevice.class), addTaskWrapper.getDeviceId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        TmsDevice tmsDevice = deviceService.getDevice(addTaskWrapper.getDeviceId()).get();

        if (tmsDevice.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logUpdate("Failed to Create Device (Device id: " + tmsDevice.getDeviceId() + ") task. device has unapproved actions",
                    SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Failed. Device has unapproved actions");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        if (tmsDevice == null) {
            loggerService.logUpdate("Failed to Update Device (Device id: " + tmsDevice.getDeviceId() + "). device doesn't exist",
                    SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate Device with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        tmsDevice.setAction(AppConstants.ACTIVITY_TASK);
        tmsDevice.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        tmsDevice.setIntrash(AppConstants.NO);

        deviceService.saveDevice(tmsDevice);

        if (addTaskWrapper.getAppId() != null || addTaskWrapper.getFile() != null || addTaskWrapper.getMasterProfileId() != null) {
            TmsDeviceTask dtk = deviceService.findTopByDeviceIdOrderByTaskIdDesc(tmsDevice);
            if (dtk != null) {
                TmsScheduler sched = dtk.getScheduleId();
                if (sched.getScheduleType().equals(AppConstants.MANUAL_SCHEDULE) && dtk.getDownloadStatus().equals("PENDING")) {
                    sched.setNoFiles(sched.getNoFiles() + 1L);
                    schedulerService.saveSchedule(sched);
                    try {
                        transferAndCopyFilesTaskNew(addTaskWrapper, tmsDevice, dtk, sched.getDirPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    createScheduleTask(addTaskWrapper, tmsDevice, "/devices/" + tmsDevice.getDeviceId() + "/");
                }
            } else {
                createScheduleTask(addTaskWrapper, tmsDevice, "/devices/" + tmsDevice.getDeviceId() + "/");
            }
        }

        response.setData(tmsDevice);
        loggerService.logCreateTask("Creating Device Task", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    private void validateDeviceAddons(TmsDevice tmsDevice, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (deviceService.getWhitelist(tmsDevice.getSerialNo()) == null) {
                loggerService.logCreate("Creating new Device failed due to the provided"
                        + "Device Not white listed (Device: " + tmsDevice.getSerialNo() + ")", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getSerialNo(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest("Device Not white listed", HttpStatus.CONFLICT);
            }

            TmsDevice dv = deviceService.getDevicebySerial(tmsDevice.getSerialNo());
            if (dv != null) {

                if (!dv.getStatus().equals("Inactive")) {
                    loggerService.logCreate("Creating new Device failed due to the provided"
                            + "Serial Number already Exists (Device: " + tmsDevice.getSerialNo() + ")", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getSerialNo(), AppConstants.STATUS_FAILED);
                    throw new GeneralBadRequest("Serial Number already Exists", HttpStatus.CONFLICT);
                }
            }
        }
    }


    private void transferAndCopyFiles(DevicesWrapper onboardWrapper, TmsDevice tmsDevice, TmsDeviceTask deviceTask) throws IOException {
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();

        try {
            rootPath = rootPath + "devices/" + tmsDevice.getDeviceId() + "/" + deviceTask.getTaskId() + "/";
            File d = new File(rootPath);
            if (d.exists()) {
                sharedMethods.deleteDirectory(rootPath);
            }

            if (onboardWrapper.getFile() != null) {
                for (MultipartFile mf : onboardWrapper.getFile()) {
                    sharedMethods.store(mf, rootPath);
                }
            }

            // use master profile and device id to generate parameter file specific to device
            if (onboardWrapper.getMasterProfileId() != null) {
                Optional<ParGlobalMasterProfile> optionalMaster = parGlobalMasterProfileService.findById(onboardWrapper.getMasterProfileId());
                if (optionalMaster.isPresent()) {
                    ParGlobalMasterProfile parGlobalMasterProfile = optionalMaster.get();

                    // generate menu profile
                    parFileMenuService.generateMenuFileAsync(new MenuFileRequest(onboardWrapper.getModelId(), parGlobalMasterProfile.getMenuProfileId()), rootPath);

                    // generate all global configs related to master profile
                    for (ParGlobalMasterChildProfile config : parGlobalMasterProfile.getChildProfiles()) {
                        parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(), onboardWrapper.getModelId(), rootPath);
                    }
                }
                customerConfigFileService.generateCustomerFile(tmsDevice.getDeviceId(), rootPath);
                loggerService.logCreate("Saving new App Files", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);

            }

            // generate customer specific device config file
            if (onboardWrapper.getAppId() != null) {
                TmsApp tmsApp = appManagementService.findTmsApp(onboardWrapper.getAppId()).get();
                String fromPath = tmsApp.getNotesFilepath();
                File f = new File(fromPath);
                sharedMethods.moveFile(fromPath, rootPath + f.getName(), rootPath);
                loggerService.logCreate("Tranfered App " + f.getName(), SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSchedule(DevicesWrapper onboardWrapper, TmsDevice tmsDevice, String rootPath) {
        long filecount;
        if (onboardWrapper.getFile() != null) {
            filecount = onboardWrapper.getFile().length + 1;
        } else {
            filecount = 1;
        }

        TmsScheduler scheduler = new TmsScheduler();
        scheduler.setAction(AppConstants.ACTIVITY_CREATE);
        scheduler.setActionStatus(AppConstants.STATUS_APPROVED);
        scheduler.setAppId(onboardWrapper.getAppId());
        scheduler.setModelId(deviceService.getModel(onboardWrapper.getModelId()));
        scheduler.setDirPath(rootPath);
        scheduler.setScheduleType(AppConstants.MANUAL_SCHEDULE);
        scheduler.setStatus(AppConstants.STATUS_NEW);
        scheduler.setNoFiles(filecount);
        scheduler.setDownloadType(AppConstants.DOWNLOAD_APP_AND_FILES);
        scheduler.setIntrash(AppConstants.NO);
        scheduler.setScheduledTime(new Date());
        scheduler.setProductId(productService.getProduct(onboardWrapper.getProductId()).get());

        //save the manual schedule
        schedulerService.saveSchedule(scheduler);

        loggerService.logCreate("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);

        TmsDeviceTask deviceTask = new TmsDeviceTask();
        deviceTask.setDeviceId(tmsDevice);
        deviceTask.setScheduleId(scheduler);
        deviceTask.setDownloadStatus("PENDING");
        deviceTask.setIntrash(AppConstants.NO);

        //persist the device task
        schedulerService.saveDeviceTask(deviceTask);

        rootPath = rootPath + deviceTask.getTaskId() + "/";
        scheduler.setDirPath(rootPath);
        schedulerService.saveSchedule(scheduler);

        if (onboardWrapper.getAppId() != null || onboardWrapper.getMasterProfileId() != null || onboardWrapper.getFile() != null) {
            try {
                transferAndCopyFiles(onboardWrapper, tmsDevice, deviceTask);
            } catch (IOException ex) {
                Logger.getLogger(OnboardingResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loggerService.logCreate("Creating new Device Task", SharedMethods.getEntityName(TmsDeviceTask.class), deviceTask.getTaskId(), AppConstants.STATUS_COMPLETED);
    }

    private void createScheduleTask(AddTaskWrapper addTaskWrapper, TmsDevice tmsDevice, String rootPath) {
        long filecount = 0;
        if (addTaskWrapper.getDownloadType().equals("App Only")) {
            filecount = 1;
        } else {
            if (addTaskWrapper.getMasterProfileId() != null) {
                filecount += 1;
            }

            if (addTaskWrapper.getFile() != null) {
                filecount += addTaskWrapper.getFile().length + 1;
            }
        }
        TmsScheduler scheduler = new TmsScheduler();
        scheduler.setAction(AppConstants.ACTIVITY_CREATE);
        scheduler.setActionStatus(AppConstants.STATUS_APPROVED);
        scheduler.setAppId(addTaskWrapper.getAppId());
        scheduler.setModelId(deviceService.getModel(addTaskWrapper.getModelId()));
        scheduler.setDirPath(rootPath);
        scheduler.setScheduleType(AppConstants.MANUAL_SCHEDULE);
        scheduler.setStatus(AppConstants.STATUS_NEW);
        scheduler.setNoFiles(filecount);
        scheduler.setDownloadType(AppConstants.DOWNLOAD_APP_AND_FILES);
        scheduler.setIntrash(AppConstants.NO);
        scheduler.setScheduledTime(new Date());

        //save the manual schedule
        schedulerService.saveSchedule(scheduler);

        loggerService.logCreate("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);

        TmsDeviceTask deviceTask = new TmsDeviceTask();
        deviceTask.setDeviceId(tmsDevice);
        deviceTask.setScheduleId(scheduler);
        deviceTask.setDownloadStatus("PENDING");
        deviceTask.setIntrash(AppConstants.NO);

        //persist the device task
        schedulerService.saveDeviceTask(deviceTask);

        rootPath = rootPath + deviceTask.getTaskId() + '/';
        scheduler.setDirPath(rootPath);
        schedulerService.saveSchedule(scheduler);

        if (addTaskWrapper.getAppId() != null || addTaskWrapper.getFile() != null || addTaskWrapper.getMasterProfileId() != null) {
            try {
                transferAndCopyFilesTask(addTaskWrapper, tmsDevice, deviceTask);
            } catch (IOException ex) {
                Logger.getLogger(OnboardingResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loggerService.logCreate("Creating new Device Task", SharedMethods.getEntityName(TmsDeviceTask.class), deviceTask.getTaskId(), AppConstants.STATUS_COMPLETED);
    }

    private void transferAndCopyFilesTask(AddTaskWrapper addTaskWrapper, TmsDevice tmsDevice, TmsDeviceTask deviceTask) throws IOException {
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
        try {
            rootPath = rootPath + "devices/" + tmsDevice.getDeviceId() + "/" + deviceTask.getTaskId() + "/";
            File d = new File(rootPath);
            if (d.exists()) {
                sharedMethods.deleteDirectory(rootPath);
            }

            if (addTaskWrapper.getFile() != null) {
                for (MultipartFile mf : addTaskWrapper.getFile()) {
                    sharedMethods.store(mf, rootPath);
                }
            }

            // use master profile and device id to generate parameter file specific to device
            if (addTaskWrapper.getMasterProfileId() != null) {
                Optional<ParGlobalMasterProfile> optionalMaster = parGlobalMasterProfileService.findById(addTaskWrapper.getMasterProfileId());
                if (optionalMaster.isPresent()) {
                    ParGlobalMasterProfile parGlobalMasterProfile = optionalMaster.get();
                    // generate menu profile
                    if (parGlobalMasterProfile.getMenuProfileId() != null) {
                        parFileMenuService.generateMenuFileAsync(new MenuFileRequest(addTaskWrapper.getModelId(), parGlobalMasterProfile.getMenuProfileId()), rootPath);
                    }
                    // generate all global configs related to master profile
                    if (!parGlobalMasterProfile.getChildProfiles().isEmpty()) {
                        for (ParGlobalMasterChildProfile config : parGlobalMasterProfile.getChildProfiles()) {
//                            parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(),    new GlobalConfigFileRequest(addTaskWrapper.getModelId(), config.getId()), rootPath);
                            parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(), addTaskWrapper.getModelId(), rootPath);
                        }
                    }
                }
            }

            // generate customer specific device config file
            customerConfigFileService.generateCustomerFile(addTaskWrapper.getDeviceId(), rootPath);
            loggerService.logCreate("Saving new App Files", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);

            if (addTaskWrapper.getAppId() != null) {
                TmsApp tmsApp = appManagementService.findTmsApp(addTaskWrapper.getAppId()).get();
                String fromPath = tmsApp.getNotesFilepath();
                File f = new File(fromPath);
                sharedMethods.moveFile(fromPath, rootPath + f.getName(), rootPath);

                loggerService.logCreate("Tranfered App " + f.getName(), SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transferAndCopyFilesTaskNew(AddTaskWrapper addTaskWrapper, TmsDevice tmsDevice, TmsDeviceTask deviceTask, String path) throws IOException {
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
        try {
            rootPath = rootPath + path;
            File d = new File(rootPath);
            if (d.exists()) {
                sharedMethods.deleteDirectory(rootPath);
            }
            if (addTaskWrapper.getFile() != null) {
                for (MultipartFile mf : addTaskWrapper.getFile()) {
                    sharedMethods.store(mf, rootPath);
                }
            }
            // use master profile and device id to generate parameter file specific to device
            if (addTaskWrapper.getMasterProfileId() != null) {
                Optional<ParGlobalMasterProfile> optionalMaster = parGlobalMasterProfileService.findById(addTaskWrapper.getMasterProfileId());
                if (optionalMaster.isPresent()) {
                    ParGlobalMasterProfile parGlobalMasterProfile = optionalMaster.get();
                    // generate menu profile
                    parFileMenuService.generateMenuFileAsync(new MenuFileRequest(addTaskWrapper.getModelId(), parGlobalMasterProfile.getMenuProfileId()), rootPath);
                    for (ParGlobalMasterChildProfile config : parGlobalMasterProfile.getChildProfiles()) {
                        parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(), addTaskWrapper.getModelId(), rootPath);
                    }
                }
            }

            // generate customer specific device config file
            if (!addTaskWrapper.getDownloadType().equalsIgnoreCase("App Only")) {
                customerConfigFileService.generateCustomerFile(addTaskWrapper.getDeviceId(), rootPath);
            }

            loggerService.logCreate("Saving new App Files", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);

            if (addTaskWrapper.getAppId() != null) {
                TmsApp tmsApp = appManagementService.findTmsApp(addTaskWrapper.getAppId()).get();
                String fromPath = tmsApp.getNotesFilepath();
                File f = new File(fromPath);
                sharedMethods.moveFile(fromPath, rootPath + f.getName(), rootPath);

                loggerService.logCreate("Tranfered App " + f.getName(), SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "Create Device Simcards List", notes = "used to create a device simcards List within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(value = "/device-simcards", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createDeviceSimcard(@ApiParam(value = "Ignore status and deviceId it will be used when fetching Devices")
                                                               @RequestBody @Valid List<TmsDeviceSimcard> tmsDeviceSimcards, BindingResult validation) throws NotFoundException {

        ResponseWrapper response = new ResponseWrapper();

        if (validation.hasErrors()) {
            response.setCode(400);
            response.setMessage("Creating new Device failed due to validation errors");
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        for (TmsDeviceSimcard devSim : tmsDeviceSimcards) {
            TmsDevice tmsDevice = deviceService.getDeviceById(devSim.getDeviceIds());

            if (tmsDevice == null) {
                throw new NotFoundException("Device Not Found", HttpStatus.NOT_FOUND);
            }
            TmsDeviceSimcard tmsDeviceSimcard = new TmsDeviceSimcard();
            tmsDeviceSimcard.setDeviceId(tmsDevice);
            tmsDeviceSimcard.setMnoIds(devSim.getMnoIds());
            tmsDeviceSimcard.setMsisdn(devSim.getMsisdn());
            tmsDeviceSimcard.setSerialNo(devSim.getSerialNo());
            deviceService.saveTmsDeviceSimcard(tmsDeviceSimcard);
        }

        response.setCode(201);
        response.setMessage("Device Simcards Created Successfully");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Create Device Currency List", notes = "used to create a device currency List within the system")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
            ,
            @ApiResponse(code = 404, message = "Device with specified id doesn't exist")
    })
    @RequestMapping(value = "/device-currency", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createDeviceCurrency(@ApiParam(value = "Ignore status and deviceId it will be used when fetching Devices")
                                                                @RequestBody @Valid List<TmsDeviceCurrency> tmsDeviceCurrencies, BindingResult validation) throws NotFoundException {

        ResponseWrapper response = new ResponseWrapper();

        if (validation.hasErrors()) {
            response.setCode(400);
            response.setMessage("Creating new Device failed due to validation errors");
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        for (TmsDeviceCurrency devCurrency : tmsDeviceCurrencies) {
            TmsDevice tmsDevice = deviceService.getDeviceById(devCurrency.getDeviceIds());

            UfsCurrency ufsCurrency = deviceService.getCurrencyById(devCurrency.getCurrencyIds());

            if (tmsDevice == null) {
                throw new NotFoundException("Device Not Found", HttpStatus.NOT_FOUND);
            }

            if (ufsCurrency == null) {
                throw new NotFoundException("Currency Not Found", HttpStatus.NOT_FOUND);
            }
            TmsDeviceCurrency tmsDeviceCurrency = new TmsDeviceCurrency();
            tmsDeviceCurrency.setDeviceId(tmsDevice);
            tmsDeviceCurrency.setCurrencyId(ufsCurrency);
            deviceService.saveTmsDeviceCurrency(tmsDeviceCurrency);
        }

        response.setCode(201);
        response.setMessage("Device Simcards Created Successfully");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

}
