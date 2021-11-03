/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

import javax.validation.constraints.Size;

/**
 *
 * @author ojuma
 */
public class DeviceCustomerFilter {
    @Size(max = 200)
    private String needle;
    @Size(max = 50)
    private String actionStatus;
    private String status;

    public DeviceCustomerFilter() {
        this.needle = "";
        this.actionStatus = "";
        this.status = "";
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
    
    
    
    
    
}
