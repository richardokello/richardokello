/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

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
@Table(name = "UFS_OTP")
@NamedQueries({
    @NamedQuery(name = "UfsOtp.findAll", query = "SELECT u FROM UfsOtp u")})
public class UfsOtp implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "OTP_ID")
    private BigDecimal otpId;
    @Basic(optional = false)
    @Column(name = "OTP")
    private String otp;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private String userId;
    @JoinColumn(name = "OTP_CATEGORY", referencedColumnName = "OTP_CATEGORY_ID")
    @ManyToOne(optional = false)
    private UfsOtpCategory otpCategory;

    public UfsOtp() {
    }

    public UfsOtp(BigDecimal otpId) {
        this.otpId = otpId;
    }

    public UfsOtp(BigDecimal otpId, String otp, Date creationDate, String userId) {
        this.otpId = otpId;
        this.otp = otp;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public BigDecimal getOtpId() {
        return otpId;
    }

    public void setOtpId(BigDecimal otpId) {
        this.otpId = otpId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UfsOtpCategory getOtpCategory() {
        return otpCategory;
    }

    public void setOtpCategory(UfsOtpCategory otpCategory) {
        this.otpCategory = otpCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otpId != null ? otpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsOtp)) {
            return false;
        }
        UfsOtp other = (UfsOtp) object;
        if ((this.otpId == null && other.otpId != null) || (this.otpId != null && !this.otpId.equals(other.otpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsOtp[ otpId=" + otpId + " ]";
    }
    
}
