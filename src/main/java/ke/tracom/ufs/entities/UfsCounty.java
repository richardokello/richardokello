/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.ChasisUUID;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "UFS_COUNTY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbmCounty.findAll", query = "SELECT s FROM UfsCounty s")
    , @NamedQuery(name = "SbmCounty.findById", query = "SELECT s FROM UfsCounty s WHERE s.uuid = :uuid")
    , @NamedQuery(name = "SbmCounty.findByName", query = "SELECT s FROM UfsCounty s WHERE s.name = :name")
    , @NamedQuery(name = "SbmCounty.findByCountyCode", query = "SELECT s FROM UfsCounty s WHERE s.countyCode = :countyCode")
    , @NamedQuery(name = "SbmCounty.findByCreationDate", query = "SELECT s FROM UfsCounty s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "SbmCounty.findByAction", query = "SELECT s FROM UfsCounty s WHERE s.action = :action")
    , @NamedQuery(name = "SbmCounty.findByActionStatus", query = "SELECT s FROM UfsCounty s WHERE s.actionStatus = :actionStatus")
    , @NamedQuery(name = "SbmCounty.findByIntrash", query = "SELECT s FROM UfsCounty s WHERE s.intrash = :intrash")})
public class UfsCounty implements Serializable {

    @Size(max = 50)
    @ModifiableField
    @Column(name = "NAME")
    private String name;
    @Size(max = 4)
    @ModifiableField
    @Column(name = "COUNTY_CODE")
    private String countyCode;
    @Size(max = 10)
    @ModifiableField
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Size(max = 10)
    @ModifiableField
    @Filter
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Size(max = 5)
    @ModifiableField
    @Column(name = "INTRASH",insertable = false)
    private String intrash;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @ChasisUUID
    @Basic(optional = false)
    @Column(name = "U_UID")
    private String uuid;
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;


    public UfsCounty() {
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsCounty)) {
            return false;
        }
        UfsCounty other = (UfsCounty) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.sbm.revenue_collection.entities.SbmCounty[ id=" + uuid + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
