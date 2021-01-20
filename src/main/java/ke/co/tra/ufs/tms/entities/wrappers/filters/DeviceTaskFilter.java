/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
public class DeviceTaskFilter {

    private String downloadStatus;
    private Date from;
    private Date to;
    private String deviceId;
    private String scheduleId;
    private String needle;

    public DeviceTaskFilter() {
        this.downloadStatus = "";
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.deviceId = "";
        this.scheduleId = "";
        this.needle = "";
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getNeedle() {
        return needle;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
}
