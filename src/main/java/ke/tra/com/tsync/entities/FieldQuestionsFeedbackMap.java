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
@Table(name = "FIELD_QUESTIONS_FEEDBACK_MAP")
@NamedQueries({
    @NamedQuery(name = "FieldQuestionsFeedbackMap.findAll", query = "SELECT f FROM FieldQuestionsFeedbackMap f")})
public class FieldQuestionsFeedbackMap implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "ANSWERS")
    private String answers;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "FEEDBACK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestionsFeedback feedbackId;
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestions questionId;

    public FieldQuestionsFeedbackMap() {
    }

    public FieldQuestionsFeedbackMap(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
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

    public FieldQuestionsFeedback getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(FieldQuestionsFeedback feedbackId) {
        this.feedbackId = feedbackId;
    }

    public FieldQuestions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(FieldQuestions questionId) {
        this.questionId = questionId;
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
        if (!(object instanceof FieldQuestionsFeedbackMap)) {
            return false;
        }
        FieldQuestionsFeedbackMap other = (FieldQuestionsFeedbackMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldQuestionsFeedbackMap[ id=" + id + " ]";
    }
    
}
