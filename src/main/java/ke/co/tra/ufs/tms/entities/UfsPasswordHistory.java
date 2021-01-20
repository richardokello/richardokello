/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_PASSWORD_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsPasswordHistory.findAll", query = "SELECT u FROM UfsPasswordHistory u")
    , @NamedQuery(name = "UfsPasswordHistory.findById", query = "SELECT u FROM UfsPasswordHistory u WHERE u.id = :id")
    , @NamedQuery(name = "UfsPasswordHistory.findByUserId", query = "SELECT u FROM UfsPasswordHistory u WHERE u.userId = :userId")
    , @NamedQuery(name = "UfsPasswordHistory.findByPassword", query = "SELECT u FROM UfsPasswordHistory u WHERE u.password = :password")
    , @NamedQuery(name = "UfsPasswordHistory.findByChangeDate", query = "SELECT u FROM UfsPasswordHistory u WHERE u.changeDate = :changeDate")})
public class UfsPasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "USER_ID")
    private BigInteger userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;

    public UfsPasswordHistory() {
    }

    public UfsPasswordHistory(BigDecimal id) {
        this.id = id;
    }

    public UfsPasswordHistory(BigDecimal id, String password) {
        this.id = id;
        this.password = password;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
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
        if (!(object instanceof UfsPasswordHistory)) {
            return false;
        }
        UfsPasswordHistory other = (UfsPasswordHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsPasswordHistory[ id=" + id + " ]";
    }
    
}
