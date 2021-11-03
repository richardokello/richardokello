/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "TMS_DEVICE_FILE_EXT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsDeviceFileExt.findAll", query = "SELECT t FROM TmsDeviceFileExt t")
    , @NamedQuery(name = "TmsDeviceFileExt.findById", query = "SELECT t FROM TmsDeviceFileExt t WHERE t.id = :id")
    , @NamedQuery(name = "TmsDeviceFileExt.findByModelId", query = "SELECT t FROM TmsDeviceFileExt t WHERE t.modelId = :modelId")
    , @NamedQuery(name = "TmsDeviceFileExt.findByParamFileExt", query = "SELECT t FROM TmsDeviceFileExt t WHERE t.paramFileExt = :paramFileExt")
    , @NamedQuery(name = "TmsDeviceFileExt.findByAppFileExt", query = "SELECT t FROM TmsDeviceFileExt t WHERE t.appFileExt = :appFileExt")})
public class TmsDeviceFileExt implements Serializable {
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    public UfsDeviceModel model;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_FILE_EXT_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_FILE_EXT_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_DEVICE_FILE_EXT_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "MODEL_ID")
    @NotNull
    private BigDecimal modelId;
    @Size(max = 10)
    @Column(name = "PARAM_FILE_EXT")
    private String paramFileExt;
    @Size(max = 10)
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

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
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

    public UfsDeviceModel getModel() {
        return model;
    }

    public void setModel(UfsDeviceModel model) {
        this.model = model;
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
        return "ke.co.tra.ufs.tms.entities.TmsDeviceFileExt[ id=" + id + " ]";
    }
    
}
