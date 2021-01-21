/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_MNO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsMno.findAll", query = "SELECT u FROM UfsMno u")
    , @NamedQuery(name = "UfsMno.findByMnoId", query = "SELECT u FROM UfsMno u WHERE u.mnoId = :mnoId")
    , @NamedQuery(name = "UfsMno.findByMnoName", query = "SELECT u FROM UfsMno u WHERE u.mnoName = :mnoName")
    , @NamedQuery(name = "UfsMno.findByDescription", query = "SELECT u FROM UfsMno u WHERE u.description = :description")
    , @NamedQuery(name = "UfsMno.findByAction", query = "SELECT u FROM UfsMno u WHERE u.action = :action")
    , @NamedQuery(name = "UfsMno.findByActionStatus", query = "SELECT u FROM UfsMno u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsMno.findByIntrash", query = "SELECT u FROM UfsMno u WHERE u.intrash = :intrash")})
public class UfsMno implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "UFS_MNO_SEQ", sequenceName = "UFS_MNO_SEQ")
    @GeneratedValue(generator = "UFS_MNO_SEQ")
    @Basic(optional = false)
    @Column(name = "MNO_ID")//UFS_MNO_SEQ
    private BigDecimal mnoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MNO_NAME")
    @Searchable
    @Filter
    @ModifiableField
    private String mnoName;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Filter
    @Searchable
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Filter
    @Column(name = "INTRASH")
    private String intrash;

    public UfsMno() {
    }

    public UfsMno(BigDecimal mnoId) {
        this.mnoId = mnoId;
    }

    public UfsMno(BigDecimal mnoId, String mnoName, String description) {
        this.mnoId = mnoId;
        this.mnoName = mnoName;
        this.description = description;
    }

    public BigDecimal getMnoId() {
        return mnoId;
    }

    public void setMnoId(BigDecimal mnoId) {
        this.mnoId = mnoId;
    }

    public String getMnoName() {
        return mnoName;
    }

    public void setMnoName(String mnoName) {
        this.mnoName = mnoName;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mnoId != null ? mnoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsMno)) {
            return false;
        }
        UfsMno other = (UfsMno) object;
        if ((this.mnoId == null && other.mnoId != null) || (this.mnoId != null && !this.mnoId.equals(other.mnoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsMno[ mnoId=" + mnoId + " ]";
    }
    
}
