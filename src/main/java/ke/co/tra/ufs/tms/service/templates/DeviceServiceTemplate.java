package ke.co.tra.ufs.tms.service.templates;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.*;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.UfsTerminalHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Owori Juma
 */
@Service
@Transactional
public class DeviceServiceTemplate implements DeviceService {

    private final MakeRepository makeRepo;
    private final ModelRepository modelRepo;
    private final DeviceTypeRepository typeRepo;
    private final WhitelistRepository whitelistRepo;
    private final BatchRepository batchRepo;
    private final Validator validator;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TmsDeviceRepository deviceRepository;
    private final DeviceCurrencyRepository deviceCurrencyRepository;
    private final DeviceSimcardRepository deviceSimcardRepository;
    private final FTPLogsRepository fTPLogsRepository;
    private final TmsDeviceParamRepository deviceParamRepository;
    private final TmsParamDefinitionRepository definitionRepository;
    private final TmsDeviceFileExtRepository deviceFileExtRepository;
    private final SchedulerRepository schedulerRepository;
    private final DeviceTaskRepository deviceTaskRepository;
    private final ConfigRepository configRepo;
    private final DeviceCustomerDetailsRepository customerDetailsRepository;
    private final CurrencyRepository currencyRepository;
    private String params = "";
    private String menus = "";
    private String bin_config = "";
    private final ParMenuProfileRepository parMenuProfileRepository;
    private final ParBinProfileRepository parBinProfileRepository;
    private final ParBinConfigRepository parBinConfigRepository;
    private final TmsDeviceTidMidRepository tmsDeviceTidRepository;
    private final TmsDeviceSimcardRepository tmsDeviceSimcardRepository;
    private final UfsCurrencyRepository ufsCurrencyRepository;
    @Value("${default.estate}")
    private BigDecimal estate;
    @Value("${default.business-unit}")
    private BigDecimal businessUnit;
    private final BusinessUnitItemRepository businessUnitItemRepository;

    private final UfsTerminalHistoryService terminalHistoryService;
    private final UfsCustomerOutletRepository customerOutletRepository;
    private final UfsContactPersonRepository contactPersonRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final MakeRepository makeRepository;

    public DeviceServiceTemplate(MakeRepository makeRepo, ModelRepository modelRepo, FTPLogsRepository fTPLogsRepository,
                                 DeviceTypeRepository typeRepo, WhitelistRepository whitelistRepo, DeviceSimcardRepository deviceSimcardRepository,
                                 BatchRepository batchRepo, Validator validator, TmsDeviceRepository deviceRepository, DeviceCurrencyRepository deviceCurrencyRepository, TmsDeviceParamRepository deviceParamRepository,
                                 TmsParamDefinitionRepository definitionRepository, TmsDeviceFileExtRepository deviceFileExtRepository,
                                 SchedulerRepository schedulerRepository, DeviceTaskRepository deviceTaskRepository, ConfigRepository configRepo, DeviceCustomerDetailsRepository customerDetailsRepository, CurrencyRepository currencyRepository, ParMenuProfileRepository parMenuProfileRepository, ParBinProfileRepository parBinProfileRepository
            , ParBinConfigRepository parBinConfigRepository, TmsDeviceTidMidRepository tmsDeviceTidRepository, TmsDeviceSimcardRepository tmsDeviceSimcardRepository, UfsCurrencyRepository ufsCurrencyRepository, BusinessUnitItemRepository businessUnitItemRepository, UfsTerminalHistoryService terminalHistoryService,
                                 UfsCustomerOutletRepository customerOutletRepository, UfsContactPersonRepository contactPersonRepository, DeviceTypeRepository deviceTypeRepository, MakeRepository makeRepository) {
        this.makeRepo = makeRepo;
        this.modelRepo = modelRepo;
        this.typeRepo = typeRepo;
        this.whitelistRepo = whitelistRepo;
        this.batchRepo = batchRepo;
        this.validator = validator;
        this.deviceRepository = deviceRepository;
        this.deviceCurrencyRepository = deviceCurrencyRepository;
        this.deviceSimcardRepository = deviceSimcardRepository;
        this.fTPLogsRepository = fTPLogsRepository;
        this.deviceParamRepository = deviceParamRepository;
        this.definitionRepository = definitionRepository;
        this.deviceFileExtRepository = deviceFileExtRepository;
        this.schedulerRepository = schedulerRepository;
        this.deviceTaskRepository = deviceTaskRepository;
        this.configRepo = configRepo;
        this.customerDetailsRepository = customerDetailsRepository;
        this.currencyRepository = currencyRepository;
        this.parMenuProfileRepository = parMenuProfileRepository;
        this.parBinProfileRepository = parBinProfileRepository;
        this.parBinConfigRepository = parBinConfigRepository;
        this.tmsDeviceTidRepository = tmsDeviceTidRepository;
        this.tmsDeviceSimcardRepository = tmsDeviceSimcardRepository;
        this.ufsCurrencyRepository = ufsCurrencyRepository;
        this.businessUnitItemRepository = businessUnitItemRepository;
        this.terminalHistoryService = terminalHistoryService;
        this.customerOutletRepository = customerOutletRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.makeRepository = makeRepository;
    }

    @Override
    public List<UfsDeviceMake> findAllByDeviceMake(String deviceMake) {
        return makeRepository.findAllByVendorNameAndIntrash(deviceMake,AppConstants.NO);
    }

    @Override
    public List<UfsDeviceType> findByTypeAndIntrash(String type) {
        return deviceTypeRepository.findAllByTypeAndIntrash(type,AppConstants.NO);
    }

    @Override
    public UfsCurrency getCurrencyById(BigDecimal id) {
        return ufsCurrencyRepository.findByCurrencyId(id);
    }

    @Override
    public UfsDeviceMake saveMake(UfsDeviceMake make) {
        return this.makeRepo.save(make);
    }

    @Override
    public UfsDeviceMake getMake(BigDecimal makeId) {
        return this.makeRepo.findBymakeIdAndIntrash(makeId, AppConstants.NO);
    }

    @Override
    public Page<UfsDeviceMake> getMake(String actionStatus, String needle, Pageable pg) {
        return this.makeRepo.findAll(actionStatus, needle, AppConstants.NO, pg);
    }

    @Override
    public Page<UfsDeviceModel> getModel(String actionStatus, String makeId, String deviceTypeId, String needle, Pageable pg) {
        return this.modelRepo.findAll(actionStatus, makeId, deviceTypeId, needle, AppConstants.NO, pg);
    }

    @Override
    public Page<UfsDeviceType> getType(String actionStatus, String needle, Pageable pg) {
        return this.typeRepo.findAll(actionStatus, needle, AppConstants.NO, pg);
    }

    @Override
    public UfsDeviceModel getModel(BigDecimal modelId) {
        return this.modelRepo.findBymodelIdAndIntrash(modelId, AppConstants.NO);
    }

    @Override
    public TmsWhitelist getWhitelist(String serialNo) {
        return this.whitelistRepo.findByserialNoAndIntrash(serialNo, AppConstants.NO);
    }

    @Override
    public TmsWhitelist getWhitelistById(BigDecimal id) {
        return this.whitelistRepo.findByIdAndIntrash(id,AppConstants.NO);
    }

    @Override
    public TmsWhitelist saveWhitelist(TmsWhitelist entity) {
        return this.whitelistRepo.save(entity);
    }

    @Override
    public TmsWhitelistBatch saveBatch(TmsWhitelistBatch batch) {
        return this.batchRepo.save(batch);
    }

    @Override
    @Async
    public void processWhitelistUpload(TmsWhitelistBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerServiceLocal loggerService, byte[] file, String ipAddress, String userAgent,Long userId, WhitelistWrapper payload) {
        try {
            List<WhitelistDetails> entities = sharedMethods.convertCsv(WhitelistDetails.class, file);
            long failed = 0;
            long success = 0;
            for (WhitelistDetails entity : entities) {
                log.error("Processing Uploading =======> csv");
                //validate entity
                DirectFieldBindingResult valid = new DirectFieldBindingResult(entity,
                        "WhitelistDetails");
                validator.validate(entity, valid);
                if (valid.hasErrors()) {
                    loggerService.logCreate("Whitelisting a device (Serial number:  "
                                    + entity.getSerialNo() + ") failed. Encountered validation "
                                    + "errors (Errors: " + SharedMethods.getFieldErrorsString(valid)
                                    + ")", SharedMethods.getEntityName(TmsWhitelist.class), null,
                            AppConstants.STATUS_FAILED,userId, ipAddress, userAgent);
                    failed++;
                    continue;
                }
                //check if serial number exists
                TmsWhitelist device = this.whitelistRepo.findByserialNoAndIntrash(entity.getSerialNo(), AppConstants.NO);
                if (device != null) {
                    loggerService.logCreate("Whitelisting a device (Serial number:  "
                                    + entity.getSerialNo() + ") failed. Serial number exists",
                            SharedMethods.getEntityName(TmsWhitelist.class), null,
                            AppConstants.STATUS_FAILED,userId, ipAddress, userAgent);
                    failed++;
                    continue;
                }

                      device = new TmsWhitelist(entity.getSerialNo(),
                        batch.getModel().getModelId(), AppConstants.ACTIVITY_CREATE,
                        AppConstants.ACTION_STATUS_UNCONFIRMED, AppConstants.NO);
                device.setBatch(batch);

                device = whitelistRepo.save(device);
                this.terminalHistoryService.saveHistory(new UfsTerminalHistory(entity.getSerialNo(), AppConstants.ACTIVITY_CREATE, "Terminal Whitelisted Successfully", userId, AppConstants.STATUS_UNAPPROVED,loggerService.getFullName()));

                loggerService.logCreate("Whitelisted device (Serial number:  "
                                + entity.getSerialNo() + ") successfully",
                        SharedMethods.getEntityName(TmsWhitelist.class), device.getId(),
                        AppConstants.STATUS_COMPLETED,userId, ipAddress, userAgent);
                success++;
            }
            batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Processing Device Whitelist upload failed", ex);
            loggerService.logCreate("Processing Device Whitelist failed. This may be "
                            + "due to wrong file format / file content msg:" + ex.getMessage(), SharedMethods.getEntityName(TmsWhitelist.class), null,
                    AppConstants.STATUS_FAILED,userId, ipAddress, userAgent);
            batch.setProcessingStatus(AppConstants.STATUS_FAILED);
        }
        this.batchRepo.save(batch);
    }

    @Override
    public TmsDevice saveDevice(TmsDevice device) {
        return deviceRepository.save(device);
    }

    @Override
    public TmsDevice getDevicebySerial(String serialNo) {
        List<String> actions = new ArrayList<>();
        actions.add(AppConstants.ACTIVITY_DECOMMISSION);
        actions.add(AppConstants.ACTIVITY_RELEASE);
        return deviceRepository.findAllBySerialNoAndIntrash(serialNo, AppConstants.NO, actions);
    }

    @Override
    public Optional<TmsDevice> getDevice(BigDecimal deviceId) {
        return deviceRepository.findById(deviceId);
    }


    @Override
    public Page<TmsWhitelist> getWhitelist(String actionStatus, String modelId,
                                           Date from, Date to, String needle, String serialNo, String assignStr, Pageable pg) {
        return this.whitelistRepo.findAll(actionStatus, modelId, from, to, needle.toLowerCase(), AppConstants.NO, serialNo, assignStr, pg);
    }

    @Override
    public Page<TmsWhitelist> getDeletedWhitelistDevices(String actionStatus, String modelId, Date from, Date to, String needle, String serialNo, String assignStr, Pageable pg) {
        return this.whitelistRepo.findAll(actionStatus, modelId, from, to, needle.toLowerCase(), AppConstants.YES, serialNo, assignStr, pg);

    }


    @Override
    public Page<TmsDevice> getDevices(String action, String actionStatus, Date from, Date to, String needle, String status, Pageable pg) {
        return deviceRepository.findAll(action, actionStatus, needle, from, to, AppConstants.NO, status, pg);
    }

    @Override
    public List<TmsDevice> findByestateId(TmsEstateItem unitItemId) {
        return deviceRepository.findByestateId(unitItemId);
    }

    @Override
    public TmsDeviceCurrency saveTmsDeviceCurrency(TmsDeviceCurrency deviceCurrency) {
        return deviceCurrencyRepository.save(deviceCurrency);
    }

    @Override
    public TmsDeviceSimcard saveTmsDeviceSimcard(TmsDeviceSimcard deviceSimcard) {
        return deviceSimcardRepository.save(deviceSimcard);
    }

    @Override
    public TmsDeviceCurrency findDeviceCurrencyBydeviceId(TmsDevice deviceId) {
        return deviceCurrencyRepository.findBydeviceId(deviceId);
    }

    @Override
    public TmsDeviceSimcard findDeviceSimCardBydeviceId(TmsDevice deviceId) {
        return deviceSimcardRepository.findBydeviceId(deviceId);
    }

    @Override
    public List<TmsDevice> findAllActive() {
        return deviceRepository.findAllActive(AppConstants.STATUS_ACTIVE, AppConstants.NO);
    }

    @Override
    public List<TmsDevice> findAll() {
        return StreamSupport.stream(deviceRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Integer findAllActiveDevices() {
        return deviceRepository.findAllActiveDevices(AppConstants.STATUS_ACTIVE, AppConstants.NO);
    }

    @Override
    public Integer findAllWhitelistedDevices() {
        return whitelistRepo.findAllWhitelistedDevices(AppConstants.NO);
    }

    @Override
    public Integer findActiveAll() {
        return deviceRepository.findActiveDevices(AppConstants.NO);
    }

    @Override
    public List<TmsDevice> findByModelIdAndEstateId(UfsDeviceModel modelId, TmsEstateItem unitItemId) {
        return deviceRepository.findByModelIdAndEstateIdAndIntrash(modelId, unitItemId, AppConstants.NO);
    }

    @Override
    public TmsWhitelist getWhitelistDevicebySerial(String serialNo) {
        return whitelistRepo.findByserialNoAndIntrash(serialNo, AppConstants.NO);
    }

    @Override
    public List<TmsWhitelist> updateWhitelistBySerialSync(List<String> serials) {
        return serials.stream()
                .map(serial -> whitelistRepo.findByserialNoAndIntrash(serial, AppConstants.NO))
                .filter(Objects::nonNull)
                .map(device -> {
                    device.setAssigned((short) 1);
                    return whitelistRepo.save(device);
                }).collect(Collectors.toList());
    }

    @Override
    public void updateWhitelistBySerialSync(String serialNo) {
        TmsWhitelist whitelist = whitelistRepo.findByserialNoAndIntrash(serialNo, AppConstants.NO);
        if (Objects.nonNull(whitelist)) {
            whitelist.setAssigned((short) 1);
            whitelistRepo.save(whitelist);
        }

    }

    @Override
    public void updateReleaseWhitelistBySerialSync(String serialNo) {
        TmsWhitelist whitelist = whitelistRepo.findByserialNoAndIntrash(serialNo, AppConstants.NO);
        if (Objects.nonNull(whitelist)) {
            whitelist.setAssigned((short) 0);
            whitelistRepo.save(whitelist);
        }
    }

    @Async
    @Override
    public void updateWhitelistBySerialAsync(List<String> serials) {
        updateWhitelistBySerialSync(serials);
    }

    @Override
    public Page<TmsFtpLogs> findByterminalSerial(String terminalSerial, Pageable pg) {
        return fTPLogsRepository.findByterminalSerial(terminalSerial, pg);
    }

    @Override
    public Optional<TmsFtpLogs> findFtpLogsByLogId(BigDecimal id) {
        return fTPLogsRepository.findById(id);
    }

    @Override
    @Async
    public void processWhitelistUploadXlxs(TmsWhitelistBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerServiceLocal loggerService, MultipartFile file, String ipAddress, String userAgent,Long userId, WhitelistWrapper payload) {
        log.error("Processing Uploading =======> xls");
        try {
            List<WhitelistDetails> entities = sharedMethods.convertXls(WhitelistDetails.class, sharedMethods.convert(file));
            long failed = 0;
            long success = 0;
            for (WhitelistDetails entity : entities) {
                log.error("Uploaded data ===>  " + entity.getSerialNo());

                //validate entity
                DirectFieldBindingResult valid = new DirectFieldBindingResult(entity,
                        "WhitelistDetails");
                validator.validate(entity, valid);
                if (valid.hasErrors()) {
                    loggerService.logCreate("Whitelisting a device (Serial number:  "
                                    + entity.getSerialNo() + ") failed. Encountered validation "
                                    + "errors (Errors: " + SharedMethods.getFieldErrorsString(valid)
                                    + ")", SharedMethods.getEntityName(TmsWhitelist.class), null,
                            AppConstants.STATUS_FAILED, ipAddress, userAgent);
                    failed++;
                    continue;
                }
                //check if serial number exists
                TmsWhitelist device = this.whitelistRepo.findByserialNoAndIntrash(entity.getSerialNo(), AppConstants.NO);
                if (device != null) {
                    loggerService.logCreate("Whitelisting a device (Serial number:  "
                                    + entity.getSerialNo() + ") failed. Serial number exists",
                            SharedMethods.getEntityName(TmsWhitelist.class), null,
                            AppConstants.STATUS_FAILED,userId, ipAddress, userAgent);
                    failed++;
                    continue;
                }

                device = new TmsWhitelist(entity.getSerialNo(),
                        batch.getModel().getModelId(), AppConstants.ACTIVITY_CREATE,
                        AppConstants.ACTION_STATUS_UNCONFIRMED, AppConstants.NO);
                device.setBatch(batch);
                device = whitelistRepo.save(device);
                this.terminalHistoryService.saveHistory(new UfsTerminalHistory(payload.getSerialNo(), AppConstants.ACTIVITY_CREATE, "Terminal Whitelisted Successfully", userId, AppConstants.STATUS_UNAPPROVED,loggerService.getFullName()));
                loggerService.logCreate("Whitelisted device (Serial number:  "
                                + entity.getSerialNo() + ") successfully",
                        SharedMethods.getEntityName(TmsWhitelist.class), device.getId(),
                        AppConstants.STATUS_COMPLETED,userId, ipAddress, userAgent);
                success++;
            }
            batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Processing Device Whitelist upload failed", ex);
            loggerService.logCreate("Processing Device Whitelist failed. This may be "
                            + "due to wrong file format / file content msg:" + ex.getMessage(), SharedMethods.getEntityName(TmsWhitelist.class), null,
                    AppConstants.STATUS_FAILED,userId, ipAddress, userAgent);
            batch.setProcessingStatus(AppConstants.STATUS_FAILED);
        } catch (InvalidFormatException ex) {
            java.util.logging.Logger.getLogger(DeviceServiceTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.batchRepo.save(batch);
    }

    @Override
    public Optional<TmsParamDefinition> gerParam(BigDecimal deviceParaId) {
        return definitionRepository.findById(deviceParaId);
    }

    @Override
    @Async
    public TmsDeviceParam generateParameterFile(TmsDevice deviceId, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        TmsDeviceParam deviceParam = deviceParamRepository.findBydeviceId(deviceId);
        System.out.println("--------------parames---------------");
        if (deviceParam != null) {
            System.out.println("--------------parames found---------------");
            log.debug("Model id {}", deviceId.getModelId().getModelId());
            TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(deviceId.getModelId().getModelId());
            if (deviceFileExt != null) {
                generateNMBParams(deviceId, deviceFileExt, sharedMethods, loggerService);
//                generateEquityConfigParams(deviceParam, deviceId, deviceFileExt, sharedMethods, loggerService);
            }
        }
        return deviceParam;
    }

    @Override
    @Async
    public void saveDevices(List<AssignDeviceWrapper> onboardWrappers, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        onboardWrappers.parallelStream().forEach(onboardWrapper -> {
            log.error("Saving device with serial {}", onboardWrapper.getSerialNo());
            TmsDevice tmsDevice = new TmsDevice();

            TmsWhitelist whitelist = whitelistRepo.findByserialNoAndIntrash(onboardWrapper.getSerialNo(), AppConstants.NO);
            TmsEstateItem estates = businessUnitItemRepository.findById(estate).orElse(null);
            if (estates != null)
                tmsDevice.setEstateId(estates);

            TmsDevice dv = getDevicebySerial(onboardWrapper.getSerialNo());
            if (dv != null) {
                if (!dv.getStatus().equals("Inactive")) {
                    tmsDevice.setModelId(whitelist.getModelId());
                    tmsDevice.setPartNumber(whitelist.getProductNo());//        tmsDevice.setEstateId();
                    tmsDevice.setCustomerOwnerName(onboardWrapper.getVendorName());
                    tmsDevice.setDeviceFieldName(onboardWrapper.getVendorName());
                    tmsDevice.setSerialNo(onboardWrapper.getSerialNo());
                    tmsDevice.setStatus(AppConstants.STATUS_ACTIVE);
                    tmsDevice.setAction(AppConstants.ACTIVITY_CREATE);
                    tmsDevice.setActionStatus(AppConstants.STATUS_APPROVED);
                    tmsDevice.setIntrash(AppConstants.NO);


                    tmsDevice = saveDevice(tmsDevice);
                    generateParameterFileSCB(tmsDevice, sharedMethods, loggerService);

                    this.terminalHistoryService.saveHistory(new UfsTerminalHistory(onboardWrapper.getSerialNo(), AppConstants.ACTIVITY_CREATE, "Terminal Assigned Successfully", loggerService.getUser(), AppConstants.STATUS_UNAPPROVED,loggerService.getFullName()));


//            loggerService.logCreate("Creating new Device", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);

                    log.error("Successfully save device with serial {} and id {}", onboardWrapper.getSerialNo(), tmsDevice.getDeviceId());
                }
            } else {
                tmsDevice.setModelId(whitelist.getModelId());
                tmsDevice.setPartNumber(whitelist.getProductNo());//        tmsDevice.setEstateId();
                tmsDevice.setCustomerOwnerName(onboardWrapper.getVendorName());
                tmsDevice.setDeviceFieldName(onboardWrapper.getVendorName());
                tmsDevice.setSerialNo(onboardWrapper.getSerialNo());
                tmsDevice.setStatus(AppConstants.STATUS_ACTIVE);
                tmsDevice.setAction(AppConstants.ACTIVITY_CREATE);
                tmsDevice.setActionStatus(AppConstants.STATUS_APPROVED);
                tmsDevice.setIntrash(AppConstants.NO);


                tmsDevice = saveDevice(tmsDevice);
                generateParameterFileSCB(tmsDevice, sharedMethods, loggerService);

                this.terminalHistoryService.saveHistory(new UfsTerminalHistory(onboardWrapper.getSerialNo(), AppConstants.ACTIVITY_CREATE, "Terminal Assigned Successfully", loggerService.getUser(), AppConstants.STATUS_UNAPPROVED,loggerService.getFullName()));


//            loggerService.logCreate("Creating new Device", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.STATUS_COMPLETED);

                log.error("Successfully save device with serial {} and id {}", onboardWrapper.getSerialNo(), tmsDevice.getDeviceId());
            }


        });
    }

    private void generateParameterFileSCB(TmsDevice deviceId, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        log.debug("Model id {}", deviceId.getModelId().getModelId());
        TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(deviceId.getModelId().getModelId());
        if (deviceFileExt != null) {
            generateNMBParams(deviceId, deviceFileExt, sharedMethods, loggerService);
        }
//
    }

    private void generateEquityConfigParams(TmsDeviceParam deviceParam, TmsDevice deviceId, TmsDeviceFileExt deviceFileExt, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        System.out.println("--------------parames extension found---------------");
        String rootPath = "";
        params = "";

        EquityConfigWrapper config = new Gson().fromJson(deviceParam.getValues(), EquityConfigWrapper.class);

        params += config.getOutletName() + ";";
        params += config.getMerchantName() + ";";
        params += config.getAddress() + ";";
        params += config.getLocation() + ";";
        params += config.getPhone() + ";";
        params += config.getWay4ServerIp() + "|";
        params += config.getWay4ServerPort() + ";";
        params += config.getPosirisServerIp() + "|";
        params += config.getPosirisServerPort() + ";";
        params += config.getPmsServerIp() + "|";
        params += config.getPmsServerPort() + ";";
        config.getMulticurrency().forEach(currency -> {
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getNumericValue() + ";";
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getCode() + ";";
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getDecimalValue() + ";";
        });

        params += config.getAdminPassword() + ";";
        params += config.getMerchantPassword() + ";";
        params += config.getReceiptProfile() + ";";
        params += config.getMid() + ";";
        params += config.getTid() + ";";
        params += config.getOutletNumber() + ";";
        params += config.getTransactionCounter() + ";";

        appendCurrentScheduleWithParams(deviceId, deviceFileExt, sharedMethods, loggerService);
    }

    private void generateNMBParams(TmsDevice deviceId, TmsDeviceFileExt deviceFileExt, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        System.out.println("--------------parames extension found---------------");
        String rootPath = "";
        params = "";

//        ConfigWrapper config = new Gson().fromJson(deviceParam.getValues(), ConfigWrapper.class);


        List<UfsSysConfigWrapper> sysConfigs = configRepo.findByentity("Connection Configuration");


        final ConfigWrapper config = new ConfigWrapper();

        sysConfigs.forEach(conf -> {
            switch (conf.getParameter()) {
                case "tsyncIp":
                    config.setTsyncIp(conf.getValue());
                    break;
                case "tsyncPort":
                    config.setTsyncPort(Long.parseLong(conf.getValue()));
                    break;
                case "tmsServerIp":
                    config.setTmsServerIp(conf.getValue());
                    break;
                case "tmsServerPort":
                    config.setTmsServerPort(Long.parseLong(conf.getValue()));
                    break;
            }
        });


        params += (config.getMerchantName() != null) ? config.getMerchantName() : null + ";";
        params += (config.getOutletNumber() != null) ? config.getOutletNumber() : null + ";";
        params += (config.getAddress() != null) ? config.getAddress() : null + ";";
        params += (config.getLocation() != null) ? config.getLocation() : null + ";";
        params += (config.getPhone() != null) ? config.getPhone() : null + ";";
        params += (config.getPostilionIp() != null) ? config.getPostilionIp() : null + "|";
        params += null + ";";
        params += config.getTmsServerIp() + "|";
        params += config.getTmsServerPort() + ";";
        params += config.getTsyncIp() + "|";
        params += config.getTsyncPort() + ";";
       /* config.getMulticurrency().forEach(currency -> {
            params += currency.getMerchantId() + ";";
            params += currency.getTerminalId() + ";";
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getCode() + ";";
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getNumericValue() + ";";
            params += currencyRepository.findById(BigDecimal.valueOf(Long.valueOf(currency.getCurrencyId()))).get().getDecimalValue() + ";";
        });*/

        params += "null;";
        params += "null;";
        params += "null;";
        params += "null;";
        params += "null;";

        params += "null;";
        params += "null;";
        params += "null;";
        params += "null;";
        params += "null;";

        params += "null;";
        params += "null;";
        params += "null;";
        params += "null;";

        appendCurrentScheduleWithParams(deviceId, deviceFileExt, sharedMethods, loggerService);
    }

    private void appendCurrentScheduleWithParams(TmsDevice deviceId, TmsDeviceFileExt deviceFileExt, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        TmsDeviceTask dtk = deviceTaskRepository.findTopByDeviceIdOrderByTaskIdDesc(deviceId);
        if (dtk != null) {
            TmsScheduler sched = dtk.getScheduleId();
            if (sched.getScheduleType().equals(AppConstants.MANUAL_SCHEDULE) && dtk.getDownloadStatus().equals("PENDING")) {
                sched.setNoFiles(sched.getNoFiles() + 1l);
                schedulerRepository.save(sched);
                sharedMethods.generateParamField(params, "CONFIG" + deviceFileExt.getParamFileExt(), configRepo.findByentityAndParameter(AppConstants.ENTITY_GLOBAL_INTEGRATION,
                        AppConstants.PARAMETER_UPLOAD_DIR).getValue() + sched.getDirPath(), loggerService);
            } else {
                createTaskandFile(params, deviceId, sharedMethods, loggerService, deviceFileExt);
            }
        } else {
            createTaskandFile(params, deviceId, sharedMethods, loggerService, deviceFileExt);
        }
    }

    private void createTaskandFile(String params, TmsDevice deviceId, SharedMethods sharedMethods, LoggerServiceLocal loggerService, TmsDeviceFileExt deviceFileExt) {
        String rootPath = "";
        TmsScheduler scheduler = new TmsScheduler();
        scheduler.setAction(AppConstants.ACTIVITY_CREATE);
        scheduler.setActionStatus(AppConstants.STATUS_APPROVED);
        scheduler.setModelId(deviceId.getModelId());
        scheduler.setDirPath(rootPath);
        scheduler.setScheduleType(AppConstants.MANUAL_SCHEDULE);
        scheduler.setStatus(AppConstants.STATUS_NEW);
        scheduler.setNoFiles(1l);
        scheduler.setDownloadType(AppConstants.DOWNLOAD_APP_AND_FILES);
        scheduler.setIntrash(AppConstants.NO);
        scheduler.setScheduledTime(new Date());
//        scheduler.setProductId(deviceId.getEstateId().getUnitId().getProductId());

        //save the manual schedule
        schedulerRepository.save(scheduler);

        //loggerService.logCreate("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.STATUS_COMPLETED);
        TmsDeviceTask deviceTask = new TmsDeviceTask();
        deviceTask.setDeviceId(deviceId);
        deviceTask.setScheduleId(scheduler);
        deviceTask.setDownloadStatus("PENDING");
        deviceTask.setIntrash(AppConstants.NO);

        //persist the device task
        deviceTaskRepository.save(deviceTask);

        rootPath = "devices/" + deviceId.getDeviceId() + "/" + deviceTask.getTaskId() + "/";
        scheduler.setDirPath("/" + rootPath);
        schedulerRepository.save(scheduler);
        rootPath = configRepo.findByentityAndParameter(AppConstants.ENTITY_GLOBAL_INTEGRATION,
                AppConstants.PARAMETER_UPLOAD_DIR).getValue() + rootPath;

        //loggerService.logCreate("Creating new Device Task", SharedMethods.getEntityName(TmsDeviceTask.class), deviceTask.getTaskId(), AppConstants.STATUS_COMPLETED);
        sharedMethods.generateParamField(params, "CONFIG" + deviceFileExt.getParamFileExt(), rootPath, loggerService);
    }

    @Override
    public DeviceCustomerDetails findByAgentMerchantId(String agentMerchantId) {
        return customerDetailsRepository.findByAgentMerchantIdAndInstrash(agentMerchantId, AppConstants.NO);
    }

    @Override
    public DeviceCustomerDetails saveDeviceCustomerDetails(DeviceCustomerDetails customerDetails) {
        return customerDetailsRepository.save(customerDetails);
    }

    @Override
    public Optional<DeviceCustomerDetails> findDeviceCustomerDetailsById(BigDecimal id) {
        return customerDetailsRepository.findById(id);
    }

    @Override
    public Page<DeviceCustomerDetails> findAllDeviceCustomerDetails(String actionStatus, String needle, Pageable pg) {
        return customerDetailsRepository.findAll(actionStatus, needle, AppConstants.NO, pg);
    }

    @Override
    public TmsDeviceTask findTopByDeviceIdOrderByTaskIdDesc(TmsDevice deviceId) {
        return deviceTaskRepository.findTopByDeviceIdOrderByTaskIdDesc(deviceId);
    }

    @Override
    @Async
    public ParMenuProfile generateMenuParameterFile(TmsDevice deviceId, ParMenuProfile menuId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Optional<ParMenuProfile> parMenuProfile = parMenuProfileRepository.findById(menuId.getId());
        if (parMenuProfile.isPresent()) {
            TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(deviceId.getModelId().getModelId());
            if (deviceFileExt != null) {
                generateEquityMenuParams(parMenuProfile.get(), rootPath, deviceFileExt, sharedMethods, loggerService);
            }
        }
        return parMenuProfile.get();
    }

    private void generateEquityMenuParams(ParMenuProfile parMenuProfile, String rootPath, TmsDeviceFileExt deviceFileExt, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Type typeOfObjectsList = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> config = new Gson().fromJson(parMenuProfile.getMenuValue(), typeOfObjectsList);
        menus = "";
        menus += idExistsInArray(config, 1);
        menus += idExistsInArray(config, 2);
        menus += idExistsInArray(config, 3);
        menus += idExistsInArray(config, 4);
        menus += idExistsInArray(config, 5);
        menus += idExistsInArray(config, 6);
        menus += idExistsInArray(config, 7);
        menus += idExistsInArray(config, 8);
        menus += idExistsInArray(config, 9);
        menus += idExistsInArray(config, 10);
        menus += idExistsInArray(config, 21);
        menus += idExistsInArray(config, 22);
        menus += idExistsInArray(config, 23);
        menus += idExistsInArray(config, 24);
        menus += idExistsInArray(config, 25);
        menus += idExistsInArray(config, 26);
        menus += idExistsInArray(config, 27);
        menus += idExistsInArray(config, 28);
        menus += idExistsInArray(config, 29);
        menus += idExistsInArray(config, 55);
        menus += idExistsInArray(config, 56);
        menus += idExistsInArray(config, 57);
        menus += idExistsInArray(config, 58);
        menus += idExistsInArray(config, 59);
        menus += idExistsInArray(config, 60);
        menus += idExistsInArray(config, 61);
        menus += idExistsInArray(config, 62);
        menus += idExistsInArray(config, 63);
        menus += idExistsInArray(config, 64);
        menus += idExistsInArray(config, 65);
        menus += idExistsInArray(config, 30);
        menus += idExistsInArray(config, 66);
        menus += idExistsInArray(config, 67);
        menus += idExistsInArray(config, 68);
        menus += idExistsInArray(config, 69);
        menus += idExistsInArray(config, 70);
        menus += idExistsInArray(config, 71);
        menus += idExistsInArray(config, 72);
        menus += idExistsInArray(config, 73);
        menus += idExistsInArray(config, 74);
        menus += idExistsInArray(config, 75);
        menus += idExistsInArray(config, 76);
        menus += idExistsInArray(config, 77);
        menus += idExistsInArray(config, 78);
        menus += idExistsInArray(config, 79);
        menus += idExistsInArray(config, 80);
        menus += idExistsInArray(config, 81);
        menus += idExistsInArray(config, 82);
        menus += idExistsInArray(config, 83);
        menus += idExistsInArray(config, 84);
        menus += idExistsInArray(config, 85);
        menus += idExistsInArray(config, 86);
        menus += idExistsInArray(config, 87);
        menus += idExistsInArray(config, 88);
        menus += idExistsInArray(config, 89);
        menus += idExistsInArray(config, 90);
        menus += idExistsInArray(config, 91);
        menus += idExistsInArray(config, 92);

        sharedMethods.generateParamField(menus, "MENU" + deviceFileExt.getParamFileExt(), rootPath, loggerService);
    }

    @Override
    @Async
    public ParBinProfile generateBinParameterFile(TmsDevice deviceId, ParBinProfile binId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Optional<ParBinProfile> parBinProfile = parBinProfileRepository.findById(binId.getId());
        if (parBinProfile.isPresent()) {
            TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(deviceId.getModelId().getModelId());
            if (deviceFileExt != null) {
                generateEquityBinParams(parBinProfile.get(), rootPath, deviceFileExt, sharedMethods, loggerService);
            }
        }
        return parBinProfile.get();
    }

    @Override
    @Async
    public void generateScheduleMenuParameterFile(BigDecimal modelId, ParMenuProfile menuId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Optional<ParMenuProfile> parMenuProfile = parMenuProfileRepository.findById(menuId.getId());
        if (parMenuProfile.isPresent()) {
            TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(modelId);
            if (deviceFileExt != null) {
                generateEquityMenuParams(parMenuProfile.get(), rootPath, deviceFileExt, sharedMethods, loggerService);
            }
        }
    }

    @Override
    @Async
    public void generateScheduleBinParameterFile(BigDecimal modelId, ParBinProfile binId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Optional<ParBinProfile> parBinProfile = parBinProfileRepository.findById(binId.getId());
        if (parBinProfile.isPresent()) {
            TmsDeviceFileExt deviceFileExt = deviceFileExtRepository.findBymodelId(modelId);
            if (deviceFileExt != null) {
                generateEquityBinParams(parBinProfile.get(), rootPath, deviceFileExt, sharedMethods, loggerService);
            }
        }
    }

    @Override
    public TmsDeviceSimcard saveDeviceSimcard(TmsDeviceSimcard deviceSim) {
        return tmsDeviceSimcardRepository.save(deviceSim);
    }

    @Override
    public TmsDeviceTidsMids saveDeviceTids(TmsDeviceTidsMids deviceTid) {
        return tmsDeviceTidRepository.save(deviceTid);
    }

    @Override
    public TmsDevice getDeviceById(BigDecimal deviceId) {
        return deviceRepository.findByDeviceIdAndIntrash(deviceId, AppConstants.NO);
    }

    @Override
    public Page<TmsDevice> getDevicesByCustomerId(BigDecimal customerId, Pageable pg) {
        List<UfsCustomerOutlet> customerOutlets = customerOutletRepository.findOutletsByCustomerIdsAndIntrash(customerId, AppConstants.NO);
        List<BigDecimal> outletIds = customerOutlets.stream().map(outlet -> BigDecimal.valueOf(outlet.getId())).collect(Collectors.toList());
        return deviceRepository.findByOutletIdsIsInAndIntrash(outletIds, AppConstants.NO, pg);
    }

    @Override
    public List<UfsContactPerson> getContactPeopleByOutletId(Long customerOutletId) {
        return contactPersonRepository.findByCustomerOutletIdAndIntrash(customerOutletId, AppConstants.NO);
    }

    @Override
    public boolean checkIfTidMidExists(String tid, String mid) {
        int tidMidCount  = tmsDeviceTidRepository.getTmsDeviceTidsMids(tid,mid);
        return tidMidCount > 0;
    }

    @Override
    public boolean checkIfTidExists(String tid) {
        return tmsDeviceTidRepository.getTmsDeviceTids(tid)>0;
    }

    @Override
    public boolean checkIfTidMidExistsByDeviceIds(String tid, String mid, Long deviceIds) {
        int tidMidCount  = tmsDeviceTidRepository.getTmsDeviceTidsMidsByDeviceIds(tid,mid,deviceIds);
        return tidMidCount > 0;
    }

    @Override
    public boolean checkIfTidExistsByDeviceIds(String tid, Long deviceIds) {
        return tmsDeviceTidRepository.getTmsDeviceTidsByDeviceIds(tid,deviceIds)>0;
    }

    @Override
    public void deleteAllByDeviceId(String serial) {
        List<TmsDevice> ent = deviceRepository.findAllBySerialNoAndIntrash(serial, AppConstants.NO);
        if(ent.size()==0){
            return;
        }
        tmsDeviceTidRepository.deleteAllByDeviceId(ent.stream().map(x->x.getDeviceId().longValue()).collect(Collectors.toList()));
    }

    private void generateEquityBinParams(ParBinProfile parBinProfile, String rootPath, TmsDeviceFileExt deviceFileExt, SharedMethods sharedMethods, LoggerServiceLocal loggerService) {
        Type typeOfObjectsList = new TypeToken<ArrayList<BigDecimal>>() {
        }.getType();
        List<BigDecimal> config = new Gson().fromJson(parBinProfile.getValue(), typeOfObjectsList);

        bin_config = "";
        config.stream().forEach(conf -> {
            Optional<ParBinConfig> bin = parBinConfigRepository.findById(conf);
            if (bin.isPresent()) {
                bin_config += bin.get().getBinIssuer() + "|" + bin.get().getBinLow() + "|" + bin.get().getBinHigh() + ";";
            }
        });
        sharedMethods.generateParamField(bin_config, "BIN_CONFIG" + deviceFileExt.getParamFileExt(), rootPath, loggerService);
    }

    @Override
    public TmsDeviceParam findParamByDeviceId(TmsDevice deviceId) {
        return deviceParamRepository.findBydeviceId(deviceId);
    }

    private String idExistsInArray(List<Integer> list, int id) {
        return ((list.stream().anyMatch(x -> x == id)) ? "1" : "0") + ";";
    }

    @Override
    public UfsDeviceModel saveModel(UfsDeviceModel model) {
        return modelRepo.save(model);
    }

    @Override
    public UfsDeviceModel getModelByName(String modelName) {
        return modelRepo.findByModelAndIntrash(modelName, AppConstants.NO);
    }

}
