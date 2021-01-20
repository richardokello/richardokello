/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers.filters;

import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.Size;

/**
 *
 * @author Cornelius M
 */
public class AuditLogFilter {
    
    private Date from;
    private Date to;
    @Size(max = 200)
    private String needle;
    @Size(max = 200)
    private String userId;
    @Size(max = 200)
    private String status;
    @Size(max = 200)
    private String activityType;
    @Size(max = 50)
    private String entityName;
    @Size(max = 50)
    private String entityId;

    public AuditLogFilter() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.from = new Date(0);
        this.needle = "";
        this.userId = "";
        this.status = "";
        this.activityType = "";
        this.entityName = "";
        this.entityId = "";
    }

    public Date getFrom() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        return cal.getTime();
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(to);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getNeedle() {
        return needle;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

}
