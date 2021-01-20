package ke.co.tra.ufs.tms.service.templates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.CustomerConfigFileService;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
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

    public CustomerConfigFileServiceTemplate(TmsDeviceRepository tmsDeviceRepository, FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService,
                                             SharedMethods sharedMethods, ParCustomerConfigIndicesRepository configIndicesRepository,
                                             ParDeviceOptionsIndicesRepository parDeviceOptionsIndicesRepository) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.configIndicesRepository = configIndicesRepository;
        this.parDeviceOptionsIndicesRepository = parDeviceOptionsIndicesRepository;
    }

    @Override
    public void generateCustomerFile(BigDecimal deviceId, String filePath) {
        log.info(">>>>>>>>>>>>>>>>>>>"+deviceId+">>>>>>>>>>>>>>>>"+filePath);
        TmsDevice tmsDevice = tmsDeviceRepository.findByDeviceIdAndIntrash(deviceId, "NO");
        if (tmsDevice == null) {
            // loggerService.log("", "", "", 1L, "", "", "");
            return;
        }

        List<ParCustomerConfigKeysIndices> parentIndices = configIndicesRepository.findAll(Sort.by(Sort.Direction.ASC, "configIndex"));
        List<String> result = new ArrayList<>();
        log.info(parentIndices.size());

        for (int i = 0; i < parentIndices.size(); i++) {
            log.info("Parent Config Id =============>"+ parentIndices.get(i).getId());
            log.info("Parent Config Id =============>"+ parentIndices.get(i).getConfigId());
            log.info("Parent Config index =============>"+ parentIndices.get(i).getConfigIndex());

            if (parentIndices.get(i).getConfig().getIsAllowed() == 1) {
                result.add(getParamValueNew(parentIndices.get(i), tmsDevice) + ";");
            };
        }
//        for (ParCustomerConfigKeysIndices parentIndex : parentIndices) {
//            log.info("Parent Config Id =============>"+ parentIndex.getId());
//            log.info("Parent Config Id =============>"+ parentIndex.getConfigId());
//            log.info("Parent Config index =============>"+ parentIndex.getConfigIndex());
//            if (parentIndex.getConfig().getIsAllowed() == 1) result.add(getParamValueNew(parentIndex, tmsDevice) + ";");
//        }
//
        log.info("results >>>>>>>>>>>>>>" + Arrays.asList(result));
        createFile(result, tmsDevice.getModelId().getModelId(), "CUSTOMER", filePath);
    }

    private String getParamValueNew(ParCustomerConfigKeysIndices parentIndex, TmsDevice device) {
        try {
            log.info("PARAMETER 1>>>>>>>>>>>>>>>>>>"+new ObjectMapper().writeValueAsString(parentIndex.getConfig().getEntityName()));
            log.info("PARAMETER 1>>>>>>>>>>>>>>>>>>"+new ObjectMapper().writeValueAsString(parentIndex.getConfig().getKeyName()));
            log.info("PARAMETER 1>>>>>>>>>>>>>>>>>>"+new ObjectMapper().writeValueAsString(device.getOutletId().getCustomerId()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            switch (parentIndex.getConfig().getEntityName()) {
                case CUSTOMER:
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsCustomer.class, device.getOutletId().getCustomerId());
                case TID_MID:
                    // has children -- get children
                    StringBuilder stringBuilder = new StringBuilder();
                    for (TmsDeviceTidsMids tidsMids : device.getDeviceTidsMidsList()) {
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

                    for (ParDeviceSelectedOptions deviceOption : device.getDeviceOptions()) {
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
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsGeographicalRegion.class, device.getOutletId().getGeographicalRegionId());
                case OUTLET:
                    return getParamValue(parentIndex.getConfig().getKeyName(), UfsCustomerOutlet.class, device.getOutletId());
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
                    log.info("TID NAME==============> "+childKey.getKeyName());
                    String tid = getParamValue(childKey.getKeyName(), TmsDeviceTidsMids.class, tidsMids);
                    return tid;
                case CURRENCY:
                    UfsCurrency ufsCurrency = tidsMids.getCurrencyId();
                    System.out.println("currency code >>>> " + ufsCurrency.getCodeName() + "Decimals >>>  " + ufsCurrency.getDecimalValue() + " Expo >>> " + ufsCurrency.getNumericValue());
                    String currency = getParamValue(childKey.getKeyName(), UfsCurrency.class, ufsCurrency);
                    System.out.println("TID >>>>  " + childKey.getKeyName() + " >>>> index: " + childKey.getChildIndex().getConfigIndex() + " >>>>>>>> result:" + currency);
                    return currency;
            }
        } catch (Exception ex) {
            System.out.println(">>>>>>>>>>>> TID Error " + childKey.getKeyName() + " >>>>>>>>>>> " + ex.getMessage());
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
