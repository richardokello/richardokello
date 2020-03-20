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
@Table(name = "DEVICE_CUSTOMER_DETAILS")
@NamedQueries({
    @NamedQuery(name = "DeviceCustomerDetails.findAll", query = "SELECT d FROM DeviceCustomerDetails d")})
public class DeviceCustomerDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DEVICE_CUSTOMER_ID")
    private BigDecimal deviceCustomerId;
    @Column(name = "AGENT_MERCHANT_ID")
    private String agentMerchantId;
    @Column(name = "FORM_VALUES")
    private String formValues;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "AGENT_NAME")
    private String agentName;

    public DeviceCustomerDetails() {
    }

    public DeviceCustomerDetails(BigDecimal deviceCustomerId) {
        this.deviceCustomerId = deviceCustomerId;
    }

    public BigDecimal getDeviceCustomerId() {
        return deviceCustomerId;
    }

    public void setDeviceCustomerId(BigDecimal deviceCustomerId) {
        this.deviceCustomerId = deviceCustomerId;
    }

    public String getAgentMerchantId() {
        return agentMerchantId;
    }

    public void setAgentMerchantId(String agentMerchantId) {
        this.agentMerchantId = agentMerchantId;
    }

    public String getFormValues() {
        return formValues;
    }

    public void setFormValues(String formValues) {
        this.formValues = formValues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceCustomerId != null ? deviceCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeviceCustomerDetails)) {
            return false;
        }
        DeviceCustomerDetails other = (DeviceCustomerDetails) object;
        if ((this.deviceCustomerId == null && other.deviceCustomerId != null) || (this.deviceCustomerId != null && !this.deviceCustomerId.equals(other.deviceCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.DeviceCustomerDetails[ deviceCustomerId=" + deviceCustomerId + " ]";
    }
    
}
