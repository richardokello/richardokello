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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "UFS_AUTH_OTP")
@NamedQueries({
    @NamedQuery(name = "UfsAuthOtp.findAll", query = "SELECT u FROM UfsAuthOtp u")})
public class UfsAuthOtp implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "OTP")
    private String otp;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser userId;

    public UfsAuthOtp() {
    }

    public UfsAuthOtp(BigDecimal id) {
        this.id = id;
    }

    public UfsAuthOtp(BigDecimal id, String otp) {
        this.id = id;
        this.otp = otp;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
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
        if (!(object instanceof UfsAuthOtp)) {
            return false;
        }
        UfsAuthOtp other = (UfsAuthOtp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsAuthOtp[ id=" + id + " ]";
    }
    
}
