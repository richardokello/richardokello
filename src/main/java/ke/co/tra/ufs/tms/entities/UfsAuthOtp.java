/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_AUTH_OTP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsAuthOtp.findAll", query = "SELECT u FROM UfsAuthOtp u")
    , @NamedQuery(name = "UfsAuthOtp.findById", query = "SELECT u FROM UfsAuthOtp u WHERE u.id = :id")
    , @NamedQuery(name = "UfsAuthOtp.findByUserId", query = "SELECT u FROM UfsAuthOtp u WHERE u.userId = :userId")
    , @NamedQuery(name = "UfsAuthOtp.findByOtp", query = "SELECT u FROM UfsAuthOtp u WHERE u.otp = :otp")
    , @NamedQuery(name = "UfsAuthOtp.findByStatus", query = "SELECT u FROM UfsAuthOtp u WHERE u.status = :status")
    , @NamedQuery(name = "UfsAuthOtp.findByGenDate", query = "SELECT u FROM UfsAuthOtp u WHERE u.genDate = :genDate")})
public class UfsAuthOtp implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_OTP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "UFS_OTP_SEQ")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_OTP_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "USER_ID")
    private BigDecimal userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "OTP")
    private String otp;
    @Size(max = 20)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "GEN_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;

    public UfsAuthOtp() {
    }

    public UfsAuthOtp(BigDecimal id) {
        this.id = id;
    }

    public UfsAuthOtp(String otp, BigDecimal userId) {
        this.userId = userId;
        this.otp = otp;
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

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
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
        return "ke.co.tra.ufs.tms.entities.UfsAuthOtp[ id=" + id + " ]";
    }
    
}
