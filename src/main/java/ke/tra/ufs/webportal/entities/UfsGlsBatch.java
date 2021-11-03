/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "UFS_GLS_BATCH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsGlsBatch.findAll", query = "SELECT u FROM UfsGlsBatch u")
    , @NamedQuery(name = "UfsGlsBatch.findByBatchId", query = "SELECT u FROM UfsGlsBatch u WHERE u.batchId = :batchId")
    , @NamedQuery(name = "UfsGlsBatch.findByTimeUploaded", query = "SELECT u FROM UfsGlsBatch u WHERE u.timeUploaded = :timeUploaded")
    , @NamedQuery(name = "UfsGlsBatch.findByModelId", query = "SELECT u FROM UfsGlsBatch u WHERE u.modelId = :modelId")
    , @NamedQuery(name = "UfsGlsBatch.findByFileName", query = "SELECT u FROM UfsGlsBatch u WHERE u.fileName = :fileName")
    , @NamedQuery(name = "UfsGlsBatch.findByFilePath", query = "SELECT u FROM UfsGlsBatch u WHERE u.filePath = :filePath")
    , @NamedQuery(name = "UfsGlsBatch.findByProcessingStatus", query = "SELECT u FROM UfsGlsBatch u WHERE u.processingStatus = :processingStatus")
    , @NamedQuery(name = "UfsGlsBatch.findByTimeCompleted", query = "SELECT u FROM UfsGlsBatch u WHERE u.timeCompleted = :timeCompleted")})
public class UfsGlsBatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GenericGenerator(
            name = "UFS_GLS_BATCH_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "UFS_GLS_BATCH_SEQ")
                    ,
                    @Parameter(name = "initial_value", value = "1")
                    ,
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_GLS_BATCH_SEQ")
    @Column(name = "BATCH_ID")
    private Long batchId;
    @Column(name = "TIME_UPLOADED",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUploaded;
    @Column(name = "MODEL_ID")
    private Long modelId;
    @Size(max = 50)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Size(max = 255)
    @Column(name = "FILE_PATH")
    private String filePath;
    @Size(max = 10)
    @Column(name = "PROCESSING_STATUS")
    private String processingStatus;
    @Column(name = "TIME_COMPLETED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCompleted;
    @JoinColumn(name = "UPLOADED_BY", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser uploadedBy;

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
        return "ke.tra.ufs.webportal.entities.UfsGlsBatch[ batchId=" + batchId + " ]";
    }
    
}
