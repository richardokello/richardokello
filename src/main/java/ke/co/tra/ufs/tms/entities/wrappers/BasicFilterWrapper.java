/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers;

import java.util.Date;
import javax.validation.constraints.Size;

/**
 *
 * @author Owori Juma
 * @param <T>
 */
public class BasicFilterWrapper<T> {
    
    private T entityId;
    private Date from;
    private Date to;
    @Size(min = 1, max = 50)
    private String actionStatus;
    private Date creationDate;

    public T getEntityId() {
        return entityId;
    }

    public void setEntityId(T entityId) {
        this.entityId = entityId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    

}
