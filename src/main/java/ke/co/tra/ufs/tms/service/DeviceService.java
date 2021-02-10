package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.AssignDeviceWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.WhitelistWrapper;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Cornelius M
 */
public interface DeviceService {

    /**
     * find by deviceMake
     *
     * @param deviceMake
     * @return
     */
    List<UfsDeviceMake> findAllByDeviceMake(String deviceMake);

    /**
     * find by type and intrash
     *
     * @param type
     * @return
     */
    List<UfsDeviceType> findByTypeAndIntrash(String type);

    /**
     * get currency
     *
     * @param id
     * @return
     */
    public UfsCurrency getCurrencyById(BigDecimal id);

    /**
     * Save device make
     *
     * @param make
     * @return
     */
    public UfsDeviceMake saveMake(UfsDeviceMake make);

    /**
     * @param model
     * @return
     */
    public UfsDeviceModel saveModel(UfsDeviceModel model);

    /**
     * @param modelName
     * @return
     */
    public UfsDeviceModel getModelByName(String modelName);

    /**
     * @param device
     * @return
     */
    public TmsDevice saveDevice(TmsDevice device);

    /**
     * @param serialNo
     * @return
     */
    public TmsDevice getDevicebySerial(String serialNo);

    /**
     * @param serialNo
     * @return
     */
    public TmsWhitelist getWhitelistDevicebySerial(String serialNo);

    /**
     * update devices synchronously
     *
     * @param serials
     * @return
     */
    List<TmsWhitelist> updateWhitelistBySerialSync(List<String> serials);

    /**
     * @param serialNo
     */
    public void updateWhitelistBySerialSync(String serialNo);

    /**
     * @param serialNo
     */
    public void updateReleaseWhitelistBySerialSync(String serialNo);

    /**
     * update devices asynchronously
     *
     * @param serials
     * @return
     */
    void updateWhitelistBySerialAsync(List<String> serials);

    /**
     * @param deviceId
     * @return
     */
    public Optional<TmsDevice> getDevice(BigDecimal deviceId);

    /**
     * @param action
     * @param actionStatus
     * @param from
     * @param to
     * @param needle
     * @param status
     * @param pg
     * @return
     */
    public Page<TmsDevice> getDevices(String action, String actionStatus, Date from, Date to, String needle, String status, Pageable pg);

    /**
     * Fetch device make by make id
     *
     * @param makeId
     * @return
     */
    public UfsDeviceMake getMake(BigDecimal makeId);

    /**
     * @return
     */
    List<TmsDevice> findAllActive();

    /**
     * @return
     */
    Integer findAllActiveDevices();

    /**
     * @return
     */
    Integer findAllWhitelistedDevices();

    /**
     * @return
     */
    Integer findActiveAll();

    /**
     * @return
     */
    List<TmsDevice> findAll();

    /**
     * Filter device make by date, action status and search needle
     *
     * @param actionStatus
     * @param pg
     * @param needle
     * @return
     */
    public Page<UfsDeviceMake> getMake(String actionStatus, String needle, Pageable pg);

    /**
     * Filter device model by action status, make id, device type id and search
     * needle
     *
     * @param actionStatus
     * @param makeId
     * @param deviceTypeId
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsDeviceModel> getModel(String actionStatus, String makeId, String deviceTypeId, String needle, Pageable pg);

    /**
     * Filter device type by action status and search needle
     *
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsDeviceType> getType(String actionStatus, String needle, Pageable pg);

    /**
     * Fetch model using model id
     *
     * @param modelId
     * @return
     */
    public UfsDeviceModel getModel(BigDecimal modelId);

    /**
     * Fetch whitelist by serial number
     *
     * @param serialNo
     * @return
     */
    public TmsWhitelist getWhitelist(String serialNo);

    /**
     * Fetch whitelist by id
     *
     * @param id
     * @return
     */
    public TmsWhitelist getWhitelistById(BigDecimal id);

    /**
     * Save whitelist
     *
     * @param entity
     * @return
     */
    public TmsWhitelist saveWhitelist(TmsWhitelist entity);

    /**
     * Save whitelist batch
     *
     * @param batch
     * @return
     */
    public TmsWhitelistBatch saveBatch(TmsWhitelistBatch batch);

    /**
     * Used to process upload file in the background
     *
     * @param batch
     * @param configService
     * @param sharedMethods
     * @param loggerService
     * @param file
     * @param remoteAddress
     * @param userAgent
     */
    public void processWhitelistUpload(TmsWhitelistBatch batch, SysConfigService configService, SharedMethods sharedMethods,
                                       LoggerServiceLocal loggerService, byte[] file, String remoteAddress,
                                       String userAgent, Long userId, WhitelistWrapper payload);

    /**
     * Filter whitelist
     *
     * @param actionStatus
     * @param modelId
     * @param from
     * @param to
     * @param needle
     * @param pg
     * @return
     */
    public Page<TmsWhitelist> getWhitelist(String actionStatus, String modelId, Date from, Date to, String needle, String serialNo, String assignStr, Pageable pg);


    /**
     * Filter whitelist
     *
     * @param actionStatus
     * @param modelId
     * @param from
     * @param to
     * @param needle
     * @param pg
     * @return
     */
    public Page<TmsWhitelist> getDeletedWhitelistDevices(String actionStatus, String modelId, Date from, Date to, String needle, String serialNo, String assignStr, Pageable pg);


    /**
     * @param unitItemId
     * @return
     */
    public List<TmsDevice> findByestateId(TmsEstateItem unitItemId);

    /**
     * @param deviceCurrency
     * @return
     */
    public TmsDeviceCurrency saveTmsDeviceCurrency(TmsDeviceCurrency deviceCurrency);

    /**
     * @param deviceSimcard
     * @return
     */
    public TmsDeviceSimcard saveTmsDeviceSimcard(TmsDeviceSimcard deviceSimcard);

    /**
     * @param deviceId
     * @return
     */
    TmsDeviceCurrency findDeviceCurrencyBydeviceId(TmsDevice deviceId);

    /**
     * @param deviceId
     * @return
     */
    TmsDeviceSimcard findDeviceSimCardBydeviceId(TmsDevice deviceId);

    /**
     * @param modelId
     * @param unitItemId
     * @return
     */
    List<TmsDevice> findByModelIdAndEstateId(UfsDeviceModel modelId, TmsEstateItem unitItemId);

    /**
     * @param terminalSerial
     * @param pg
     * @return
     */
    public Page<TmsFtpLogs> findByterminalSerial(String terminalSerial, Pageable pg);

    /**
     * @param id
     * @return
     */
    public Optional<TmsFtpLogs> findFtpLogsByLogId(BigDecimal id);

    /**
     * @param batch
     * @param configService
     * @param sharedMethods
     * @param loggerService
     * @param file
     * @param remoteAddr
     * @param abbreviate
     */
    public void processWhitelistUploadXlxs(TmsWhitelistBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerServiceLocal loggerService, MultipartFile file, String remoteAddr, String abbreviate, Long userId, WhitelistWrapper payload);

    /**
     * @param deviceParaId
     * @return
     */
    public Optional<TmsParamDefinition> gerParam(BigDecimal deviceParaId);

    /**
     * @param deviceId
     * @param sharedMethods
     * @param loggerService
     * @return
     */
    public TmsDeviceParam generateParameterFile(TmsDevice deviceId, SharedMethods sharedMethods, LoggerServiceLocal loggerService);

    public void saveDevices(List<AssignDeviceWrapper> onboardWrapper, SharedMethods sharedMethods, LoggerServiceLocal loggerService);

    /**
     * @param deviceId
     * @return
     */
    public TmsDeviceParam findParamByDeviceId(TmsDevice deviceId);

    /**
     * @param agentMerchantId
     * @return
     */
    public DeviceCustomerDetails findByAgentMerchantId(String agentMerchantId);

    /**
     * @param customerDetails
     * @return
     */
    public DeviceCustomerDetails saveDeviceCustomerDetails(DeviceCustomerDetails customerDetails);

    /**
     * @param id
     * @return
     */
    public Optional<DeviceCustomerDetails> findDeviceCustomerDetailsById(BigDecimal id);

    /**
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<DeviceCustomerDetails> findAllDeviceCustomerDetails(String actionStatus, String needle, Pageable pg);

    /**
     * @param deviceId
     * @return
     */
    TmsDeviceTask findTopByDeviceIdOrderByTaskIdDesc(TmsDevice deviceId);


    /**
     * @param menuId
     * @param rootPath
     * @param sharedMethods
     * @param loggerService
     * @return
     */
    public ParMenuProfile generateMenuParameterFile(TmsDevice deviceId, ParMenuProfile menuId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService);


    /**
     * @param binId
     * @param rootPath
     * @param sharedMethods
     * @param loggerService
     * @return
     */
    public ParBinProfile generateBinParameterFile(TmsDevice deviceId, ParBinProfile binId, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService);

    /**
     * @param modelId
     * @param parMenuProfile
     * @param rootPath
     * @param sharedMethods
     * @param loggerService
     */
    public void generateScheduleMenuParameterFile(BigDecimal modelId, ParMenuProfile parMenuProfile, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService);

    /**
     * @param modelId
     * @param parBinProfile
     * @param rootPath
     * @param sharedMethods
     * @param loggerService
     */
    public void generateScheduleBinParameterFile(BigDecimal modelId, ParBinProfile parBinProfile, String rootPath, SharedMethods sharedMethods, LoggerServiceLocal loggerService);

    /**
     * @param deviceSim
     * @return
     */
    public TmsDeviceSimcard saveDeviceSimcard(TmsDeviceSimcard deviceSim);


    /**
     * @param deviceTid
     * @return
     */
    public TmsDeviceTidsMids saveDeviceTids(TmsDeviceTidsMids deviceTid);

    /**
     * @param deviceId
     * @return
     */
    public TmsDevice getDeviceById(BigDecimal deviceId);

    /**
     * @param customerId
     * @return
     */
    Page<TmsDevice> getDevicesByCustomerId(BigDecimal customerId, Pageable pg);

    /**
     * @param customerOutletId
     * @return
     */
    List<UfsContactPerson> getContactPeopleByOutletId(Long customerOutletId);

    /**
     * find by checkIfTidMidExists
     *
     * @param tid
     * @param mid
     * @return
     */
    boolean checkIfTidMidExists(String tid, String mid);

    boolean checkIfTidExists(String tid);

    boolean checkIfTidMidExistsByDeviceIds(String tid, String mid, Long deviceIds);

    boolean checkIfTidExistsByDeviceIds(String tid, Long deviceIds);

    void deleteAllByDeviceId(String tmsDevice);

}
