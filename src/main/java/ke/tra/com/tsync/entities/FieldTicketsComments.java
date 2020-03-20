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
@Table(name = "FIELD_TICKETS_COMMENTS")
@NamedQueries({
    @NamedQuery(name = "FieldTicketsComments.findAll", query = "SELECT f FROM FieldTicketsComments f")})
public class FieldTicketsComments implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "COMMENTS")
    private String comments;
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
    @ManyToOne
    private UfsUser supervisorId;
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FieldTickets ticketId;

    public FieldTicketsComments() {
    }

    public FieldTicketsComments(BigDecimal id) {
        this.id = id;
    }

    public FieldTicketsComments(BigDecimal id, String comments) {
        this.id = id;
        this.comments = comments;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public FieldTickets getTicketId() {
        return ticketId;
    }

    public void setTicketId(FieldTickets ticketId) {
        this.ticketId = ticketId;
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
        if (!(object instanceof FieldTicketsComments)) {
            return false;
        }
        FieldTicketsComments other = (FieldTicketsComments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.FieldTicketsComments[ id=" + id + " ]";
    }
    
}
