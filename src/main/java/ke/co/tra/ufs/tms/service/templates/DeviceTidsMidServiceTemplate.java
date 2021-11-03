package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import ke.co.tra.ufs.tms.repository.TmsDeviceTidMidRepository;
import ke.co.tra.ufs.tms.service.DeviceTidsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceTidsMidServiceTemplate implements DeviceTidsService {

    @Autowired
    private TmsDeviceTidMidRepository tmsDeviceTidRepository;

    @Override
    public List<String> fetchData(Long deviceId) {
        List<TmsDeviceTidsMids> tmsDeviceTidsList = tmsDeviceTidRepository.findAllByDeviceIds(deviceId);
        List<String> tidString = tmsDeviceTidsList.stream().parallel().map(tids -> tids.getTid()).collect(Collectors.toList());
        return tidString;
    }
}
