/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

/**
 *
 * @author Owori Juma
 */
public class DevicesFilter extends CommonFilter {

    private String action;

    public DevicesFilter() {
        this.action = "";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "DevicesFilter{" + "action=" + action + '}';
    }
    
    

}
