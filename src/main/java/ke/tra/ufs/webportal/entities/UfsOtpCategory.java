/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_OTP_CATEGORY", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsOtpCategory.findAll", query = "SELECT u FROM UfsOtpCategory u")
        , @NamedQuery(name = "UfsOtpCategory.findByOtpCategoryId", query = "SELECT u FROM UfsOtpCategory u WHERE u.otpCategoryId = :otpCategoryId")
        , @NamedQuery(name = "UfsOtpCategory.findByCategory", query = "SELECT u FROM UfsOtpCategory u WHERE u.category = :category")
        , @NamedQuery(name = "UfsOtpCategory.findByDescription", query = "SELECT u FROM UfsOtpCategory u WHERE u.description = :description")
        , @NamedQuery(name = "UfsOtpCategory.findByAction", query = "SELECT u FROM UfsOtpCategory u WHERE u.action = :action")
        , @NamedQuery(name = "UfsOtpCategory.findByActionStatus", query = "SELECT u FROM UfsOtpCategory u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsOtpCategory.findByIntrash", query = "SELECT u FROM UfsOtpCategory u WHERE u.intrash = :intrash")
        , @NamedQuery(name = "UfsOtpCategory.findByCreationDate", query = "SELECT u FROM UfsOtpCategory u WHERE u.creationDate = :creationDate")})
public class UfsOtpCategory implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /**
     * Initialize authentication category
     */
    @Transient
    public static UfsOtpCategory AUTH_OTP = new UfsOtpCategory((short) 1);

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "OTP_CATEGORY_ID")
    private Short otpCategoryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otpCategory")
    private List<UfsOtp> ufsOtpList;

    public UfsOtpCategory() {
    }

    public UfsOtpCategory(Short otpCategoryId) {
        this.otpCategoryId = otpCategoryId;
    }

    public UfsOtpCategory(Short otpCategoryId, String category, Date creationDate) {
        this.otpCategoryId = otpCategoryId;
        this.category = category;
        this.creationDate = creationDate;
    }

    public Short getOtpCategoryId() {
        return otpCategoryId;
    }

    public void setOtpCategoryId(Short otpCategoryId) {
        this.otpCategoryId = otpCategoryId;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<UfsOtp> getUfsOtpList() {
        return ufsOtpList;
    }

    public void setUfsOtpList(List<UfsOtp> ufsOtpList) {
        this.ufsOtpList = ufsOtpList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otpCategoryId != null ? otpCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsOtpCategory)) {
            return false;
        }
        UfsOtpCategory other = (UfsOtpCategory) object;
        if ((this.otpCategoryId == null && other.otpCategoryId != null) || (this.otpCategoryId != null && !this.otpCategoryId.equals(other.otpCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsOtpCategory[ otpCategoryId=" + otpCategoryId + " ]";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
}
