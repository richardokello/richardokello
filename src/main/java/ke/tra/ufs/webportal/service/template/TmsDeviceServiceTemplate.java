package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.utils.SharedMethods;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.repository.WhitelistRepository;
import ke.tra.ufs.webportal.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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

    public TmsDeviceServiceTemplate(TmsDeviceRepository tmsDeviceRepository, LoggerService loggerService, PosUserService posUserService, PasswordEncoder encoder, SysConfigService configService, NotifyService notifyService, WhitelistRepository whitelistRepo, ContactPersonService contactPersonService) {
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.loggerService = loggerService;
        this.posUserService = posUserService;
        this.encoder = encoder;
        this.configService = configService;
        this.notifyService = notifyService;
        this.whitelistRepo = whitelistRepo;
        this.contactPersonService = contactPersonService;
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
    public void updateDeviceOwnerByOutletId(List<Long> customerOutlets, String customerOwnerName) {
        List<TmsDevice> devices = findByOutletIds(customerOutlets.stream().map(BigDecimal::new).collect(Collectors.toList()));
        for (TmsDevice device : devices) {
            device.setCustomerOwnerName(customerOwnerName);
            device.setDeviceFieldName(customerOwnerName);
            tmsDeviceRepository.save(device);
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
