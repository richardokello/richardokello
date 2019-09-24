package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.wrappers.IsInitiatorResonseEntity;
import ke.tra.ufs.webportal.wrappers.IsInitiatorWrapper;
import ke.tra.ufs.webportal.wrappers.LogExtras;
import ke.tra.ufs.webportal.wrappers.LogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
@Transactional
public class LoggerServiceTemplate implements LoggerService {
    private final LogExtras extras;
    @Value("${baseUrl}")
    private String url;
    @Autowired
    Executor executor;

    private final RestTemplate temp;

    public LoggerServiceTemplate(LogExtras extras) {
        this.extras = extras;
        this.temp = new RestTemplate();
    }


    @Override
    public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
        LogWrapper log = wrapper(description, entity, entityId, activity, activityStatus, notes);
        log.set_userId(extras.getUserId());
        sendLog(log);
    }


    private void sendLog(LogWrapper log) {
        executor.execute(() -> {
            temp.postForEntity(url + "ufs-logger-service/api/v1/logger/log", log, LogWrapper.class);
        });
    }

    @Override
    public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes) {
        LogWrapper log = wrapper(description, entity, entityId, activity, activityStatus, notes);
        log.set_userId(userId);
        sendLog(log);
    }

    @Override
    public boolean isInitiator(String Entity, Object entityId, String activity) {
        //return false;
        RestTemplate temp = new RestTemplate();
        List<IsInitiatorWrapper> payload = new ArrayList<>();
        IsInitiatorWrapper ismaker = new IsInitiatorWrapper(new BigDecimal(extras.getUserId()), Entity, entityId.toString(), activity);
        payload.add(ismaker);

        IsInitiatorResonseEntity res = temp.postForObject(url + "ufs-logger-service/api/v1/logger/log/is-initiator", payload, IsInitiatorResonseEntity.class);
        return res.getData().getAllowewd().size() <= 0;
    }

    private LogWrapper wrapper(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
        String entityHolder = (entityId == null) ? null : entityId.toString();
        LogWrapper log = new LogWrapper();
        log.setDescription(description);
        log.setEntityName(entity);
        log.setEntityId(entityHolder);
        log.setActivityType(activity);
        log.setStatus(activityStatus);
        log.setNotes(notes);

        log.setSource(extras.getSource());
        log.setIpAddress(extras.getIpAddress());
        log.setClientId(extras.getClientId());
        return log;
    }


}
