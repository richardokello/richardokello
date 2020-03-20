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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "UFS_GLS_BATCH")
@NamedQueries({
    @NamedQuery(name = "UfsGlsBatch.findAll", query = "SELECT u FROM UfsGlsBatch u")})
public class UfsGlsBatch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "BATCH_ID")
    private Long batchId;
    @Column(name = "TIME_UPLOADED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUploaded;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "PROCESSING_STATUS")
    private String processingStatus;
    @Column(name = "TIME_COMPLETED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCompleted;
    @Column(name = "MODEL_ID")
    private Long modelId;
    @JoinColumn(name = "UPLOADED_BY", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser uploadedBy;
    @OneToMany(mappedBy = "batchId")
    private Collection<UfsGls> ufsGlsCollection;

    public UfsGlsBatch() {
    }

    public UfsGlsBatch(Long batchId) {
        this.batchId = batchId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Date getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(Date timeUploaded) {
        this.timeUploaded = timeUploaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(Date timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public UfsUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UfsUser uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Collection<UfsGls> getUfsGlsCollection() {
        return ufsGlsCollection;
    }

    public void setUfsGlsCollection(Collection<UfsGls> ufsGlsCollection) {
        this.ufsGlsCollection = ufsGlsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (batchId != null ? batchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsGlsBatch)) {
            return false;
        }
        UfsGlsBatch other = (UfsGlsBatch) object;
        if ((this.batchId == null && other.batchId != null) || (this.batchId != null && !this.batchId.equals(other.batchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsGlsBatch[ batchId=" + batchId + " ]";
    }
    
}
