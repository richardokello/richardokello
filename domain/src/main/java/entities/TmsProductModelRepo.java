/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_PRODUCT_MODEL_REPO")
@NamedQueries({
    @NamedQuery(name = "TmsProductModelRepo.findAll", query = "SELECT t FROM TmsProductModelRepo t")})
public class TmsProductModelRepo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "MODEL_ID")
    private BigInteger modelId;
    @Basic(optional = false)
    @Column(name = "REPO_PATH")
    private String repoPath;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false)
    private UfsProduct productId;

    public TmsProductModelRepo() {
    }

    public TmsProductModelRepo(BigDecimal id) {
        this.id = id;
    }

    public TmsProductModelRepo(BigDecimal id, BigInteger modelId, String repoPath) {
        this.id = id;
        this.modelId = modelId;
        this.repoPath = repoPath;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getModelId() {
        return modelId;
    }

    public void setModelId(BigInteger modelId) {
        this.modelId = modelId;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public UfsProduct getProductId() {
        return productId;
    }

    public void setProductId(UfsProduct productId) {
        this.productId = productId;
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
        if (!(object instanceof TmsProductModelRepo)) {
            return false;
        }
        TmsProductModelRepo other = (TmsProductModelRepo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsProductModelRepo[ id=" + id + " ]";
    }
    
}
