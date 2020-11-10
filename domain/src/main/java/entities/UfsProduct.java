/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entities.TmsEstateHierachy;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */

@Entity
@Table(name = "UFS_PRODUCT")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsProduct.findAll", query = "SELECT u FROM UfsProduct u"),
        @NamedQuery(name = "UfsProduct.findByProductId", query = "SELECT u FROM UfsProduct u WHERE u.productId = :productId"),
        @NamedQuery(name = "UfsProduct.findByProductName", query = "SELECT u FROM UfsProduct u WHERE u.productName = :productName"),
        @NamedQuery(name = "UfsProduct.findByDescription", query = "SELECT u FROM UfsProduct u WHERE u.description = :description"),
        @NamedQuery(name = "UfsProduct.findByStatus", query = "SELECT u FROM UfsProduct u WHERE u.status = :status"),
        @NamedQuery(name = "UfsProduct.findByCreationDate", query = "SELECT u FROM UfsProduct u WHERE u.creationDate = :creationDate"),
        @NamedQuery(name = "UfsProduct.findByAction", query = "SELECT u FROM UfsProduct u WHERE u.action = :action"),
        @NamedQuery(name = "UfsProduct.findByActionStatus", query = "SELECT u FROM UfsProduct u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsProduct.findByIntrash", query = "SELECT u FROM UfsProduct u WHERE u.intrash = :intrash")})
public class UfsProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCT_ID")
    private BigDecimal productId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<TmsEstateHierachy> tmsEstateHierachyCollection;

    public UfsProduct() {
    }

    public UfsProduct(BigDecimal productId) {
        this.productId = productId;
    }

    public UfsProduct(BigDecimal productId, String productName, String description) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
    }

    public BigDecimal getProductId() {
        return productId;
    }

    public void setProductId(BigDecimal productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    @XmlTransient
    @JsonIgnore
    public Collection<TmsEstateHierachy> getTmsEstateHierachyCollection() {
        return tmsEstateHierachyCollection;
    }

    public void setTmsEstateHierachyCollection(Collection<TmsEstateHierachy> tmsEstateHierachyCollection) {
        this.tmsEstateHierachyCollection = tmsEstateHierachyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsProduct)) {
            return false;
        }
        UfsProduct other = (UfsProduct) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsProduct[ productId=" + productId + " ]";
    }

}

