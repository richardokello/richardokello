/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_SYS_CONFIG", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsSysConfig.findAll", query = "SELECT u FROM UfsSysConfig u")
    , @NamedQuery(name = "UfsSysConfig.findById", query = "SELECT u FROM UfsSysConfig u WHERE u.id = :id")
    , @NamedQuery(name = "UfsSysConfig.findByEntity", query = "SELECT u FROM UfsSysConfig u WHERE u.entity = :entity")
    , @NamedQuery(name = "UfsSysConfig.findByParameter", query = "SELECT u FROM UfsSysConfig u WHERE u.parameter = :parameter")
    , @NamedQuery(name = "UfsSysConfig.findByValue", query = "SELECT u FROM UfsSysConfig u WHERE u.value = :value")
    , @NamedQuery(name = "UfsSysConfig.findByValueType", query = "SELECT u FROM UfsSysConfig u WHERE u.valueType = :valueType")
    , @NamedQuery(name = "UfsSysConfig.findByDescription", query = "SELECT u FROM UfsSysConfig u WHERE u.description = :description")
    , @NamedQuery(name = "UfsSysConfig.findByAction", query = "SELECT u FROM UfsSysConfig u WHERE u.action = :action")
    , @NamedQuery(name = "UfsSysConfig.findByActionStatus", query = "SELECT u FROM UfsSysConfig u WHERE u.actionStatus = :actionStatus")})
public class UfsSysConfig implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ENTITY")
    private String entity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 50)
    @Column(name = "PARAMETER")
    private String parameter;
    @Size(max = 255)
    @Column(name = "VALUE")
    private String value;
    @Size(max = 20)
    @Column(name = "VALUE_TYPE")
    private String valueType;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;

    public UfsSysConfig() {
    }

    public UfsSysConfig(BigDecimal id) {
        this.id = id;
    }

    public UfsSysConfig(BigDecimal id, String entity, String parameter, String description) {
        this.id = id;
        this.entity = entity;
        this.parameter = parameter;
        this.description = description;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }


    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsSysConfig)) {
            return false;
        }
        UfsSysConfig other = (UfsSysConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsSysConfig[ id=" + id + " ]";
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
}
