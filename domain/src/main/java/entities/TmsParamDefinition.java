/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_PARAM_DEFINITION")
@NamedQueries({
    @NamedQuery(name = "TmsParamDefinition.findAll", query = "SELECT t FROM TmsParamDefinition t")})
public class TmsParamDefinition implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PARAM_DEF_ID")
    private BigDecimal paramDefId;
    @Basic(optional = false)
    @Column(name = "PARAM_TYPE")
    private String paramType;
    @Basic(optional = false)
    @Column(name = "PARAMS")
    private String params;
    @Column(name = "FILE_OUTPUT_NAME")
    private String fileOutputName;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne
    private UfsProduct productId;

    public TmsParamDefinition() {
    }

    public TmsParamDefinition(BigDecimal paramDefId) {
        this.paramDefId = paramDefId;
    }

    public TmsParamDefinition(BigDecimal paramDefId, String paramType, String params) {
        this.paramDefId = paramDefId;
        this.paramType = paramType;
        this.params = params;
    }

    public BigDecimal getParamDefId() {
        return paramDefId;
    }

    public void setParamDefId(BigDecimal paramDefId) {
        this.paramDefId = paramDefId;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getFileOutputName() {
        return fileOutputName;
    }

    public void setFileOutputName(String fileOutputName) {
        this.fileOutputName = fileOutputName;
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
        hash += (paramDefId != null ? paramDefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsParamDefinition)) {
            return false;
        }
        TmsParamDefinition other = (TmsParamDefinition) object;
        if ((this.paramDefId == null && other.paramDefId != null) || (this.paramDefId != null && !this.paramDefId.equals(other.paramDefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsParamDefinition[ paramDefId=" + paramDefId + " ]";
    }
    
}
