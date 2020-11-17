/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
@Table(name = "TMS_WHITELIST_BATCH")
@NamedQueries({
    @NamedQuery(name = "TmsWhitelistBatch.findAll", query = "SELECT t FROM TmsWhitelistBatch t")})
public class TmsWhitelistBatch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "BATCH_ID")
    private Long batchId;
    @Column(name = "TIME_UPLOADED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUploaded;
    @Column(name = "MODEL_ID")
    private Long modelId;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "PROCESSING_STATUS")
    private String processingStatus;
    @Column(name = "TIME_COMPLETED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCompleted;
    @OneToMany(mappedBy = "batchId")
    private Collection<TmsWhitelist> tmsWhitelistCollection;
    @JoinColumn(name = "UPLOADED_BY", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser uploadedBy;

    public TmsWhitelistBatch() {
    }

    public TmsWhitelistBatch(Long batchId) {
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

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

    public Collection<TmsWhitelist> getTmsWhitelistCollection() {
        return tmsWhitelistCollection;
    }

    public void setTmsWhitelistCollection(Collection<TmsWhitelist> tmsWhitelistCollection) {
        this.tmsWhitelistCollection = tmsWhitelistCollection;
    }

    public UfsUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UfsUser uploadedBy) {
        this.uploadedBy = uploadedBy;
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
        if (!(object instanceof TmsWhitelistBatch)) {
            return false;
        }
        TmsWhitelistBatch other = (TmsWhitelistBatch) object;
        if ((this.batchId == null && other.batchId != null) || (this.batchId != null && !this.batchId.equals(other.batchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsWhitelistBatch[ batchId=" + batchId + " ]";
    }
    
}
