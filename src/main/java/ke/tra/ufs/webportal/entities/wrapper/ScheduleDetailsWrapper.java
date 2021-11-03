/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.wrapper;

/**
 *
 * @author Owori Juma
 */
public class ScheduleDetailsWrapper {
    private Integer success;
    private Integer failed;
    private Integer total;
    private Integer noOfDevices;
    private Integer pending;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getNoOfDevices() {
        return noOfDevices;
    }

    public void setNoOfDevices(Integer noOfDevices) {
        this.noOfDevices = noOfDevices;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }
    
    
}
