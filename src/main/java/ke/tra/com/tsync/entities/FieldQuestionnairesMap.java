/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "FIELD_QUESTIONNAIRES_MAP")
@NamedQueries({
    @NamedQuery(name = "FieldQuestionnairesMap.findAll", query = "SELECT f FROM FieldQuestionnairesMap f")})
public class FieldQuestionnairesMap implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestions questionId;
    @JoinColumn(name = "QUESTIONNAIRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldQuestionnaires questionnaireId;

    public FieldQuestionnairesMap() {
    }

    public FieldQuestionnairesMap(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public FieldQuestions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(FieldQuestions questionId) {
        this.questionId = questionId;
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
        if (!(object instanceof FieldQuestionnairesMap)) {
            return false;
        }
        FieldQuestionnairesMap other = (FieldQuestionnairesMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldQuestionnairesMap[ id=" + id + " ]";
    }
    
}
