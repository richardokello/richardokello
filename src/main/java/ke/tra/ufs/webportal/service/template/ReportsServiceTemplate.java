package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsDeviceTids;
import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;
import ke.tra.ufs.webportal.entities.views.VwUfsAlltxns;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.repository.TmsDeviceTidRepository;
import ke.tra.ufs.webportal.repository.UfsGeographicalRegionRepository;
import ke.tra.ufs.webportal.repository.VwUfsAlltxnsRepository;
import ke.tra.ufs.webportal.service.ReportsService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.wrappers.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ReportsServiceTemplate implements ReportsService {

    private final TmsDeviceRepository tmsDeviceRepository;
    private final TmsDeviceTidRepository tmsDeviceTidRepository;
    private final VwUfsAlltxnsRepository vwUfsAlltxnsRepository;
    private final UfsGeographicalRegionRepository ufsGeographicalRegionRepository;

    public ReportsServiceTemplate(TmsDeviceRepository tmsDeviceRepository, TmsDeviceTidRepository tmsDeviceTidRepository, VwUfsAlltxnsRepository vwUfsAlltxnsRepository, UfsGeographicalRegionRepository ufsGeographicalRegionRepository) {
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.tmsDeviceTidRepository = tmsDeviceTidRepository;
        this.vwUfsAlltxnsRepository = vwUfsAlltxnsRepository;
        this.ufsGeographicalRegionRepository = ufsGeographicalRegionRepository;
    }

    @Override
    public ResponseWrapper generateGeographicalReports() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        ReportData reportData = new ReportData();
        Row row = new Row();
        List<Values> valuesList = new ArrayList<>();
        List<Values> valuesList1 = new ArrayList<>();
        List<Values> valuesList2 = new ArrayList<>();
        List<Header> headerList = new ArrayList<>();
        List<Row> rowList = new ArrayList<>();

        //find all tmsDevice for a particular region
        List<UfsGeographicalRegion> ufsGeographicalRegionList = ufsGeographicalRegionRepository.findAll();
        Total total = new Total();

        for (UfsGeographicalRegion ids:ufsGeographicalRegionList)  {

            AtomicReference<Long> totalamout = new AtomicReference<>(0L);
            AtomicReference<Long> totalCount = new AtomicReference<>(0L);

            Header header = new Header();
            header.setTitle(ids.getRegionName());

            Set<TmsDevice> tmsDeviceList = tmsDeviceRepository.findAllByGeographicalRegionIds(ids.getId());
            Set<Long> deviceIdsSet = tmsDeviceList.stream().map(x-> x.getDeviceId().longValue()).collect(Collectors.toSet());//get all device Id
            Set<TmsDeviceTids> tmsDeviceTidsList = tmsDeviceTidRepository.findAllByDeviceIdsIn(deviceIdsSet);
            Set<String> tidSet = tmsDeviceTidsList.stream().map(TmsDeviceTids::getTid).collect(Collectors.toSet());// get all Tids
            List<VwUfsAlltxns> vwUfsAlltxnsList = vwUfsAlltxnsRepository.findAllByTidInAndTransactiontypeIsNotNull(tidSet);

            Map<String,Long> map1 =vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype,Collectors.counting()));//count all per transaction types
            Map<String,Long> map2 = vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype,Collectors.summingLong(x -> Long.parseLong(x.getAmount()))));// count all per amount


            Values values = new Values();
            values.setAmount(map2.get(AppConstants.TRANSACTION_TYPE_WITHDRAWAL));
            values.setCount(map1.get(AppConstants.TRANSACTION_TYPE_WITHDRAWAL));

            Values values1 = new Values();
            values1.setAmount(map2.get(AppConstants.TRANSACTION_TYPE_DEPOSIT));
            values1.setCount(map1.get(AppConstants.TRANSACTION_TYPE_DEPOSIT));

            valuesList.add(values);
            valuesList1.add(values1);

            Withdrawal withdrawal = new Withdrawal();
            Deposit deposit = new Deposit();

            withdrawal.setValues(valuesList);
            deposit.setValues(valuesList1);

            Values values2 = new Values();
            map1.keySet().forEach(k -> {
                totalamout.updateAndGet(v -> v + map2.get(k));
                totalCount.updateAndGet(v -> v + map1.get(k));
            });
            values2.setAmount(totalamout.get());
            values2.setCount(totalCount.get());

            valuesList2.add(values2);
            total.setValues(valuesList2);


            row.setWithdrawal(withdrawal);
            row.setDeposit(deposit);
            row.setTotal(total);

            headerList.add(header);

        };

        rowList.add(row);

        reportData.setRows(rowList);
        reportData.setHeader(headerList);

        responseWrapper.setData(reportData);

        return responseWrapper;
    }
}
