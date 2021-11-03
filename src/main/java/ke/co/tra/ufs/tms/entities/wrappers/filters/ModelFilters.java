/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers.filters;

/**
 *
 * @author Cornelius M
 */
public class ModelFilters extends CommonFilter {
    
    private String makeId;
    private String deviceTypeId;

    public ModelFilters() {
        this.makeId = "";
        this.deviceTypeId = "";
    }

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
    
    

}
