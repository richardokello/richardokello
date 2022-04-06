/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_PARAM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceParam.findAll", query = "SELECT t FROM TmsDeviceParam t")
    , @NamedQuery(name = "TmsDeviceParam.findByParamId", query = "SELECT t FROM TmsDeviceParam t WHERE t.paramId = :paramId")
    , @NamedQuery(name = "TmsDeviceParam.findByValues", query = "SELECT t FROM TmsDeviceParam t WHERE t.values = :values")})
public class TmsDeviceParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_PARAM_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_PARAM_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_DEVICE_PARAM_SEQ")
    @Basic(optional = false)//
    @Column(name = "PARAM_ID")
    private BigDecimal paramId;
    @Size(max = 4000)
    @Column(name = "\"VALUE\"")
    private String values;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne
    @JsonIgnore
    private TmsDevice deviceId;
    @JoinColumn(name = "DEVICE_CUSTOMER_ID", referencedColumnName = "DEVICE_CUSTOMER_ID")
    @ManyToOne
    private DeviceCustomerDetails deviceCustomerId;

    public TmsDeviceParam() {
    }

    public TmsDeviceParam(BigDecimal paramId) {
        this.paramId = paramId;
    }

    public BigDecimal getParamId() {
        return paramId;
    }

    public void setParamId(BigDecimal paramId) {
        this.paramId = paramId;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceCustomerDetails getDeviceCustomerId() {
        return deviceCustomerId;
    }

    public void setDeviceCustomerId(DeviceCustomerDetails deviceCustomerId) {
        this.deviceCustomerId = deviceCustomerId;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramId != null ? paramId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsDeviceParam)) {
            return false;
        }
        TmsDeviceParam other = (TmsDeviceParam) object;
        if ((this.paramId == null && other.paramId != null) || (this.paramId != null && !this.paramId.equals(other.paramId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceParam[ paramId=" + paramId + " ]";
    }

}
