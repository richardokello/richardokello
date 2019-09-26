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
import ke.tra.ufs.webportal.wrappers.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
        List<Header> headerList = new ArrayList<>();
        List<Row> rowList = new ArrayList<>();
        //find all tmsDevice for a particular region

        List<UfsGeographicalRegion> ufsGeographicalRegionList = ufsGeographicalRegionRepository.findAll();

        ufsGeographicalRegionList.forEach(ids -> {

            Header header = new Header();
            header.setTitle(ids.getRegionName());

            Set<TmsDevice> tmsDeviceList = tmsDeviceRepository.findAllByGeographicalRegionIds(ids.getId());
            Set<Long> deviceIdsSet = tmsDeviceList.stream().map(x-> x.getDeviceId().longValue()).collect(Collectors.toSet());//get all device Id
            Set<TmsDeviceTids> tmsDeviceTidsList = tmsDeviceTidRepository.findAllByDeviceIdsIn(deviceIdsSet);
            Set<String> tidSet = tmsDeviceTidsList.stream().map(TmsDeviceTids::getTid).collect(Collectors.toSet());// get all Tids
            List<VwUfsAlltxns> vwUfsAlltxnsList = vwUfsAlltxnsRepository.findAllByTidInAndTransactiontypeIsNotNull(tidSet);

            Map<String,Long> map1 =vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype,Collectors.counting()));//count all per transaction types
            Map<String,Long> map2 = vwUfsAlltxnsList.stream().collect(Collectors.groupingBy(VwUfsAlltxns::getTransactiontype,Collectors.summingLong(x -> Long.parseLong(x.getAmount()))));// count all per amount

            map1.entrySet().forEach(x -> {
                Row row = new Row();
                Values values = new Values();
                values.setAmount(map2.get(x.getKey()));
                values.setCount(map1.get(x.getKey()));

                row.setType(x.getKey());
                row.setValues(values);
                rowList.add(row);
            });

            headerList.add(header);
        });

        reportData.setHeader(headerList);
        reportData.setRows(rowList);


        responseWrapper.setData(reportData);

        return responseWrapper;
    }
}
