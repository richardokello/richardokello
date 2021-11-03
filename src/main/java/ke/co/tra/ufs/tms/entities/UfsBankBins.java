/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_BANK_BINS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsBankBins.findAll", query = "SELECT u FROM UfsBankBins u")
    , @NamedQuery(name = "UfsBankBins.findById", query = "SELECT u FROM UfsBankBins u WHERE u.id = :id")
    , @NamedQuery(name = "UfsBankBins.findByBankId", query = "SELECT u FROM UfsBankBins u WHERE u.bankId = :bankId")
    , @NamedQuery(name = "UfsBankBins.findByBinType", query = "SELECT u FROM UfsBankBins u WHERE u.binType = :binType")
    , @NamedQuery(name = "UfsBankBins.findByValue", query = "SELECT u FROM UfsBankBins u WHERE u.value = :value")
    , @NamedQuery(name = "UfsBankBins.findByIntrash", query = "SELECT u FROM UfsBankBins u WHERE u.intrash = :intrash")})
public class UfsBankBins implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "BIN_TYPE")
    private String binType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "VALUE")
    private String value;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
   
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private UfsBanks bankId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANK_BINS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANK_BINS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_BANK_BINS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BANK_ID")
    private long bankIds;

    public UfsBankBins() {
    }

    public UfsBankBins(Long id) {
        this.id = id;
    }

    public UfsBankBins(Long id, long bankIds, String binType, String value) {
        this.id = id;
        this.bankIds = bankIds;
        this.binType = binType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "ke.tra.ufs.tms.entities.UfsBankBins[ id=" + id + " ]";
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }


    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

    public long getBankIds() {
        return bankIds;
    }

    public void setBankIds(long bankIds) {
        this.bankIds = bankIds;
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

}
