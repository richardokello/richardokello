/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_PRODUCT")
@NamedQueries({
    @NamedQuery(name = "UfsProduct.findAll", query = "SELECT u FROM UfsProduct u")})
public class UfsProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private BigDecimal productId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<TmsApp> tmsAppCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<TmsProductModelRepo> tmsProductModelRepoCollection;
    @OneToMany(mappedBy = "productId")
    private Collection<TmsParamDefinition> tmsParamDefinitionCollection;

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

    public Collection<TmsApp> getTmsAppCollection() {
        return tmsAppCollection;
    }

    public void setTmsAppCollection(Collection<TmsApp> tmsAppCollection) {
        this.tmsAppCollection = tmsAppCollection;
    }

    public Collection<TmsProductModelRepo> getTmsProductModelRepoCollection() {
        return tmsProductModelRepoCollection;
    }

    public void setTmsProductModelRepoCollection(Collection<TmsProductModelRepo> tmsProductModelRepoCollection) {
        this.tmsProductModelRepoCollection = tmsProductModelRepoCollection;
    }

    public Collection<TmsParamDefinition> getTmsParamDefinitionCollection() {
        return tmsParamDefinitionCollection;
    }

    public void setTmsParamDefinitionCollection(Collection<TmsParamDefinition> tmsParamDefinitionCollection) {
        this.tmsParamDefinitionCollection = tmsParamDefinitionCollection;
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
        return "com.mycompany.oracleufs.UfsProduct[ productId=" + productId + " ]";
    }
    
}
