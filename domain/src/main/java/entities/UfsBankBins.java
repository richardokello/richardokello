/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_BANK_BINS")
@NamedQueries({
    @NamedQuery(name = "UfsBankBins.findAll", query = "SELECT u FROM UfsBankBins u")})
public class UfsBankBins implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "BIN_TYPE")
    private String binType;
    @Basic(optional = false)
    @Column(name = "VALUE")
    private String value;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBanks bankId;

    public UfsBankBins() {
    }

    public UfsBankBins(Long id) {
        this.id = id;
    }

    public UfsBankBins(Long id, String binType, String value) {
        this.id = id;
        this.binType = binType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
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
        if (!(object instanceof UfsBankBins)) {
            return false;
        }
        UfsBankBins other = (UfsBankBins) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsBankBins[ id=" + id + " ]";
    }
    
}
