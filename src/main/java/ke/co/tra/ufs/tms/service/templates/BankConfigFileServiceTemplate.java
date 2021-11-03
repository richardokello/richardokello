package ke.co.tra.ufs.tms.service.templates;

import ke.axle.chassis.utils.AppConstants;
import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.BankConfigFileService;
import ke.co.tra.ufs.tms.service.FileExtensionRepository;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BankConfigFileServiceTemplate extends ParFileService implements BankConfigFileService {

    private final TmsDeviceRepository tmsDeviceRepository;
    private final TmsDeviceTidMidRepository tmsDeviceTidMidRepository;
    private final UfsBankRepository bankRepository;
    private final TidMidCurrencyRepository tidMidCurrencyRepository;
    private final CurrencyRepository currencyRepository;

    public BankConfigFileServiceTemplate(FileExtensionRepository fileExtensionRepository, LoggerServiceVersion loggerService, SharedMethods sharedMethods, TmsDeviceRepository tmsDeviceRepository, TmsDeviceTidMidRepository tmsDeviceTidMidRepository, UfsBankRepository bankRepository, TidMidCurrencyRepository tidMidCurrencyRepository, CurrencyRepository currencyRepository) {
        super(fileExtensionRepository, loggerService, sharedMethods);
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.tmsDeviceTidMidRepository = tmsDeviceTidMidRepository;
        this.bankRepository = bankRepository;
        this.tidMidCurrencyRepository = tidMidCurrencyRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void generateBankFile(BigDecimal deviceId, String filePath) {
        //FENACOBU|10000001|500000000000001|8002|BIF,108,2#USD,840,2#EURO,970,2;
        // CECM|60000007|000000010000015|8001|BIF,108,2#USD,840,2#EURO,970,2;
        TmsDevice tmsDevice = tmsDeviceRepository.findByDeviceIdAndIntrash(deviceId, "NO");
        if (tmsDevice == null) {
            return;
        }
        List<String> result = new ArrayList<>();
        StringBuilder tidMidBuilder = new StringBuilder();
        StringBuilder currencyBuilder = new StringBuilder();
        StringBuilder bankFileBuilder;

        List<TmsDeviceTidsMids> tidsMidsList = tmsDeviceTidMidRepository.findAllByDeviceIds(tmsDevice.getDeviceId().longValue());

        for (TmsDeviceTidsMids tidsMids : tidsMidsList) {
            // save the values in arraylist
            UfsBanks ufsBanks = bankRepository.findById(tidsMids.getUfsBankId()).get();
            tidMidBuilder.append(ufsBanks.getBankName())
                    .append("|")
                    .append(tidsMids.getTid())
                    .append("|")
                    .append(tidsMids.getMid())
                    .append("|")
                    .append(ufsBanks.getPort())
                    .append("|");
            List<TidMidCurrency> tidMidCurrencies = tidMidCurrencyRepository.findAllByTidMidIds(tidsMids.getId());
            if(Objects.nonNull(tidMidCurrencies) && tidMidCurrencies.size()>0){
                //needs to be optimized its running 0(n^2)
                tidMidCurrencies.forEach(tidMidCurr->{
                    UfsCurrency ufsCurrency = currencyRepository.findById(tidMidCurr.getCurrencyIds()).get();
                    currencyBuilder
                            .append(ufsCurrency.getCode())
                            .append(",")
                            .append(ufsCurrency.getNumericValue())
                            .append(",")
                            .append(ufsCurrency.getDecimalValue())
                            .append("#");

                });
            }
            bankFileBuilder = tidMidBuilder.append(currencyBuilder);
            bankFileBuilder.replace(bankFileBuilder.length()-1,bankFileBuilder.length(),";");
            result.add(bankFileBuilder.toString());
        }
        createFile(result, tmsDevice.getModelId().getModelId(), "BANK", filePath);

    }

    @Override
    public TmsDevice getDevice(BigDecimal id) {
        return tmsDeviceRepository.findByDeviceIdAndIntrash(id, AppConstants.NO);
    }

}
