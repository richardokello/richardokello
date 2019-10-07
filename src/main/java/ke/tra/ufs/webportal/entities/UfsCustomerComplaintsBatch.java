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
 * @author kmwangi
 */
@Entity
@Table(name = "UFS_CUSTOMER_COMPLAINTS_BATCH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerComplaintsBatch.findAll", query = "SELECT u FROM UfsCustomerComplaintsBatch u")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByBatchId", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.batchId = :batchId")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByTimeUploaded", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.timeUploaded = :timeUploaded")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByFileName", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.fileName = :fileName")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByFilePath", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.filePath = :filePath")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByProcessingStatus", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.processingStatus = :processingStatus")
    , @NamedQuery(name = "UfsCustomerComplaintsBatch.findByTimeCompleted", query = "SELECT u FROM UfsCustomerComplaintsBatch u WHERE u.timeCompleted = :timeCompleted")})
public class UfsCustomerComplaintsBatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "CUSTOMER_COMPLAINTS_BATCH_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "CUSTOMER_COMPLAINTS_BATCH_SEQ")
                    ,
                    @Parameter(name = "initial_value", value = "1")
                    ,
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "CUSTOMER_COMPLAINTS_BATCH_SEQ")
    @Column(name = "BATCH_ID")
    private Long batchId;
    @Column(name = "TIME_UPLOADED",insertable = false,updatable = false)
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

    public UfsCustomerComplaintsBatch() {
    }

    public UfsCustomerComplaintsBatch(Long batchId) {
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
        if (!(object instanceof UfsCustomerComplaintsBatch)) {
            return false;
        }
        UfsCustomerComplaintsBatch other = (UfsCustomerComplaintsBatch) object;
        if ((this.batchId == null && other.batchId != null) || (this.batchId != null && !this.batchId.equals(other.batchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerComplaintsBatch[ batchId=" + batchId + " ]";
    }
    
}
