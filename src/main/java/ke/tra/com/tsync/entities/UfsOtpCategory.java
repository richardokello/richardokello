/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_OTP_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "UfsOtpCategory.findAll", query = "SELECT u FROM UfsOtpCategory u")})
public class UfsOtpCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "OTP_CATEGORY_ID")
    private Short otpCategoryId;
    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otpCategory")
    private Collection<UfsOtp> ufsOtpCollection;

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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Collection<UfsOtp> getUfsOtpCollection() {
        return ufsOtpCollection;
    }

    public void setUfsOtpCollection(Collection<UfsOtp> ufsOtpCollection) {
        this.ufsOtpCollection = ufsOtpCollection;
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
        return "com.mycompany.oracleufs.UfsOtpCategory[ otpCategoryId=" + otpCategoryId + " ]";
    }
    
}
