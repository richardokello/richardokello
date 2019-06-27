/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.Filter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_GLS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsGls.findAll", query = "SELECT u FROM UfsGls u"),
    @NamedQuery(name = "UfsGls.findById", query = "SELECT u FROM UfsGls u WHERE u.id = :id"),
    @NamedQuery(name = "UfsGls.findByGlName", query = "SELECT u FROM UfsGls u WHERE u.glName = :glName"),
    @NamedQuery(name = "UfsGls.findByGlCode", query = "SELECT u FROM UfsGls u WHERE u.glCode = :glCode"),
    @NamedQuery(name = "UfsGls.findByGlAccountNumber", query = "SELECT u FROM UfsGls u WHERE u.glAccountNumber = :glAccountNumber"),
    @NamedQuery(name = "UfsGls.findByGlLocation", query = "SELECT u FROM UfsGls u WHERE u.glLocation = :glLocation"),
    @NamedQuery(name = "UfsGls.findByAction", query = "SELECT u FROM UfsGls u WHERE u.action = :action"),
    @NamedQuery(name = "UfsGls.findByActionStatus", query = "SELECT u FROM UfsGls u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsGls.findByCreatedAt", query = "SELECT u FROM UfsGls u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UfsGls.findByIntrash", query = "SELECT u FROM UfsGls u WHERE u.intrash = :intrash")})
public class UfsGls implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
            @GenericGenerator(
            name = "UFS_GLS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_GLS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_GLS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GL_NAME")
    private String glName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GL_CODE")
    private String glCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "GL_ACCOUNT_NUMBER")
    private String glAccountNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GL_LOCATION")
    private String glLocation;
    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Filter
    @Size(min = 1, max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBanks bankId;
    @Column(name = "BANK_ID")
    private BigDecimal bankIds;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @Column(name = "BANK_BRANCH_ID")
    private BigDecimal bankBranchIds;

    public UfsGls() {
    }

    public UfsGls(Long id) {
        this.id = id;
    }

    public UfsGls(Long id, String glName, String glCode, String glAccountNumber, String glLocation, String action, String actionStatus, Date createdAt, String intrash) {
        this.id = id;
        this.glName = glName;
        this.glCode = glCode;
        this.glAccountNumber = glAccountNumber;
        this.glLocation = glLocation;
        this.action = action;
        this.actionStatus = actionStatus;
        this.createdAt = createdAt;
        this.intrash = intrash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getGlAccountNumber() {
        return glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public String getGlLocation() {
        return glLocation;
    }

    public void setGlLocation(String glLocation) {
        this.glLocation = glLocation;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

    public UfsBankBranches getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(UfsBankBranches bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public BigDecimal getBankIds() {
        return bankIds;
    }

    public void setBankIds(BigDecimal bankIds) {
        this.bankIds = bankIds;
    }

    public BigDecimal getBankBranchIds() {
        return bankBranchIds;
    }

    public void setBankBranchIds(BigDecimal bankBranchIds) {
        this.bankBranchIds = bankBranchIds;
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
        if (!(object instanceof UfsGls)) {
            return false;
        }
        UfsGls other = (UfsGls) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsGls[ id=" + id + " ]";
    }
    
}
