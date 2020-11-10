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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "FIELD_QUESTIONS")
@NamedQueries({
    @NamedQuery(name = "FieldQuestions.findAll", query = "SELECT f FROM FieldQuestions f")})
public class FieldQuestions implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "QUESTION")
    private String question;
    @Column(name = "ANSWERS")
    private String answers;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private Collection<FieldQuestionnairesMap> fieldQuestionnairesMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private Collection<FieldQuestionsFeedbackMap> fieldQuestionsFeedbackMapCollection;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestionTypes typeId;
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestionCategories categoryId;

    public FieldQuestions() {
    }

    public FieldQuestions(BigDecimal id) {
        this.id = id;
    }

    public FieldQuestions(BigDecimal id, String question) {
        this.id = id;
        this.question = question;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
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

    public Collection<FieldQuestionsFeedbackMap> getFieldQuestionsFeedbackMapCollection() {
        return fieldQuestionsFeedbackMapCollection;
    }

    public void setFieldQuestionsFeedbackMapCollection(Collection<FieldQuestionsFeedbackMap> fieldQuestionsFeedbackMapCollection) {
        this.fieldQuestionsFeedbackMapCollection = fieldQuestionsFeedbackMapCollection;
    }

    public FieldQuestionTypes getTypeId() {
        return typeId;
    }

    public void setTypeId(FieldQuestionTypes typeId) {
        this.typeId = typeId;
    }

    public FieldQuestionCategories getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(FieldQuestionCategories categoryId) {
        this.categoryId = categoryId;
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
        if (!(object instanceof FieldQuestions)) {
            return false;
        }
        FieldQuestions other = (FieldQuestions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldQuestions[ id=" + id + " ]";
    }
    
}
