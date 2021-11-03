/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers.filters;

import javax.validation.constraints.Size;

/**
 *
 * @author Cornelius M
 */
public class ConfigFilter {
    @Size(max = 200)
    private String needle;
    @Size(max = 20)
    private String actionStatus;
    @Size(max = 50)
    private String entity;

    public ConfigFilter() {
        this.needle = "";
        this.actionStatus = "";
        this.entity = "";
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

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

}
