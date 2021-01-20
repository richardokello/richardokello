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
public class BusinessUnitFilter {

    private Date to;
    private Date from;
    @Size(max = 200)
    private String needle;
    @Size(max = 50)
    private String status;
    @Size(max = 50)
    private String actionStatus;
    private String productId;

    public BusinessUnitFilter() {
        this.needle = "";
        this.status = "";
        this.actionStatus = "";
        this.from = new Date(0);
        this.productId="";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
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

    public Date getFrom() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        return cal.getTime();
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public String getNeedle() {
        return needle;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    
}
