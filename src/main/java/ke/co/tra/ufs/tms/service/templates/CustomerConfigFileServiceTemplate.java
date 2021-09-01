package ke.co.tra.ufs.tms.service.templates;

import com.google.gson.Gson;
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.entities.wrappers.InstitutionWrapper;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.CustomerConfigFileService;
import ke.co.tra.ufs.tms.service.CustomerOutletService;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
@CommonsLog
public class CustomerConfigFileServiceTemplate extends ParFileService implements CustomerConfigFileService {
    private final TmsDeviceRepository tmsDeviceRepository;
    private final ParCustomerConfigIndicesRepository configIndicesRepository;
    private final ParDeviceOptionsIndicesRepository parDeviceOptionsIndicesRepository;
    private final ParDeviceSelectedOptionsRepository parDeviceSelectedOptionsRepository;
    private final CustomerOutletService outletService;
    private final TmsDeviceTidMidRepository tmsDeviceTidMidRepository;
    private final CurrencyRepository currencyRepository;


    public CustomerConfigFileServiceTemplate(TmsDeviceRepository tmsDeviceRepository, FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService,
                                             SharedMethods sharedMethods, ParCustomerConfigIndicesRepository configIndicesRepository,
                                             ParDeviceOptionsIndicesRepository parDeviceOptionsIndicesRepository, ParDeviceSelectedOptionsRepository parDeviceSelectedOptionsRepository, CustomerOutletService outletService, TmsDeviceTidMidRepository tmsDeviceTidMidRepository, CurrencyRepository currencyRepository) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.configIndicesRepository = configIndicesRepository;
        this.parDeviceOptionsIndicesRepository = parDeviceOptionsIndicesRepository;
        this.parDeviceSelectedOptionsRepository = parDeviceSelectedOptionsRepository;
        this.outletService = outletService;
        this.tmsDeviceTidMidRepository = tmsDeviceTidMidRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void generateCustomerFile(BigDecimal deviceId, String filePath) {
        TmsDevice tmsDevice = tmsDeviceRepository.findByDeviceIdAndIntrash(deviceId, "NO");
        if (tmsDevice == null) {
            return;
        }

        List<ParCustomerConfigKeysIndices> parentIndices = configIndicesRepository.findAll(Sort.by(Sort.Direction.ASC, "configIndex"));
        List<String> result = new ArrayList<>();
        log.error(parentIndices.size());

        for (int i = 0; i < parentIndices.size(); i++) {
            if (parentIndices.get(i).getConfig().getIsAllowed() == 1) {
                result.add(getParamValueNew(parentIndices.get(i), tmsDevice) + ";");
            }
        }

        log.error("results >>>>>>>>>>>>>>" + Arrays.asList(result));
        createFile(result, tmsDevice.getModelId().getModelId(), "CUSTOMER", filePath);
    }

    private String getParamValueNew(ParCustomerConfigKeysIndices parentIndex, TmsDevice device) {
        try {
            UfsCustomerOutlet outlet = outletService.findByOutletId(device.getOutletIds().longValue());
            UfsCustomer customer = outlet.getCustomerId();
            log.error("Entity Names=>" + parentIndex.getConfig().getEntityName());
            switch (parentIndex.getConfig().getEntityName()) {
                case CUSTOMER:
                    if(parentIndex.getConfig().getKeyName().equals(AppConstants.CRDB_BILLER_PREFIX)){
                        Gson gson = new Gson();
                        if(customer.getOrgData() != null){
                            InstitutionWrapper institutionWrapper = gson.fromJson(customer.getOrgData(), InstitutionWrapper.class);
                            return institutionWrapper.getOrgPrefix().replaceAll("\"", "");
                        }else{
                            return null;
                        }
                    }
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsCustomer.class, customer);
                case TID_MID:
                    // has children -- get children
                    StringBuilder stringBuilder = new StringBuilder();
                    List<TmsDeviceTidsMids> tidsMidsList = tmsDeviceTidMidRepository.findAllByDeviceIds(device.getDeviceId().longValue());
                    for (TmsDeviceTidsMids tidsMids : tidsMidsList) {
                        // save the values in arraylist by their position
                        List<String> tidMidValues = new ArrayList<>();
//                        List<ParCustomerConfigChildKeys> childKeys = new ArrayList<>(parentIndex.getConfig().getChildKeys()); when using sets
                        parentIndex.getConfig().getChildKeys().sort(Comparator.comparing(x -> x.getChildIndex().getConfigIndex()));
                        for (ParCustomerConfigChildKeys childKey : parentIndex.getConfig().getChildKeys()) {
                            if (childKey.getIsAllowed() == 1) { // process what is allowed
                                String result = getTIDChildParamValue(childKey, tidsMids);
                                tidMidValues.add(result);
                            }
                        }
                        for (String value : tidMidValues) {
                            stringBuilder.append(value).append(';');
                        }
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    return stringBuilder.toString();
                case DEVICE_OPTIONS:
                    // get device options
                    StringBuilder builder = new StringBuilder();
                    Set<BigDecimal> deviceOptionsSet = new HashSet<>();
                    List<ParDeviceSelectedOptions> parDeviceSelectedOptions = parDeviceSelectedOptionsRepository.findAllByDeviceId(device.getDeviceId());
                    for (ParDeviceSelectedOptions deviceOption : parDeviceSelectedOptions) {
                        deviceOptionsSet.add(deviceOption.getDeviceOptionId());
                    }

                    List<ParDeviceOptionsIndices> deviceOptionsIndices = parDeviceOptionsIndicesRepository.findAll(Sort.by(Sort.Direction.ASC, "optionId"));
                    for (ParDeviceOptionsIndices index : deviceOptionsIndices) {
                        if (index.getOption().getIsAllowed() == 1) {
                            if (deviceOptionsSet.contains(index.getOptionId())) {
                                builder.append('1').append(';');
                            } else {
                                builder.append('0').append(';');
                            }
                        }
                    }
                    // removes lat character ';' since the parent call is adding ';' for every parameter
                    builder.deleteCharAt(builder.length() - 1);
                    return builder.toString();
                case LOCATION:
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsGeographicalRegion.class, outlet.getGeographicalRegionId());
                case OUTLET:
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsCustomerOutlet.class, outlet);
                default:
                    return "";
            }
        } catch (Exception ex) {
            System.out.println("Parent Error >>>>>>>>>>>>> " + ex.getMessage());
            return " ";
        }
    }


    // this is child should not have children -- with tid mid table you can only get currency object and itself
    private String getTIDChildParamValue(ParCustomerConfigChildKeys childKey, TmsDeviceTidsMids tidsMids) {
        try {
            switch (childKey.getEntityName()) {
                case TID_MID:
                    String tid = getParamValue(childKey.getKeyName(), TmsDeviceTidsMids.class, tidsMids);
                    return tid;
                case CURRENCY:
                    UfsCurrency ufsCurrency = currencyRepository.findById(tidsMids.getCurrencyIds()).get();
                    String currency = getParamValue(childKey.getKeyName(), UfsCurrency.class, ufsCurrency);
                    return currency;
            }
        } catch (Exception ex) {
            log.error(">>>>>>>>>>>> TID Error " + childKey.getKeyName() + " >>>>>>>>>>> " + ex.getMessage());
            return "";
        }
        return "";
    }

    @Override
    public TmsDevice getDevice(BigDecimal id) {
        return tmsDeviceRepository.findByDeviceIdAndIntrash(id, "NO");
    }

    @SuppressWarnings("rawtypes")
    private String getParamValue(String param, Class clazz, Object privateObject) throws
            NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field privateStringField = clazz.getDeclaredField(param);
        privateStringField.setAccessible(true);
        Object field = privateStringField.get(privateObject);
        return privateStringField.getType() == Class.forName("java.lang.String") ? (String) field : String.valueOf(field);
    }
}
