/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
@Table(name = "UFS_MODIFIED_RECORD")
@NamedQueries({
    @NamedQuery(name = "UfsModifiedRecord.findAll", query = "SELECT u FROM UfsModifiedRecord u")})
public class UfsModifiedRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "UFS_ENTITY")
    private String ufsEntity;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "VALUES")
    private String values;
    @Column(name = "EDITTED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date edittedOn;

    public UfsModifiedRecord() {
    }

    public UfsModifiedRecord(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUfsEntity() {
        return ufsEntity;
    }

    public void setUfsEntity(String ufsEntity) {
        this.ufsEntity = ufsEntity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Date getEdittedOn() {
        return edittedOn;
    }

    public void setEdittedOn(Date edittedOn) {
        this.edittedOn = edittedOn;
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
        if (!(object instanceof UfsModifiedRecord)) {
            return false;
        }
        UfsModifiedRecord other = (UfsModifiedRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsModifiedRecord[ id=" + id + " ]";
    }
    
}
