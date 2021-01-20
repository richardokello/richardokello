/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Owori Juma
 */
public class ScheduleFilter {
    private String from;
    private String to;
    private Date from_;
    private Date to_;
    private String status;
    private String appId;
    private String scheduleType;
    private String downloadType;
    private String action;
    private String actionStatus;
    private String needle;

    public ScheduleFilter() {

        this.from_ = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        this.to_ = cal.getTime();
        this.status = "";
        this.appId = "";
        this.scheduleType = "";
        this.downloadType = "";
        this.action = "";
        this.actionStatus = "";
        this.needle = "";
    }

    public Date getFrom_() {
        if (Objects.nonNull(from_)) {
            return this.from_;
        }else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(from_);
            cal.add(Calendar.DAY_OF_WEEK, -1);
            return cal.getTime();
        }
    }

    public void setFrom_(Date from_) {
        this.from_ = from_;
    }

    public Date getTo_() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(to_);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    public void setTo_(Date to_) {
        this.to_ = to_;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.setFrom_(tryParse(from));
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to_ = tryParse(to);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public String getNeedle() {
        return needle;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    private Date tryParse(String dateString) {
        List<String> formatStrings = Arrays.asList(
                "dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy",
                "dd/MM/yyyy HH:mm:ss.SSS", "dd/MM/yyyy HH:mm:ss",
                "dd-MM-yyyy HH:mm:ss.SSS", "dd-MM-yyyy HH:mm:ss");
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            } catch (ParseException e) {
            }
        }

        return null;
    }

}
