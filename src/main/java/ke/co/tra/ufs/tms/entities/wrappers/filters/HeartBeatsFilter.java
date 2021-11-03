/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author tracom9
 */
public class HeartBeatsFilter {

    private String applicationVersion;
    @Size(max = 20)
    private String chargingStatus;
    @Size(max = 20)
    private String osVersion;
    @Size(max = 50)
    private String serialNo;
    @ApiModelProperty
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date from;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date to;
    private String needle;

    public HeartBeatsFilter() {
        this.applicationVersion = "";
        this.chargingStatus = "";
        this.osVersion = "";
        this.serialNo = "";
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.needle = "";
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(String chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Date getFrom() {
        LocalDateTime fromTime = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(fromTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        LocalDateTime toTime = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59).withNano(0);
        return Date.from(toTime.atZone(ZoneId.systemDefault()).toInstant());
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
}
