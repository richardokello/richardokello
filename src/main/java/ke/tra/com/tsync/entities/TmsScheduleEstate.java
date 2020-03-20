/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_SCHEDULE_ESTATE")
@NamedQueries({
    @NamedQuery(name = "TmsScheduleEstate.findAll", query = "SELECT t FROM TmsScheduleEstate t")})
public class TmsScheduleEstate implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "SCHEDULE_ID")
    private BigInteger scheduleId;
    @Basic(optional = false)
    @Column(name = "UNIT_ITEM_ID")
    private BigInteger unitItemId;
    @Column(name = "INTRASH")
    private String intrash;

    public TmsScheduleEstate() {
    }

    public TmsScheduleEstate(BigDecimal id) {
        this.id = id;
    }

    public TmsScheduleEstate(BigDecimal id, BigInteger scheduleId, BigInteger unitItemId) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.unitItemId = unitItemId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigInteger scheduleId) {
        this.scheduleId = scheduleId;
    }

    public BigInteger getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(BigInteger unitItemId) {
        this.unitItemId = unitItemId;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
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
        if (!(object instanceof TmsScheduleEstate)) {
            return false;
        }
        TmsScheduleEstate other = (TmsScheduleEstate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsScheduleEstate[ id=" + id + " ]";
    }
    
}
