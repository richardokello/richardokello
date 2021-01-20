/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_PRODUCT_MODEL_REPO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsProductModelRepo.findAll", query = "SELECT t FROM TmsProductModelRepo t")
    , @NamedQuery(name = "TmsProductModelRepo.findById", query = "SELECT t FROM TmsProductModelRepo t WHERE t.id = :id")
    , @NamedQuery(name = "TmsProductModelRepo.findByRepoPath", query = "SELECT t FROM TmsProductModelRepo t WHERE t.repoPath = :repoPath")})
public class TmsProductModelRepo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "REPO_PATH")
    private String repoPath;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false)
    private UfsProduct productId;

    public TmsProductModelRepo() {
    }

    public TmsProductModelRepo(BigDecimal id) {
        this.id = id;
    }

    public TmsProductModelRepo(BigDecimal id, String repoPath) {
        this.id = id;
        this.repoPath = repoPath;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
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
        return "ke.co.tra.ufs.tms.entities.TmsProductModelRepo[ id=" + id + " ]";
    }
    
}
