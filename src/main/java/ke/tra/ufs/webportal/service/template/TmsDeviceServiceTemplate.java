package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.utils.SharedMethods;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.MenuFileRequest;
import ke.tra.ufs.webportal.repository.*;
import ke.tra.ufs.webportal.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TmsDeviceServiceTemplate implements TmsDeviceService {

    private final TmsDeviceRepository tmsDeviceRepository;
    private final LoggerService loggerService;
    private final PosUserService posUserService;
    private final PasswordEncoder encoder;
    private final SysConfigService configService;
    private final NotifyService notifyService;
    private final WhitelistRepository whitelistRepo;
    private final ContactPersonService contactPersonService;
    private final TmsDeviceTidRepository tmsDeviceTidRepository;
    private final TmsDeviceTidCurrencyRepository tmsDeviceTidCurrencyRepository;
    private final ParGlobalMasterProfileService parGlobalMasterProfileService;
    private final ParFileMenuService parFileMenuService;
    private final ParFileConfigService parFileConfigService;
    private final CustomerConfigFileService customerConfigFileService;
    private final ParDeviceSelectedOptionsService parDeviceSelectedOptionsService;
    private final SchedulerService schedulerService;
    private final UfsCustomerOutletService outletService;
    private final UfsContactPersonRepository contactPersonRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;
    private final UfsPosUserRepository ufsPosUserRepository;
    private final DeviceTaskRepository deviceTaskRepository;

    public TmsDeviceServiceTemplate(TmsDeviceRepository tmsDeviceRepository, LoggerService loggerService, PosUserService posUserService, PasswordEncoder encoder, SysConfigService configService, NotifyService notifyService, WhitelistRepository whitelistRepo, ContactPersonService contactPersonService, TmsDeviceTidRepository tmsDeviceTidRepository, TmsDeviceTidCurrencyRepository tmsDeviceTidCurrencyRepository, ParGlobalMasterProfileService parGlobalMasterProfileService, ParFileMenuService parFileMenuService, ParFileConfigService parFileConfigService, CustomerConfigFileService customerConfigFileService, ParDeviceSelectedOptionsService parDeviceSelectedOptionsService, SchedulerService schedulerService, UfsCustomerOutletService outletService, UfsContactPersonRepository contactPersonRepository, UfsCustomerOutletRepository customerOutletRepository, UfsPosUserRepository ufsPosUserRepository, DeviceTaskRepository deviceTaskRepository) {
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.loggerService = loggerService;
        this.posUserService = posUserService;
        this.encoder = encoder;
        this.configService = configService;
        this.notifyService = notifyService;
        this.whitelistRepo = whitelistRepo;
        this.contactPersonService = contactPersonService;
        this.tmsDeviceTidRepository = tmsDeviceTidRepository;
        this.tmsDeviceTidCurrencyRepository = tmsDeviceTidCurrencyRepository;
        this.parGlobalMasterProfileService = parGlobalMasterProfileService;
        this.parFileMenuService = parFileMenuService;
        this.parFileConfigService = parFileConfigService;
        this.customerConfigFileService = customerConfigFileService;
        this.parDeviceSelectedOptionsService = parDeviceSelectedOptionsService;
        this.schedulerService = schedulerService;
        this.outletService = outletService;
        this.contactPersonRepository = contactPersonRepository;
        this.customerOutletRepository = customerOutletRepository;
        this.ufsPosUserRepository = ufsPosUserRepository;
        this.deviceTaskRepository = deviceTaskRepository;
    }

    @Override
    public TmsDevice findByDeviceId(BigDecimal deviceId) {
        return tmsDeviceRepository.findByDeviceId(deviceId);
    }

    @Override
    public List<TmsDevice> findByOutletIds(List<BigDecimal> outletIds) {
        return tmsDeviceRepository.findByOutletIdsInAndIntrash(outletIds, AppConstants.NO);
    }

    @Override
    public List<TmsDeviceTids> findByDeviceIds(Long deviceIds) {
        return tmsDeviceTidRepository.findAllByDeviceIds(deviceIds);
    }

    @Override
    public List<TmsDeviceTidCurrency> findByDeviceIds(TmsDevice deviceIds) {
        return tmsDeviceTidCurrencyRepository.findAllByDeviceId(deviceIds);
    }

    @Override
    public TmsDevice findByDeviceIdAndIntrash(BigDecimal id) {
        return tmsDeviceRepository.findByDeviceIdAndIntrash(id, AppConstants.NO);
    }

    @Override
    public Integer findAllActiveDevices() {
        return tmsDeviceRepository.findAllActiveDevices(AppConstants.STATUS_ACTIVE, AppConstants.NO);
    }

    @Override
    @Async
    public void activateDevicesByOutlets(List<UfsCustomerOutlet> customerOutlets, String notes) {
        System.out.println("Approving devices by outlet");
        List<BigDecimal> outletIds = customerOutlets.stream().map(x -> BigDecimal.valueOf(x.getId())).collect(Collectors.toList());
        if (outletIds.size() == 0) {
            return;
        }
        List<TmsDevice> devices = findByOutletIds(outletIds);
        for (TmsDevice device : devices) {
            if (device.getAction().equals(AppConstants.ACTIVITY_CREATE) && device.getActionStatus().equals(AppConstants.STATUS_UNAPPROVED)) {
                processApproveNew(device, notes);
            }
        }
    }

    @Override
    @Async
    public void activateDevicesByOutletsIds(List<Long> customerOutlets, String notes) {
        List<TmsDevice> devices = findByOutletIds(customerOutlets.stream().map(BigDecimal::new).collect(Collectors.toList()));
        for (TmsDevice device : devices) {
            if (device.getAction().equals(AppConstants.ACTIVITY_CREATE) && device.getActionStatus().equals(AppConstants.STATUS_UNAPPROVED)) {
                processApproveNew(device, notes);
            }
        }
    }

    @Override
    @Async
    public void approveContactPersons(Long customerId, String notes) {
        System.out.println("Approving contact persons");
        List<UfsContactPerson> contactPerson = this.contactPersonService.getAllContactPersonByCustomerId(BigDecimal.valueOf(customerId));
        for (UfsContactPerson cntper : contactPerson) {
            if (cntper.getAction().equalsIgnoreCase(ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE) && cntper.getActionStatus().equals(ke.tra.ufs.webportal.utils.AppConstants.STATUS_UNAPPROVED)) {
                cntper.setActionStatus(AppConstants.STATUS_APPROVED);
                this.contactPersonService.saveContactPerson(cntper);
                List<UfsPosUser> posUsers = this.posUserService.findByContactPersonId(cntper.getId());
                if (!posUsers.isEmpty()) {
                    posUsers.forEach(posUser -> {

                        UfsPosUser savedUser = posUser;
                        //generate random pin
                        String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(ke.tra.ufs.webportal.utils.AppConstants.ENTITY_POS_CONFIGURATION, ke.tra.ufs.webportal.utils.AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);

                        posUser.setActionStatus(ke.tra.ufs.webportal.utils.AppConstants.STATUS_APPROVED);
                        posUser.setPin(encoder.encode(randomPin));
                        savedUser = posUserService.savePosUser(posUser);

                        String message = "Your username is " + savedUser.getUsername() + ". Use password :" + randomPin + " to login to POS terminal";
                        if (cntper.getEmail() != null) {
                            notifyService.sendEmail(cntper.getEmail(), "Login Credentials", message);
                            loggerService.log("Sent login credentials for " + cntper.getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                    ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, ke.tra.ufs.webportal.utils.AppConstants.STATUS_COMPLETED, "Sent login credentials");

                        } else {
                            if (cntper.getPhoneNumber() != null) {
                                // send sms
                                posUserService.sendSmsMessage(cntper.getPhoneNumber(), message);
                                loggerService.log("Sent login credentials for " + cntper.getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                        ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, ke.tra.ufs.webportal.utils.AppConstants.STATUS_COMPLETED, "Sent login credentials");

                            } else {

                                loggerService.log("Failed to send login credentials for " + cntper.getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                        ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, ke.tra.ufs.webportal.utils.AppConstants.STATUS_FAILED_STRING, "No valid email or phone number.");

                            }
                        }
                    });
                }
            }
        }

    }

    @Override
    @Async
    public void updateDeviceOwnerByOutletId(List<Long> customerOutlets) {
        List<TmsDevice> devices = findByOutletIds(customerOutlets.stream().map(BigDecimal::new).collect(Collectors.toList()));
        for (TmsDevice device : devices) {
            if (device.getOutletIds() != null) {
                UfsCustomerOutlet outlet = customerOutletRepository.findById(device.getOutletIds().longValue()).get();
                device.setDeviceFieldName(outlet.getOutletName());
                tmsDeviceRepository.save(device);
            }
        }
    }

    @Override
    @Async
    public void updateContactPersonsDetails(UfsCustomer entity) {
        List<UfsContactPerson> contactPerson = this.contactPersonService.getAllContactPersonByCustomerId(new BigDecimal(entity.getId()));
        for (UfsContactPerson cntper : contactPerson) {
            cntper.setPhoneNumber(entity.getBusinessPrimaryContactNo());
            contactPersonService.saveContactPerson(cntper);
        }
    }

    @Override
    public void addDevicesTaskByOutletsIds(List<Long> outletsIds) {
        String rootPath = configService.fetchSysConfigById(new BigDecimal(24)).getValue();
        List<BigDecimal> outletsFiltered = outletsIds.stream().filter(outletId -> {
            UfsCustomerOutlet outlet = outletService.findById(outletId);
            if (outlet.getAction().equals(AppConstants.ACTIVITY_UPDATE) && outlet.getActionStatus().equals(AppConstants.STATUS_APPROVED)) {
                return true;
            }
            return false;
        }).map(BigDecimal::new).collect(Collectors.toList());

        List<TmsDevice> devices = findByOutletIds(outletsFiltered);
        devices.parallelStream().forEach(device -> {
            TmsDeviceTask dtk = findTopByDeviceIdOrderByTaskIdDesc(device);
            if (dtk == null) {
                processAddTaskTodevice(device, rootPath, "/devices/" + device.getDeviceId() + "/");
            } else {
                processAddTaskTodevice(device, rootPath, "/devices/" + device.getDeviceId() + "/", dtk);
            }
        });
    }



    @Override
    @Async
    public void deActivateDevicesByOutlets(List<UfsCustomerOutlet> customerOutlets, String notes) {
        List<BigDecimal> outletIds = customerOutlets.stream().map(x -> new BigDecimal(x.getId())).collect(Collectors.toList());
        List<TmsDevice> devices = findByOutletIds(outletIds);

        List<TmsDevice> updateDevices = devices.stream().peek(x -> {
            x.setActionStatus(AppConstants.STATUS_DECLINED);
            x.setStatus(AppConstants.STATUS_INACTIVE);
            x.setIntrash(AppConstants.YES);
        }).collect(Collectors.toList());

        tmsDeviceRepository.saveAll(updateDevices);
        tmsDeviceTidRepository.deleteAllByDeviceId(updateDevices.stream().map(x -> x.getDeviceId().longValue()).collect(Collectors.toList()));

    }

    @Override
    @Async
    public void delineContactPersons(Long id, String notes) {
        List<UfsContactPerson> contactPerson = this.contactPersonService.getAllContactPersonByCustomerId(BigDecimal.valueOf(id));
        List<UfsContactPerson> contactPersonUpdate = contactPerson.stream().peek(x -> {
            x.setActionStatus(AppConstants.STATUS_DECLINED);
            x.setIntrash(AppConstants.YES);
        }).collect(Collectors.toList());
        contactPersonRepository.saveAll(contactPersonUpdate);
    }

    @Override
    @Async
    public void updateDeviceOwnersByContactPersons(List<Long> contactPersonId) {
        List<UfsContactPerson> cntPerson = contactPersonRepository.findByIdInAndIntrash(contactPersonId, AppConstants.NO);
        for (UfsContactPerson person : cntPerson) {
            List<UfsPosUser> posUsers = ufsPosUserRepository.findByContactPersonIdAndIntrash(person.getId(), AppConstants.NO);
            List<BigDecimal> devices = posUsers.stream().map(UfsPosUser::getTmsDeviceId).collect(Collectors.toList());
            List<TmsDevice> deviceModel = tmsDeviceRepository.findByDeviceIdInAndIntrash(devices, AppConstants.NO);
            deviceModel.forEach(dv -> {
                processUpdateDeviceDetails(dv, person.getName());
            });

        }
    }

    @Override
    public Integer findByMidCount(String mid) {
        return tmsDeviceTidCurrencyRepository.findByMid(mid);
    }

    @Override
    public Integer findByListMidCount(Set<String> mid) {
        return tmsDeviceTidCurrencyRepository.findByMidIn(mid);
    }


    private void processUpdateDeviceDetails(TmsDevice device, String customername) {
        device.setCustomerOwnerName(customername);
        device.setDeviceFieldName(customername);
        tmsDeviceRepository.save(device);
    }

    public void processAddTaskTodevice(TmsDevice device, String rootPath, String path) {
        long filecount = 1;

        TmsScheduler scheduler = new TmsScheduler();
        scheduler.setAction(AppConstants.ACTIVITY_CREATE);
        scheduler.setActionStatus(AppConstants.STATUS_APPROVED);
        scheduler.setModelId(device.getModelId());
        scheduler.setDirPath(path);
        scheduler.setScheduleType("Manual");
        scheduler.setStatus(AppConstants.STATUS_NEW);
        scheduler.setNoFiles(filecount);
        scheduler.setDownloadType("Files");
        scheduler.setIntrash(AppConstants.NO);
        scheduler.setScheduledTime(new Date());
        scheduler.setProductId(device.getEstateId().getUnitId().getProductId());

        //save the manual schedule
        schedulerService.saveSchedule(scheduler);

        // loggerService.log("Creating new Schedule", SharedMethods.getEntityName(TmsScheduler.class), scheduler.getScheduleId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "");

        TmsDeviceTask deviceTask = new TmsDeviceTask();
        deviceTask.setDeviceId(device);
        deviceTask.setScheduleId(scheduler);
        deviceTask.setDownloadStatus("PENDING");
        deviceTask.setIntrash(AppConstants.NO);

        //persist the device task
        schedulerService.saveDeviceTask(deviceTask);

        rootPath = rootPath + path + deviceTask.getTaskId() + "/";
        scheduler.setDirPath(path + deviceTask.getTaskId() + "/");
        schedulerService.saveSchedule(scheduler);
        transferAndCopyFiles(device, rootPath);

        //loggerService.log("Creating new Device Task", SharedMethods.getEntityName(TmsDeviceTask.class), deviceTask.getTaskId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "");
    }

    private void processAddTaskTodevice(TmsDevice device, String rootPath, String path, TmsDeviceTask dtk) {
    }

    private void transferAndCopyFiles(TmsDevice tmsDevice, String rootPath) {
        if (tmsDevice.getMasterProfileId() != null) {
            Optional<ParGlobalMasterProfile> optionalMaster = parGlobalMasterProfileService.findById(tmsDevice.getMasterProfileId());
            if (optionalMaster.isPresent()) {
                ParGlobalMasterProfile parGlobalMasterProfile = optionalMaster.get();

                // generate menu profile
                parFileMenuService.generateMenuFileAsync(new MenuFileRequest(tmsDevice.getModelId().getModelId(), parGlobalMasterProfile.getMenuProfileId()), rootPath);

                // generate all global configs related to master profile
                for (ParGlobalMasterChildProfile config : parGlobalMasterProfile.getChildProfiles()) {
                    parFileConfigService.generateGlobalConfigFileAsync(config.getConfigProfile(), tmsDevice.getModelId().getModelId(), rootPath);
                }
            }
            customerConfigFileService.generateCustomerFile(tmsDevice.getDeviceId(), rootPath);
            //loggerService.log("Saving new App Files", SharedMethods.getEntityName(TmsDevice.class), tmsDevice.getDeviceId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "");


        }
    }

    public TmsDeviceTask findTopByDeviceIdOrderByTaskIdDesc(TmsDevice deviceId) {
        return deviceTaskRepository.findTopByDeviceIdOrderByTaskIdDesc(deviceId);
    }

    private void processApproveNew(TmsDevice entity, String notes) {
        entity.setActionStatus(AppConstants.STATUS_APPROVED);
        tmsDeviceRepository.save(entity);
        //set Whitelisted device to assigned
        updateWhitelistBySerialSync(entity.getSerialNo());

        loggerService.log("Done approving new Device serial (" + entity.getSerialNo() + ")",
                SharedMethods.getEntityName(TmsDevice.class), entity.getDeviceId(), ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_APPROVE, ke.tra.ufs.webportal.utils.AppConstants.STATUS_COMPLETED, notes);

        // send credentials for customer owner
        UfsPosUser posUser = posUserService.findByDeviceIdAndFirstTime(entity.getDeviceId(), (short) 1);
        if (Objects.nonNull(posUser)) {
            UfsCustomerOwners customerOwner = posUser.getCustomerOwners();
            if (customerOwner != null) {
                String cutomerRandomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(ke.tra.ufs.webportal.utils.AppConstants.ENTITY_POS_CONFIGURATION,
                        ke.tra.ufs.webportal.utils.AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
                String message = "Your username is " + posUser.getUsername() + ". Use password " + cutomerRandomPin + " to login to POS terminal";
                posUser.setPin(encoder.encode(cutomerRandomPin));
                posUser.setActionStatus(AppConstants.STATUS_APPROVED);
                posUserService.savePosUser(posUser);

                if (customerOwner.getDirectorEmailAddress() != null) {
                    notifyService.sendEmail(customerOwner.getDirectorEmailAddress(), "Login Credentials", message);
                    loggerService.log("Sent login credentials for " + customerOwner.getDirectorName(), UfsPosUser.class.getSimpleName(),
                            posUser.getPosUserId(), ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, notes);
                } else {
                    if (customerOwner.getDirectorPrimaryContactNumber() != null) {
                        // send sms
                        posUserService.sendSmsMessage(customerOwner.getDirectorPrimaryContactNumber(), message);
                        loggerService.log("Sent login credentials for " + customerOwner.getDirectorName(), UfsPosUser.class.getSimpleName(),
                                posUser.getPosUserId(), ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, notes);
                    } else {
                        loggerService.log("Failed to send login credentials for " + customerOwner.getDirectorName() + ". No valid email or phone number.", UfsPosUser.class.getSimpleName(),
                                posUser.getPosUserId(), ke.tra.ufs.webportal.utils.AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_FAILED, notes);
                    }
                }
            }
        }
    }

    private void updateWhitelistBySerialSync(String serialNo) {
        TmsWhitelist whitelist = whitelistRepo.findByserialNoAndIntrash(serialNo, AppConstants.NO);
        if (Objects.nonNull(whitelist)) {
            whitelist.setAssigned((short) 1);
            whitelistRepo.save(whitelist);
        }

    }

}
