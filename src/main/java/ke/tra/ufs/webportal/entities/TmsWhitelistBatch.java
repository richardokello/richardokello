/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 *
 * @author Cornelius M
 */
@Entity
@Table(name = "TMS_WHITELIST_BATCH")
@NamedQueries({
    @NamedQuery(name = "TmsWhitelistBatch.findAll", query = "SELECT t FROM TmsWhitelistBatch t")
    , @NamedQuery(name = "TmsWhitelistBatch.findByBatchId", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.batchId = :batchId")
    , @NamedQuery(name = "TmsWhitelistBatch.findByTimeUploaded", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.timeUploaded = :timeUploaded")
    , @NamedQuery(name = "TmsWhitelistBatch.findByFileName", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.fileName = :fileName")
    , @NamedQuery(name = "TmsWhitelistBatch.findByFilePath", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.filePath = :filePath")
    , @NamedQuery(name = "TmsWhitelistBatch.findByProcessingStatus", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.processingStatus = :processingStatus")
     , @NamedQuery(name = "TmsWhitelistBatch.findByTimeCompleted", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.timeCompleted = :timeCompleted")
    , @NamedQuery(name = "TmsWhitelistBatch.findByUploadedBy", query = "SELECT t FROM TmsWhitelistBatch t WHERE t.uploadedBy = :uploadedBy")})
public class TmsWhitelistBatch implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "batch", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TmsWhitelist> whitelist;
    
    @Id
    @Basic(optional = false)    
    @GenericGenerator(
            name = "TMS_WHITELIST_BATCH_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "TMS_WHITELIST_BATCH_SEQ")
                ,
                @Parameter(name = "initial_value", value = "1")
                ,
                @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_WHITELIST_BATCH_SEQ")
    @Column(name = "BATCH_ID")
    private Long batchId;
    @Column(name = "TIME_UPLOADED", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUploaded;
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
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne
    private UfsDeviceModel model;

    public TmsWhitelistBatch() {
    }

    public TmsWhitelistBatch(Long batchId) {
        this.batchId = batchId;
    }


    public TmsWhitelistBatch(String fileName, String filePath, String processingStatus, UfsUser uploadedBy, UfsDeviceModel model) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.processingStatus = processingStatus;
        this.uploadedBy = uploadedBy;
        this.model = model;
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

    public UfsUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UfsUser uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public UfsDeviceModel getModel() {
        return model;
    }

    public void setModel(UfsDeviceModel model) {
        this.model = model;
    }

    public Set<TmsWhitelist> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<TmsWhitelist> whitelist) {
        this.whitelist = whitelist;
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
        return "ke.co.tra.ufs.tms.entities.TmsWhitelistBatch[ batchId=" + batchId + " ]";
    }
}
