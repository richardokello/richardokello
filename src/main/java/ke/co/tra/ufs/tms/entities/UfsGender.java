/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_GENDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsGender.findAll", query = "SELECT u FROM UfsGender u")
    , @NamedQuery(name = "UfsGender.findByGenderId", query = "SELECT u FROM UfsGender u WHERE u.genderId = :genderId")
    , @NamedQuery(name = "UfsGender.findByGender", query = "SELECT u FROM UfsGender u WHERE u.gender = :gender")
    , @NamedQuery(name = "UfsGender.findByDescription", query = "SELECT u FROM UfsGender u WHERE u.description = :description")
    , @NamedQuery(name = "UfsGender.findByAction", query = "SELECT u FROM UfsGender u WHERE u.action = :action")
    , @NamedQuery(name = "UfsGender.findByActionStatus", query = "SELECT u FROM UfsGender u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsGender.findByCreationDate", query = "SELECT u FROM UfsGender u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsGender.findByIntrash", query = "SELECT u FROM UfsGender u WHERE u.intrash = :intrash")})
public class UfsGender implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GENDER")
    private String gender;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_GENDER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_GENDER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_GENDER_SEQ")
    @Column(name = "GENDER_ID")
    private BigDecimal genderId;


    @OneToMany(mappedBy = "gender")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsUser> ufsUserList;

    public UfsGender() {
    }

    public UfsGender(BigDecimal genderId) {
        this.genderId = genderId;
    }

    public UfsGender(BigDecimal genderId, String gender, Date creationDate) {
        this.genderId = genderId;
        this.gender = gender;
        this.creationDate = creationDate;
    }

    public BigDecimal getGenderId() {
        return genderId;
    }

    public void setGenderId(BigDecimal genderId) {
        this.genderId = genderId;
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


    @XmlTransient
    public List<UfsUser> getUfsUserList() {
        return ufsUserList;
    }

    public void setUfsUserList(List<UfsUser> ufsUserList) {
        this.ufsUserList = ufsUserList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genderId != null ? genderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsGender)) {
            return false;
        }
        UfsGender other = (UfsGender) object;
        if ((this.genderId == null && other.genderId != null) || (this.genderId != null && !this.genderId.equals(other.genderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsGender[ genderId=" + genderId + " ]";
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
    
}
