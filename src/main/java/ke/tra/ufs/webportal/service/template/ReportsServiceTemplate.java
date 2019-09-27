package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsDeviceTids;
import ke.tra.ufs.webportal.entities.TransactionTypes;
import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;
import ke.tra.ufs.webportal.entities.views.VwUfsAlltxns;
import ke.tra.ufs.webportal.repository.*;
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
    private final TransactionTypeRepository transactionTypeRepository;

    public ReportsServiceTemplate(TmsDeviceRepository tmsDeviceRepository, TmsDeviceTidRepository tmsDeviceTidRepository, VwUfsAlltxnsRepository vwUfsAlltxnsRepository, UfsGeographicalRegionRepository ufsGeographicalRegionRepository, TransactionTypeRepository transactionTypeRepository) {
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.tmsDeviceTidRepository = tmsDeviceTidRepository;
        this.vwUfsAlltxnsRepository = vwUfsAlltxnsRepository;
        this.ufsGeographicalRegionRepository = ufsGeographicalRegionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public ResponseWrapper generateGeographicalReports() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        ReportData reportData = new ReportData();


        List<Row> rowList = new ArrayList<>();

        //find all tmsDevice for a particular region
        Iterable<UfsGeographicalRegion> ufsGeographicalRegionList = ufsGeographicalRegionRepository.findAll();
        Total total = new Total();

        /*
        for (UfsGeographicalRegion ids : ufsGeographicalRegionList) {

            AtomicReference<Long> totalamout = new AtomicReference<>(0L);
            AtomicReference<Long> totalCount = new AtomicReference<>(0L);



            Set<TmsDevice> tmsDeviceList = tmsDeviceRepository.findAllByGeographicalRegionIds(ids.getId());
            Set<Long> deviceIdsSet = tmsDeviceList.stream().map(x -> x.getDeviceId().longValue()).collect(Collectors.toSet());//get all device Id
            Set<TmsDeviceTids> tmsDeviceTidsList = tmsDeviceTidRepository.findAllByDeviceIdsIn(deviceIdsSet);
            Set<String> tidSet = tmsDeviceTidsList.stream().map(TmsDeviceTids::getTid).collect(Collectors.toSet());// get all Tids
            List<VwUfsAlltxns> vwUfsAlltxnsList = vwUfsAlltxnsRepository.findAllByTidInAndTransactiontypeIsNotNull(tidSet);

            Map<String, Long> map1 = vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype, Collectors.counting()));//count all per transaction types
            Map<String, Long> map2 = vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype, Collectors.summingLong(x -> Long.parseLong(x.getAmount()))));// count all per amount


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


        };*/
        //totals
        Row totals = new Row();
        totals.setTransactionType("Totals");
        List<Values> values_total = new ArrayList<>();

        //get transactions types
        Iterable<TransactionTypes> transactionTypes = transactionTypeRepository.findAll();
        transactionTypes.forEach(trans -> {
            Row rw = new Row();
            rw.setTransactionType(trans.getTxnName());
            List<Values> val = new ArrayList<>();
            Set<Header> headerList = new HashSet<>();
            Values valueTotal = new Values();

            int totalCount = 0;
            //loop regions
            for (UfsGeographicalRegion region : ufsGeographicalRegionList) {
                //Add Header title
                Header header = new Header();
                header.setTitle(region.getRegionName());
                headerList.add(header);
                //get devices per region
                Set<Long> deviceIds = tmsDeviceRepository.findAllByGeographicalRegionIds(region.getId()).stream().map(x -> x.getDeviceId().longValue()).collect(Collectors.toSet());

                //fetch TIDS required
                Set<String> tids = tmsDeviceTidRepository.findAllByDeviceIdsIn(deviceIds).stream().map(TmsDeviceTids::getTid).collect(Collectors.toSet());

                //fetch transactions by type and tids
                List<VwUfsAlltxns> transactionsByTypes = vwUfsAlltxnsRepository.findAllByTidInAndTransactiontype(tids, trans.getTxnName());

                //Set values transactions
                Optional<Long> amount = transactionsByTypes.stream().map(x -> Long.valueOf(x.getAmount())).reduce(Long::sum);
                int count = transactionsByTypes.size();
                Values values = new Values();
                values.setCount(count);
                totalCount += count;

                if (amount.isPresent()) {
                    values.setAmount(amount.get());
                } else {
                    values.setAmount(0L);
                }
                val.add(values);


            }

            reportData.setHeader(headerList);
            rw.setValues(val);
            rowList.add(rw);
        });


//        rowList.add(totals);
        reportData.setRows(rowList);

        responseWrapper.setData(reportData);

        return responseWrapper;
    }
}
