/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author owori.juma
 */
@Entity
@Table(name = "UFS_OTP")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsOtp.findAll", query = "SELECT u FROM UfsOtp u")
        , @NamedQuery(name = "UfsOtp.findByOtpId", query = "SELECT u FROM UfsOtp u WHERE u.otpId = :otpId")
        , @NamedQuery(name = "UfsOtp.findByOtp", query = "SELECT u FROM UfsOtp u WHERE u.otp = :otp")
        , @NamedQuery(name = "UfsOtp.findByCreationDate", query = "SELECT u FROM UfsOtp u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsOtp.findByUserId", query = "SELECT u FROM UfsOtp u WHERE u.userId = :userId")})
public class UfsOtp implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "OTP")
    private String otp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USER_ID")
    private String userId;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "UFS_OTP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "UFS_OTP_SEQ"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_OTP_SEQ")
    @Column(name = "OTP_ID")
    private BigDecimal otpId;
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "OTP_CATEGORY", referencedColumnName = "OTP_CATEGORY_ID")
    @ManyToOne(optional = false)
    private UfsOtpCategory otpCategory;
    @Column(name = "OTP_ATTEMPTS")
    private Short otpAttempts;

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

    public UfsOtp(String otp, UfsOtpCategory otpCategory, String userId) {
        super();
        this.otp = otp;
        this.otpCategory = otpCategory;
        this.userId = userId;
    }

    public BigDecimal getOtpId() {
        return otpId;
    }

    public void setOtpId(BigDecimal otpId) {
        this.otpId = otpId;
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

    public Short getOtpAttempts() {
        return otpAttempts;
    }

    public void setOtpAttempts(Short otpAttempts) {
        this.otpAttempts = otpAttempts;
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
        return "ke.tracom.ufs.entities.UfsOtp[ otpId=" + otpId + " ]";
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
