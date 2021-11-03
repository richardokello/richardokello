package ke.co.tra.ufs.tms.service;

import java.util.List;

public interface DeviceTidsService {

    /**
     * fetch tid
     *
     * @param deviceId
     * @return
     */
    List<String> fetchData(Long deviceId);
}
