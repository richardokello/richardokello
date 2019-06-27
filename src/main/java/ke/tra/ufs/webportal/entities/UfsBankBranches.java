/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.Filter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author ojuma
 */
@Entity
@Table(name = "UFS_BANK_BRANCHES", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsBankBranches.findAll", query = "SELECT u FROM UfsBankBranches u")
        , @NamedQuery(name = "UfsBankBranches.findById", query = "SELECT u FROM UfsBankBranches u WHERE u.id = :id")
        , @NamedQuery(name = "UfsBankBranches.findByName", query = "SELECT u FROM UfsBankBranches u WHERE u.name = :name")
        , @NamedQuery(name = "UfsBankBranches.findByCode", query = "SELECT u FROM UfsBankBranches u WHERE u.code = :code")
        , @NamedQuery(name = "UfsBankBranches.findByAction", query = "SELECT u FROM UfsBankBranches u WHERE u.action = :action")
        , @NamedQuery(name = "UfsBankBranches.findByActionStatus", query = "SELECT u FROM UfsBankBranches u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsBankBranches.findByCreatedAt", query = "SELECT u FROM UfsBankBranches u WHERE u.createdAt = :createdAt")
        , @NamedQuery(name = "UfsBankBranches.findByIntrash", query = "SELECT u FROM UfsBankBranches u WHERE u.intrash = :intrash")})
public class UfsBankBranches implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANK_BRANCHES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANK_BRANCHES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_BANK_BRANCHES_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private UfsBanks bankId;
    @Column(name = "BANK_ID")
    private Long bankIds;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private UfsBankRegion bankRegionId;
    @Column(name = "BANK_REGION_ID" )
    private BigDecimal bankRegionIds;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private UfsGeographicalRegion geographicalRegionId;
    @Column(name = "GEOGRAPHICAL_REGION_ID" )
    private BigDecimal geographicalRegionIds;

    public UfsBankBranches() {
    }

    public UfsBankBranches(Long id) {
        this.id = id;
    }

    public UfsBankBranches(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    public Long getBankIds() {
        return bankIds;
    }

    public void setBankIds(Long bankIds) {
        this.bankIds = bankIds;
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
        if (!(object instanceof UfsBankBranches)) {
            return false;
        }
        UfsBankBranches other = (UfsBankBranches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsBankBranches[ id=" + id + " ]";
    }


    public BigDecimal getBankRegionIds() {
        return bankRegionIds;
    }

    public void setBankRegionIds(BigDecimal bankRegionIds) {
        this.bankRegionIds = bankRegionIds;
    }

    public BigDecimal getGeographicalRegionIds() {
        return geographicalRegionIds;
    }

    public void setGeographicalRegionIds(BigDecimal geographicalRegionIds) {
        this.geographicalRegionIds = geographicalRegionIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
