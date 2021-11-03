package ke.co.tra.ufs.tms.entities.wrappers.filters;

import io.swagger.annotations.ApiModelProperty;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

public class DecommisionedDevicesFilter {

    @Size(max = 50)
    private String action;
    @Size(max = 50)
    private String actionStatus;
    @Size(max = 200)
    private String needle;
    @ApiModelProperty
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date from;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date to;

    public DecommisionedDevicesFilter() {
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.needle = "";
        this.action = AppConstants.ACTIVITY_DECOMMISSION;
        this.actionStatus = AppConstants.ACTION_STATUS_APPROVED;
    }


}
