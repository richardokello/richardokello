/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_SCHEDULE_ESTATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsScheduleEstate.findAll", query = "SELECT t FROM TmsScheduleEstate t")
    , @NamedQuery(name = "TmsScheduleEstate.findById", query = "SELECT t FROM TmsScheduleEstate t WHERE t.id = :id")
    , @NamedQuery(name = "TmsScheduleEstate.findByIntrash", query = "SELECT t FROM TmsScheduleEstate t WHERE t.intrash = :intrash")})
public class TmsScheduleEstate implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "TMS_SCHEDULE_ESTATE_SEQ", sequenceName = "TMS_SCHEDULE_ESTATE_SEQ")
    @GeneratedValue(generator = "TMS_SCHEDULE_ESTATE_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "UNIT_ITEM_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne(optional = false)
    private TmsEstateItem unitItemId;
    @JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
    @ManyToOne(optional = false)
    private TmsScheduler scheduleId;

    public TmsScheduleEstate() {
    }

    public TmsScheduleEstate(BigDecimal id) {
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

    public TmsEstateItem getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(TmsEstateItem unitItemId) {
        this.unitItemId = unitItemId;
    }

    public TmsScheduler getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(TmsScheduler scheduleId) {
        this.scheduleId = scheduleId;
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
        return "ke.co.tra.ufs.tms.entities.TmsScheduleEstate[ id=" + id + " ]";
    }

}
