/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_PARAM_DEFINITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsParamDefinition.findAll", query = "SELECT t FROM TmsParamDefinition t")
    , @NamedQuery(name = "TmsParamDefinition.findByParamDefId", query = "SELECT t FROM TmsParamDefinition t WHERE t.paramDefId = :paramDefId")
    , @NamedQuery(name = "TmsParamDefinition.findByParamType", query = "SELECT t FROM TmsParamDefinition t WHERE t.paramType = :paramType")
    , @NamedQuery(name = "TmsParamDefinition.findByParams", query = "SELECT t FROM TmsParamDefinition t WHERE t.params = :params")
    , @NamedQuery(name = "TmsParamDefinition.findByFileOutputName", query = "SELECT t FROM TmsParamDefinition t WHERE t.fileOutputName = :fileOutputName")
, @NamedQuery(name = "TmsParamDefinition.findByProductId", query = "SELECT t FROM TmsParamDefinition t WHERE t.productId = :productId")})
public class TmsParamDefinition implements Serializable {

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne
    private UfsProduct productId;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "TMS_PARAM_DEFINITION_SEQ", sequenceName = "TMS_PARAM_DEFINITION_SEQ")
    @GeneratedValue(generator = "TMS_PARAM_DEFINITION_SEQ")
    @Basic(optional = false)
    @Column(name = "PARAM_DEF_ID")//
    private BigDecimal paramDefId;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "PARAM_TYPE")
    private String paramType;
    @Basic(optional = false)
    @Size(min = 1, max = 4000)
    @Column(name = "PARAMS")
    private String params;
    @Size(max = 30)
    @Column(name = "FILE_OUTPUT_NAME")
    private String fileOutputName;

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
        return "ke.co.tra.ufs.tms.entities.TmsParamDefinition[ paramDefId=" + paramDefId + " ]";
    }

    public UfsProduct getProductId() {
        return productId;
    }

    public void setProductId(UfsProduct productId) {
        this.productId = productId;
    }
    
}
