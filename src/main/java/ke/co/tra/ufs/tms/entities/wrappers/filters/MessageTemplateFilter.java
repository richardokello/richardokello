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
public class MessageTemplateFilter {
    
    public String actionStatus;

    public MessageTemplateFilter() {
        actionStatus = "";
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }   

}
