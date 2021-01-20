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
 * @author Owori Juma
 */
public class RoleFilters {
    
    private Date from;
    private Date to;
    @Size(max = 200)
    private String needle;
    @Size(max = 50)
    private String actionStatus;

    public RoleFilters() {
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.needle = "";
        this.actionStatus = "";
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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }
    
    

}
