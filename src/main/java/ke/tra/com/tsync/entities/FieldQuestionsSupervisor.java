/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "FIELD_QUESTIONS_SUPERVISOR")
@NamedQueries({
    @NamedQuery(name = "FieldQuestionsSupervisor.findAll", query = "SELECT f FROM FieldQuestionsSupervisor f")})
public class FieldQuestionsSupervisor implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "SUPERVISOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser supervisorId;
    @JoinColumn(name = "TARGET_REGIONS", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion targetRegions;
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches branchId;
    @JoinColumn(name = "QUESTIONNAIRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestionnaires questionnaireId;

    public FieldQuestionsSupervisor() {
    }

    public FieldQuestionsSupervisor(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public UfsUser getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(UfsUser supervisorId) {
        this.supervisorId = supervisorId;
    }

    public UfsBankRegion getTargetRegions() {
        return targetRegions;
    }

    public void setTargetRegions(UfsBankRegion targetRegions) {
        this.targetRegions = targetRegions;
    }

    public UfsBankBranches getBranchId() {
        return branchId;
    }

    public void setBranchId(UfsBankBranches branchId) {
        this.branchId = branchId;
    }

    public FieldQuestionnaires getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(FieldQuestionnaires questionnaireId) {
        this.questionnaireId = questionnaireId;
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
        if (!(object instanceof FieldQuestionsSupervisor)) {
            return false;
        }
        FieldQuestionsSupervisor other = (FieldQuestionsSupervisor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldQuestionsSupervisor[ id=" + id + " ]";
    }
    
}
