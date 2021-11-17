/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.UfsAuditLog;
import ke.co.tra.ufs.tms.entities.UfsAuthentication;
import ke.co.tra.ufs.tms.repository.AuditLogRepository;
import ke.co.tra.ufs.tms.repository.AuthenticationRepository;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.CustomEntry;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.RunTimeBadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Owori Juma
 */
@Service
@Transactional
public class LoggerServiceVersion implements LoggerServiceLocal, LoggerService {

    private final HttpServletRequest request;
    private final AuditLogRepository auditLogRepo;
    private final Logger log;
    @Autowired
    AuthenticationRepository urepo;

    //   Logged in user
    private String fullname;


    public LoggerServiceVersion(HttpServletRequest request, AuditLogRepository auditLogRepo) {
        this.request = request;
        this.auditLogRepo = auditLogRepo;
        this.log = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    public void posPinReset(String description, String entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_RESET_POS_PIN, activityStatus, null, this.getUser(), ipAddress, source);

    }

    @Override
    public void logCreate(String description, String entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_CREATE, activityStatus, null, this.getUser(), ipAddress, source);

    }

    @Override
    public void logCreate(String description, String entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_CREATE, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logCreate(String description, String entity, Object entityId, String activityStatus, Long user) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_CREATE, activityStatus, null, user, ipAddress, source);

    }

    @Override
    public void logCreate(String description, String entity, Object entityId, String activityStatus, Long user, String ipAddress, String source) {
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_CREATE, activityStatus, "Created Successfully", user, ipAddress, source);
    }


    @Override
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_UPDATE, activityStatus, null, this.getUser(), ipAddress, source);
    }

    @Override
    public void logCreateTask(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_TASK, activityStatus, null, this.getUser(), ipAddress, source);
    }

    @Override
    public void logCreateDecommision(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_DECOMMISSION, activityStatus, null, this.getUser(), ipAddress, source);

    }

    @Override
    public void logCreateRelease(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_RELEASE, activityStatus, null, this.getUser(), ipAddress, source);

    }
    @Override
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, Long user) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_UPDATE, activityStatus, null, user, ipAddress, source);
    }

    @Override
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_UPDATE, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logApprove(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_APPROVE, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logDeactivate(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_DEACTIVATE, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logActivate(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_ACTIVATION, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logUnlock(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_UNLOCK, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logLock(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_LOCK, activityStatus, notes + " by " + getFullName(), this.getUser(), ipAddress, source);
    }

    @Override
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, String notes, Long user) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_UPDATE, activityStatus, notes + " by " + getFullName(), user, ipAddress, source);
    }

    @Override
    public Long getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println("LOGGED IN USER : " + username);
        UfsAuthentication userAuth = urepo.findByusernameIgnoreCase(username);
        fullname = userAuth.getUser().getFullName();
        return userAuth.getUserId();
    }

    /*
     *  full name of the currently logged in user
     */
    @Override
    public String getFullName() {
        return fullname;
    }

    private void persistLog(String description, CustomEntry<String, String> entity, String activityType, String status, Long user) {
        this.persistLog(description, entity, activityType, status, null, user);
    }

    private void persistLog(String description, CustomEntry<String, String> entity,
                            String activityType, String status, String notes, Long user) {
        String ipAddress = request.getRemoteAddr();//request.getParameter("userIp");
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        auditLogRepo.save(
                //                new UfsAuditLog(SharedMethods.currentDate(),
                //                activityType, status, entity.getValue(), entity.getKey(),
                //                description, source, ipAddress, user
                //        )
                new UfsAuditLog(SharedMethods.currentDate(), activityType, status, entity.getValue(),
                        entity.getKey(), description, source, ipAddress, user, notes + " by " + getFullName()));
        log.info("AUDIT_LOG => {}; for entity {}, from userIp {}, user agent {}, status", description, entity, ipAddress, source, status);

    }

    private void persistLog(String description, CustomEntry<String, String> entity,
                            String activityType, String status, String notes, Long user, String ipAddress, String source) {
        auditLogRepo.save(
                new UfsAuditLog(SharedMethods.currentDate(), activityType, status, entity.getValue(),
                        entity.getKey(), description, source, ipAddress, user, notes + " by " + getFullName()));
        log.info("AUDIT_LOG => {}; for entity {}, from userIp {}, user agent {}, status", description, entity, ipAddress, source, status);

    }

    private void persistLog(String description, String entity, String entityId,
                            String activityType, String status, String notes, Long user, String ipAddress, String source) {
        auditLogRepo.save(
                new UfsAuditLog(SharedMethods.currentDate(), activityType, status, entity,
                        entityId, description, source, ipAddress, user, notes + " by " + getFullName()));
        log.info("AUDIT_LOG => {}; for entity {}, from userIp {}, user agent {}, status", description, entity, ipAddress, source, status);

    }

    private void persistAuditTrail(String description, CustomEntry<String, String> entity, String activityType, String status, Long user, String notes) {
        String ipAddress = request.getParameter("userIp");
        String source = request.getParameter("userAgent");
        if (ipAddress == null) {
            throw new RunTimeBadRequest("Sorry userIp is required");
        } else if (source == null) {
            throw new RunTimeBadRequest("Sorry userAgent details is required i.e. OS, Browser/Application");
        }
        auditLogRepo.save(new UfsAuditLog(SharedMethods.currentDate(),
                activityType, status, entity.getValue(), entity.getKey(),
                description, source, ipAddress, user, notes + " by " + getFullName()
        ));
    }


    @Override
    public void log(String description, String Entity, Object entityId, String activity, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), activity, activityStatus, null, this.getUser(), ipAddress, source);

    }

    @Override
    public void log(String description, String Entity, Object entityId, String activity, String activityStatus, Long user) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), activity, activityStatus, null, user, ipAddress, source);
    }

    @Override
    public void logDelete(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_DELETE, activityStatus, null, this.getUser(), ipAddress, source);
    }

    @Override
    public void logDelete(String description, String Entity, Object entityId, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_DELETE, activityStatus, notes, this.getUser(), ipAddress, source);
    }


    @Override
    public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
//        String ipAddress = request.getRemoteAddr();
//        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
            RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
            if (attribs instanceof NativeWebRequest) {
                HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
                String ipAddress =  request.getRemoteAddr();
                String source =  org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
                this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), activity, activityStatus, notes, this.getUser(), ipAddress, source);
            }

    }

    @Override
    public boolean isInitiator(String Entity, Object entityId, String activity) {
        Long user_id = getUser();
        System.out.println("LOGGED IN USER ID : " + user_id);
        return auditLogRepo.isInitiator(user_id, Entity, entityId.toString(), activity, AppConstants.STATUS_COMPLETED);
    }

    @Override
    public void logCreate(String s, String entityName, Object o, String statusFailed, String ipAddress, String userAgent) {
        this.persistLog(s, entityName, (o == null) ? null : o.toString(), AppConstants.ACTIVITY_CREATE, statusFailed, "Created By "+this.getFullName(), this.getUser(), ipAddress, userAgent);
    }

    @Override
    public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, entity, (entityId == null) ? null : entityId.toString(), activity, activityStatus, notes, this.getUser(), ipAddress, source);
    }

    @Override
    public void logApprove(String description, String Entity, Object entityId, String activityStatus) {
        String ipAddress = request.getRemoteAddr();
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        this.persistLog(description, Entity, (entityId == null) ? null : entityId.toString(), AppConstants.ACTIVITY_APPROVE, activityStatus, "", this.getUser(), ipAddress, source);
    }

}
