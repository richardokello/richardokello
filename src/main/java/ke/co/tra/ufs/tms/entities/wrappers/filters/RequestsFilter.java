/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers.filters;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.Size;
/**
 *
 * @author Cornelius M
 */
public class RequestsFilter {
    
    @Size(max = 200)
    private String needle;
    private String requestBy;
    private String customerId;
    private String citAgentId;
    private BigDecimal outletId;
    private Date from;
    private Date to;  
    private String requestStage;
    private String requestStatus;
    private String requestType;

    public RequestsFilter() {
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
    }

    public String getNeedle() {
        return needle;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getCitAgentId() {
        return citAgentId;
    }

    public void setCitAgentId(String citAgentId) {
        this.citAgentId = citAgentId;
    }

    public BigDecimal getOutletId() {
        return outletId;
    }

    public void setOutletId(BigDecimal outletId) {
        this.outletId = outletId;
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

    public String getRequestStage() {
        return requestStage;
    }

    public void setRequestStage(String requestStage) {
        this.requestStage = requestStage;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
        
}
