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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_DEVICE_CURRENCY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceCurrency.findAll", query = "SELECT t FROM TmsDeviceCurrency t")
    , @NamedQuery(name = "TmsDeviceCurrency.findById", query = "SELECT t FROM TmsDeviceCurrency t WHERE t.id = :id")
    , @NamedQuery(name = "TmsDeviceCurrency.findByDeviceId", query = "SELECT t FROM TmsDeviceCurrency t WHERE t.deviceId = :deviceId")
    , @NamedQuery(name = "TmsDeviceCurrency.findByCurrencyId", query = "SELECT t FROM TmsDeviceCurrency t WHERE t.currencyId = :currencyId")})

public class TmsDeviceCurrency implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_CURRENCY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_CURRENCY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_DEVICE_CURRENCY_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID")
    @ManyToOne(optional = false)
    @JsonIgnore
    private TmsDevice deviceId;
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCurrency currencyId;

    @Transient
    private BigDecimal deviceIds;

    @Transient
    private BigDecimal currencyIds;

    public TmsDeviceCurrency() {
    }

    public TmsDeviceCurrency(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public UfsCurrency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(UfsCurrency currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(BigDecimal deviceIds) {
        this.deviceIds = deviceIds;
    }

    public BigDecimal getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(BigDecimal currencyIds) {
        this.currencyIds = currencyIds;
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
        if (!(object instanceof TmsDeviceCurrency)) {
            return false;
        }
        TmsDeviceCurrency other = (TmsDeviceCurrency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsDeviceCurrency[ id=" + id + " ]";
    }

}
