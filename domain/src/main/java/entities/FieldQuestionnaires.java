/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "FIELD_QUESTIONNAIRES")
@NamedQueries({
    @NamedQuery(name = "FieldQuestionnaires.findAll", query = "SELECT f FROM FieldQuestionnaires f")})
public class FieldQuestionnaires implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaireId")
    private Collection<FieldQuestionnairesMap> fieldQuestionnairesMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaireId")
    private Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaireId")
    private Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaireId")
    private Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection;

    public FieldQuestionnaires() {
    }

    public FieldQuestionnaires(BigDecimal id) {
        this.id = id;
    }

    public FieldQuestionnaires(BigDecimal id, String name) {
        this.id = id;
        this.name = name;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Collection<FieldQuestionnairesMap> getFieldQuestionnairesMapCollection() {
        return fieldQuestionnairesMapCollection;
    }

    public void setFieldQuestionnairesMapCollection(Collection<FieldQuestionnairesMap> fieldQuestionnairesMapCollection) {
        this.fieldQuestionnairesMapCollection = fieldQuestionnairesMapCollection;
    }

    public Collection<FieldQuestionsCustomers> getFieldQuestionsCustomersCollection() {
        return fieldQuestionsCustomersCollection;
    }

    public void setFieldQuestionsCustomersCollection(Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection) {
        this.fieldQuestionsCustomersCollection = fieldQuestionsCustomersCollection;
    }

    public Collection<FieldQuestionsSupervisor> getFieldQuestionsSupervisorCollection() {
        return fieldQuestionsSupervisorCollection;
    }

    public void setFieldQuestionsSupervisorCollection(Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection) {
        this.fieldQuestionsSupervisorCollection = fieldQuestionsSupervisorCollection;
    }

    public Collection<FieldQuestionsFeedback> getFieldQuestionsFeedbackCollection() {
        return fieldQuestionsFeedbackCollection;
    }

    public void setFieldQuestionsFeedbackCollection(Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection) {
        this.fieldQuestionsFeedbackCollection = fieldQuestionsFeedbackCollection;
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
        if (!(object instanceof FieldQuestionnaires)) {
            return false;
        }
        FieldQuestionnaires other = (FieldQuestionnaires) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldQuestionnaires[ id=" + id + " ]";
    }
    
}
