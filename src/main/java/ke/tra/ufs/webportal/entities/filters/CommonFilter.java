package ke.tra.ufs.webportal.entities.filters;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CommonFilter {
    @ApiModelProperty
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date from;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date to;
    @Size(max = 200)
    private String needle;
    @Size(max = 50)
    private String actionStatus;
    private String status;

    public CommonFilter() {
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.needle = "";
        this.actionStatus = "";
        this.status = "";
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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CommonFilter{" + "from=" + from + ", to=" + to + ", needle=" + needle + ", actionStatus=" + actionStatus + '}';
    }

}
