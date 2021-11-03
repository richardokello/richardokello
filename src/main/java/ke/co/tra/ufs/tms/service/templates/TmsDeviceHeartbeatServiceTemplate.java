package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import ke.co.tra.ufs.tms.entities.TmsDeviceHeartbeat;
import ke.co.tra.ufs.tms.repository.TmsDeviceHeartbeatRepository;
import ke.co.tra.ufs.tms.service.TmsDeviceHeartbeatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori juma
 */
@Service
@Transactional
public class TmsDeviceHeartbeatServiceTemplate implements TmsDeviceHeartbeatService {

    private final TmsDeviceHeartbeatRepository heartbeatRepository;

    public TmsDeviceHeartbeatServiceTemplate(TmsDeviceHeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @Override
    public Page<TmsDeviceHeartbeat> findAll(Pageable pg) {
        return heartbeatRepository.findAll(pg);
    }

    @Override
    public Page<TmsDeviceHeartbeat> findBySerialNo(String serialNo, Pageable pg) {
        System.out.println("Filtered Serial : " + serialNo.substring(serialNo.length() - 8));
        return heartbeatRepository.findBySerialNo(serialNo, serialNo.substring(serialNo.length() - 8), pg);
    }

    @Override
    public Page<TmsDeviceHeartbeat> findAll(String applicationVersion, String chargingStatus, String osVersion, String serialNo, Date from, Date to,String needle, Pageable pg) {
        return heartbeatRepository.findAll(applicationVersion, chargingStatus, osVersion, serialNo,from,to,needle, pg);
    }

    @Override
    public Optional<TmsDeviceHeartbeat> findById(BigDecimal logId) {
        return heartbeatRepository.findById(logId);
    }

    @Override
    public Integer findTodaysHeartbeats() {
        //To get time at the beginning of the day
        LocalDateTime dateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Date datefrom = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        return heartbeatRepository.findTodaysHeartBeats(datefrom);
    }

    @Override
    public Integer findAdaysHeartbeats(LocalDateTime dateTime) {
        LocalDateTime from = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = dateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
        Date datefrom = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        Date dateto = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("From :"+datefrom +" to Date : "+dateto);
        return heartbeatRepository.findAdaysHeartBeats(datefrom, dateto);
    }
}
