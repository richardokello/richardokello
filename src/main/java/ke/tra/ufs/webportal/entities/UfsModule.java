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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_MODULE", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsModule.findAll", query = "SELECT u FROM UfsModule u")
    , @NamedQuery(name = "UfsModule.findByModuleId", query = "SELECT u FROM UfsModule u WHERE u.moduleId = :moduleId")
    , @NamedQuery(name = "UfsModule.findByModuleName", query = "SELECT u FROM UfsModule u WHERE u.moduleName = :moduleName")
    , @NamedQuery(name = "UfsModule.findByDescription", query = "SELECT u FROM UfsModule u WHERE u.description = :description")
    , @NamedQuery(name = "UfsModule.findByAction", query = "SELECT u FROM UfsModule u WHERE u.action = :action")
    , @NamedQuery(name = "UfsModule.findByActionStatus", query = "SELECT u FROM UfsModule u WHERE u.actionStatus = :actionStatus")})
public class UfsModule implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MODULE_NAME")
    private String moduleName;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULE_ID")
    private Short moduleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module")
    private List<UfsEntity> ufsEntityList;

    public UfsModule() {
    }

    public UfsModule(Short moduleId) {
        this.moduleId = moduleId;
    }

    public UfsModule(Short moduleId, String moduleName) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public Short getModuleId() {
        return moduleId;
    }

    public void setModuleId(Short moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @XmlTransient
    public List<UfsEntity> getUfsEntityList() {
        return ufsEntityList;
    }

    public void setUfsEntityList(List<UfsEntity> ufsEntityList) {
        this.ufsEntityList = ufsEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moduleId != null ? moduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsModule)) {
            return false;
        }
        UfsModule other = (UfsModule) object;
        if ((this.moduleId == null && other.moduleId != null) || (this.moduleId != null && !this.moduleId.equals(other.moduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsModule[ moduleId=" + moduleId + " ]";
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
