/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsAuditLog;
import ke.co.tra.ufs.tms.entities.UfsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Owori Juma
 */
public interface LoggerServiceLocal {

    /**
     * Used to save audit log with activity type write
     *
     * @param description
     * @param entity
     * @param entityId
     * @param activityStatus
     */
    public void posPinReset(String description, String entity, Object entityId, String activityStatus);

    /**
     * Used to save audit log with activity type write
     *
     * @param description
     * @param entity
     * @param entityId
     * @param activityStatus
     */
    public void logCreate(String description, String entity, Object entityId, String activityStatus);
    /**
     * 
     * @param description
     * @param entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logCreate(String description, String entity, Object entityId, String activityStatus, String notes);

    /**
     * Used to save audit log with activity type write
     *
     * @param description
     * @param entity
     * @param entityId
     * @param activityStatus
     * @param user
     */
    public void logCreate(String description, String entity, Object entityId, String activityStatus, Long user);

    /**
     * Used to save audit log with activity type write
     *
     * @param description
     * @param entity
     * @param entityId
     * @param activityStatus
     * @param user
     * @param ip
     * @param agent
     */
    public void logCreate(String description, String entity, Object entityId, String activityStatus, Long user, String ip, String agent);
    /**
     * Save log with activity type update and current authenticated user
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     */
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus);

    /**
     * Save log with activity type update and current authenticated user
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     */
    public void logCreateTask(String description, String Entity, Object entityId, String activityStatus);
    /**
     * Save log with activity type update and current authenticated user
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     */
    public void logCreateRelease(String description, String Entity, Object entityId, String activityStatus);

    /**
     * Save log with activity type update and current authenticated user
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     */
    public void logCreateDecommision(String description, String Entity, Object entityId, String activityStatus);

    /**
     * Save log with activity type update
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param user
     */
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, Long user);

    /**
     * Save log with activity type update
     *
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes
     */
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, String notes);
    /**
     * Save log with activity type approve
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logApprove(String description, String Entity, Object entityId, String activityStatus, String notes);
    
    public void logApprove(String description, String Entity, Object entityId, String activityStatus);
    /**
     * Save log with activity type deactivation
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logDeactivate(String description, String Entity, Object entityId, String activityStatus, String notes);
    /**
     * Save log with activity type activation
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logActivate(String description, String Entity, Object entityId, String activityStatus, String notes);
    /**
     * Save audit log with activity type unlock
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logUnlock(String description, String Entity, Object entityId, String activityStatus, String notes);
    /**
     * Save audit log with activity type lock
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logLock(String description, String Entity, Object entityId, String activityStatus, String notes);
    /**
     * Save log with activity type update
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes
     * @param user 
     */
    public void logUpdate(String description, String Entity, Object entityId, String activityStatus, String notes, Long user);
    


    
    public void log(String description, String Entity, Object entityId, String activity, String activityStatus);
    
    public void log(String description, String Entity, Object entityId, String activity, String activityStatus, Long user);
    /**
     * Log with deletion status
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus 
     */
    public void logDelete(String description, String Entity, Object entityId, String activityStatus);
    /**
     * Log with deletion status
     * @param description
     * @param Entity
     * @param entityId
     * @param activityStatus
     * @param notes 
     */
    public void logDelete(String description, String Entity, Object entityId, String activityStatus, String notes);  

    /**
     * Check if the specified user has performed the action
     * @param Entity
     * @param entityId
     * @param activity
     * @return 
     */
    public boolean isInitiator(String Entity, Object entityId, String activity);

   public  void logCreate(String s, String entityName, Object o, String statusFailed, String ipAddress, String userAgent);

    /**
     * get logged in userId
     * @return
     */
    public Long getUser();


    /**
     * get logged in user full name
     * @return
     */
    public String getFullName();
}
