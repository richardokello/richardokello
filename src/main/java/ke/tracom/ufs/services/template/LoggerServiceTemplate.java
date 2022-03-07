package ke.tracom.ufs.services.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.config.multitenancy.ThreadLocalStorage;
import ke.tracom.ufs.wrappers.IsInitiatorResonseEntity;
import ke.tracom.ufs.wrappers.IsInitiatorWrapper;
import ke.tracom.ufs.wrappers.LogExtras;
import ke.tracom.ufs.wrappers.LogWrapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
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

    //    @Autowired
    Executor executor;

    //    @Autowired
    RestTemplate restTemplate;

    public LoggerServiceTemplate(LogExtras extras, Executor executor, RestTemplate restTemplate) {
        this.extras = extras;
        this.restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            System.out.println(request.getURI());
            return execution.execute(request, body);
        });
        this.executor = executor;
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
        System.out.println("RequestContextHolder.getRequestAttributes() == null " + (RequestContextHolder.getRequestAttributes() == null));
        HttpServletRequest req =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        executor.execute(() -> {
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = req.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
//                System.out.println("Header names >>>>> " + header + " >>> " + req.getHeader(header));
                if (!header.startsWith("x-forwarded"))
                    headers.add(header, req.getHeader(header));
            }

            HttpEntity<LogWrapper> request = new HttpEntity<>(logs, headers);
            request.getHeaders().forEach((key, value) -> System.out.println("Header names >>>>> " + key + " >>> " + value));
            ThreadLocalStorage.setTenantName(headers.getFirst("X-TenantID"));
            restTemplate.postForEntity(url + "ufs-logger-service/api/v1/logger/log", request, LogWrapper.class);

//            restTemplate.exchange(url + "ufs-logger-service/api/v1/logger/log", HttpMethod.POST, request, LogWrapper.class);
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
        try {
            System.out.println("is maker >>>> " + new ObjectMapper().writeValueAsString(ismaker) + " url >>> " + url);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
