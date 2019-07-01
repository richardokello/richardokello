/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_ASSIGNED_SIMDETAILS", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsAssignedSimdetails.findAll", query = "SELECT u FROM UfsAssignedSimdetails u"),
    @NamedQuery(name = "UfsAssignedSimdetails.findById", query = "SELECT u FROM UfsAssignedSimdetails u WHERE u.id = :id"),
    @NamedQuery(name = "UfsAssignedSimdetails.findByPhoneNumber", query = "SELECT u FROM UfsAssignedSimdetails u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "UfsAssignedSimdetails.findBySerialNumber", query = "SELECT u FROM UfsAssignedSimdetails u WHERE u.serialNumber = :serialNumber"),
    @NamedQuery(name = "UfsAssignedSimdetails.findBySimPin", query = "SELECT u FROM UfsAssignedSimdetails u WHERE u.simPin = :simPin")})
public class UfsAssignedSimdetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
        @GenericGenerator(
            name = "ASSIGNED_SIMDETAILS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ASSIGNED_SIMDETAILS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "ASSIGNED_SIMDETAILS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    @Size(max = 5)
    @Column(name = "SIM_PIN")
    private String simPin;
    @JoinColumn(name = "ASSIGNED_DEVICE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsAssignedDevice assignedDeviceId;
    
    @Column(name = "ASSIGNED_DEVICE_ID")
    private BigDecimal assignedDeviceIds;
    
    @JoinColumn(name = "SIM_PROVIDER", referencedColumnName = "MNO_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsMno simProvider;
    
    @Column(name = "SIM_PROVIDER")
    private BigDecimal simProviders;

    public UfsAssignedSimdetails() {
    }

    public UfsAssignedSimdetails(Long id) {
        this.id = id;
    }

    public UfsAssignedSimdetails(Long id, String phoneNumber, String serialNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.serialNumber = serialNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSimPin() {
        return simPin;
    }

    public void setSimPin(String simPin) {
        this.simPin = simPin;
    }

    public UfsAssignedDevice getAssignedDeviceId() {
        return assignedDeviceId;
    }

    public void setAssignedDeviceId(UfsAssignedDevice assignedDeviceId) {
        this.assignedDeviceId = assignedDeviceId;
    }

    public UfsMno getSimProvider() {
        return simProvider;
    }

    public void setSimProvider(UfsMno simProvider) {
        this.simProvider = simProvider;
    }

    public BigDecimal getAssignedDeviceIds() {
        return assignedDeviceIds;
    }

    public void setAssignedDeviceIds(BigDecimal assignedDeviceIds) {
        this.assignedDeviceIds = assignedDeviceIds;
    }

    public BigDecimal getSimProviders() {
        return simProviders;
    }

    public void setSimProviders(BigDecimal simProviders) {
        this.simProviders = simProviders;
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
        if (!(object instanceof UfsAssignedSimdetails)) {
            return false;
        }
        UfsAssignedSimdetails other = (UfsAssignedSimdetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsAssignedSimdetails[ id=" + id + " ]";
    }
    
}
