package ke.tracom.ufs.services.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.wrappers.IsInitiatorResonseEntity;
import ke.tracom.ufs.wrappers.IsInitiatorWrapper;
import ke.tracom.ufs.wrappers.LogExtras;
import ke.tracom.ufs.wrappers.LogWrapper;
import lombok.extern.apachecommons.CommonsLog;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Owori Juma
 */

@Service
@CommonsLog
public class LoggerServiceTemplate implements LoggerService {
    private final LogExtras extras;
    @Value("${gatewayUrl}")
    private String url;
    @Autowired
    Executor executor;

    @Autowired
    RestTemplate restTemplate;

    public LoggerServiceTemplate(LogExtras extras) {
        this.extras = extras;
    }


    @Override
    public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
        LogWrapper log = wrapper(description, entity, entityId, activity, activityStatus, notes + " by " + extras.getFullName());
        log.set_userId(extras.getUserId());
        sendLog(log);
    }


    private void sendLog(LogWrapper logs) {
        try {
            log.error("Data, =>" + new ObjectMapper().writeValueAsString(logs));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        executor.execute(() -> {
            restTemplate.postForEntity(url + "ufs-logger-service/api/v1/logger/log", logs, LogWrapper.class);
        });
    }

    @Override
    public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes) {
        LogWrapper log = wrapper(description, entity, entityId, activity, activityStatus, notes + " by " + extras.getFullName());
        log.set_userId(userId);
        sendLog(log);
    }

    @Override
    public boolean isInitiator(String Entity, Object entityId, String activity) {
        List<IsInitiatorWrapper> payload = new ArrayList<>();
        IsInitiatorWrapper ismaker = new IsInitiatorWrapper(new BigDecimal(extras.getUserId()), Entity, entityId.toString(), activity);
        payload.add(ismaker);

        IsInitiatorResonseEntity res = restTemplate.postForObject(url + "ufs-logger-service/api/v1/logger/log/is-initiator", payload, IsInitiatorResonseEntity.class);
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
