/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "DEVICE_CUSTOMER_DETAILS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceCustomerDetails.findAll", query = "SELECT d FROM DeviceCustomerDetails d")
    , @NamedQuery(name = "DeviceCustomerDetails.findByDeviceCustomerId", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.deviceCustomerId = :deviceCustomerId")
    , @NamedQuery(name = "DeviceCustomerDetails.findByAgentMerchantId", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.agentMerchantId = :agentMerchantId")
    , @NamedQuery(name = "DeviceCustomerDetails.findByFormValues", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.formValues = :formValues")
    , @NamedQuery(name = "DeviceCustomerDetails.findByStatus", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.status = :status")
    , @NamedQuery(name = "DeviceCustomerDetails.findByAction", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.action = :action")
    , @NamedQuery(name = "DeviceCustomerDetails.findByActionStatus", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.actionStatus = :actionStatus")
    , @NamedQuery(name = "DeviceCustomerDetails.findByInstrash", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.instrash = :instrash")
    , @NamedQuery(name = "DeviceCustomerDetails.findByCreationDate", query = "SELECT d FROM DeviceCustomerDetails d WHERE d.creationDate = :creationDate")})
public class DeviceCustomerDetails implements Serializable {

    @Size(max = 50)
    @Column(name = "AGENT_NAME")
    @NotNull
    private String agentName;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id    
    @SequenceGenerator(name = "DEVICE_CUSTOMER_DETAILS_SEQ", sequenceName = "DEVICE_CUSTOMER_DETAILS_SEQ")
    @GeneratedValue(generator = "DEVICE_CUSTOMER_DETAILS_SEQ")
    @Basic(optional = false)
    @Column(name = "DEVICE_CUSTOMER_ID")
    private BigDecimal deviceCustomerId;
    @Size(max = 20)
    @Column(name = "AGENT_MERCHANT_ID")
    @NotNull
    private String agentMerchantId;
    @Size(max = 2000)
    @Column(name = "FORM_VALUES")
    @NotNull
    private String formValues;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 2)
    @Column(name = "INSTRASH")
    private String instrash;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

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

    public String getInstrash() {
        return instrash;
    }

    public void setInstrash(String instrash) {
        this.instrash = instrash;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        return "ke.co.tra.ufs.tms.entities.DeviceCustomerDetails[ deviceCustomerId=" + deviceCustomerId + " ]";
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

}
