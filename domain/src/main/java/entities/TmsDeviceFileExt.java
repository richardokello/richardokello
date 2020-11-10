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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_DEVICE_FILE_EXT")
@NamedQueries({
    @NamedQuery(name = "TmsDeviceFileExt.findAll", query = "SELECT t FROM TmsDeviceFileExt t")})
public class TmsDeviceFileExt implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "MODEL_ID")
    private BigInteger modelId;
    @Column(name = "PARAM_FILE_EXT")
    private String paramFileExt;
    @Column(name = "APP_FILE_EXT")
    private String appFileExt;

    public TmsDeviceFileExt() {
    }

    public TmsDeviceFileExt(BigDecimal id) {
        this.id = id;
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

    public String getParamFileExt() {
        return paramFileExt;
    }

    public void setParamFileExt(String paramFileExt) {
        this.paramFileExt = paramFileExt;
    }

    public String getAppFileExt() {
        return appFileExt;
    }

    public void setAppFileExt(String appFileExt) {
        this.appFileExt = appFileExt;
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
        if (!(object instanceof TmsDeviceFileExt)) {
            return false;
        }
        TmsDeviceFileExt other = (TmsDeviceFileExt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsDeviceFileExt[ id=" + id + " ]";
    }
    
}
