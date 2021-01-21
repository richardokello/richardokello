/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tracom.ufs.entities.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 *
 * @author Owori Juma
 */
public class UfsSysConfigWrapper {

    private BigDecimal id;
    @NotNull
    @Size(min = 1, max = 50)
    private String entity;
    @NotNull
    @Size(min = 1, max = 50)
    private String parameter;
    @Size(max = 50)
    private String value;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    @Size(max = 10)
    @JsonIgnore
    private String action;
    @Size(max = 10)
    @JsonIgnore
    private String actionStatus;
    @Size(max = 10)
    private String valueType;

    public UfsSysConfigWrapper() {
    }

    public UfsSysConfigWrapper(BigDecimal id, String entity, String parameter, String value, String description, String action, String actionStatus, String valueType) {
        this.id = id;
        this.entity = entity;
        this.parameter = parameter;
        this.value = value;
        this.description = description;
        this.action = action;
        this.actionStatus = actionStatus;
        this.valueType = valueType;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    
}
