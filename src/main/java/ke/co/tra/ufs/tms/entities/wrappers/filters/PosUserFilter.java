/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;



import javax.validation.constraints.Size;
import java.util.Calendar;

/**
 *
 * @author Owori Juma
 */
public class PosUserFilter extends CommonFilter {

    private String action;
    @Size(max = 200)
    private String needle;
    @Size(max = 50)
    private String actionStatus;

    public PosUserFilter() {

        this.action = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.needle = "";
        this.actionStatus = "";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    @Override
    public String toString() {
        return "PosUserFilter{" +", needle=" + needle + ", actionStatus=" + actionStatus + '}';
    }
    
    

}
