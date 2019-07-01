/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
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
import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER_HISTORY", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerHistory.findAll", query = "SELECT u FROM UfsCustomerHistory u"),
    @NamedQuery(name = "UfsCustomerHistory.findById", query = "SELECT u FROM UfsCustomerHistory u WHERE u.id = :id"),
    @NamedQuery(name = "UfsCustomerHistory.findByActivity", query = "SELECT u FROM UfsCustomerHistory u WHERE u.activity = :activity"),
    @NamedQuery(name = "UfsCustomerHistory.findByNotes", query = "SELECT u FROM UfsCustomerHistory u WHERE u.notes = :notes"),
    @NamedQuery(name = "UfsCustomerHistory.findByAction", query = "SELECT u FROM UfsCustomerHistory u WHERE u.action = :action"),
    @NamedQuery(name = "UfsCustomerHistory.findByActionStatus", query = "SELECT u FROM UfsCustomerHistory u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsCustomerHistory.findByTimestamp", query = "SELECT u FROM UfsCustomerHistory u WHERE u.timestamp = :timestamp")})
public class UfsCustomerHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
            @GenericGenerator(
            name = "UFS_CUSTOMER_HISTORY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_HISTORY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_HISTORY_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ACTIVITY")
    private String activity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOTES")
    private String notes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "TIMESTAMP",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomer customerId;

    public UfsCustomerHistory() {
    }

    public UfsCustomerHistory(Long id) {
        this.id = id;
    }

    public UfsCustomerHistory(Long id, String activity, String notes, String action, Date timestamp) {
        this.id = id;
        this.activity = activity;
        this.notes = notes;
        this.action = action;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof UfsCustomerHistory)) {
            return false;
        }
        UfsCustomerHistory other = (UfsCustomerHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerHistory[ id=" + id + " ]";
    }
    
}
