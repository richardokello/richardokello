/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_REGIONS_BATCH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsRegionsBatch.findAll", query = "SELECT u FROM UfsRegionsBatch u")
    , @NamedQuery(name = "UfsRegionsBatch.findById", query = "SELECT u FROM UfsRegionsBatch u WHERE u.id = :id")
    , @NamedQuery(name = "UfsRegionsBatch.findByTimeUploaded", query = "SELECT u FROM UfsRegionsBatch u WHERE u.timeUploaded = :timeUploaded")
    , @NamedQuery(name = "UfsRegionsBatch.findByFileName", query = "SELECT u FROM UfsRegionsBatch u WHERE u.fileName = :fileName")
    , @NamedQuery(name = "UfsRegionsBatch.findByFilePath", query = "SELECT u FROM UfsRegionsBatch u WHERE u.filePath = :filePath")
    , @NamedQuery(name = "UfsRegionsBatch.findByProcessingStatus", query = "SELECT u FROM UfsRegionsBatch u WHERE u.processingStatus = :processingStatus")
    , @NamedQuery(name = "UfsRegionsBatch.findByTimeCompleted", query = "SELECT u FROM UfsRegionsBatch u WHERE u.timeCompleted = :timeCompleted")
    , @NamedQuery(name = "UfsRegionsBatch.findByBatchType", query = "SELECT u FROM UfsRegionsBatch u WHERE u.batchType = :batchType")})
public class UfsRegionsBatch implements Serializable {

    @Size(max = 50)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Size(max = 255)
    @Column(name = "FILE_PATH")
    private String filePath;
    @Size(max = 10)
    @Column(name = "PROCESSING_STATUS")
    private String processingStatus;
    @Size(max = 20)
    @Column(name = "BATCH_TYPE")
    private String batchType;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "TIME_UPLOADED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUploaded;
    @Column(name = "TIME_COMPLETED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCompleted;
    @JoinColumn(name = "UPLOADED_BY", referencedColumnName = "USER_ID")
    @ManyToOne
    private UfsUser uploadedBy;

    public UfsRegionsBatch() {
    }

    public UfsRegionsBatch(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsRegionsBatch)) {
            return false;
        }
        UfsRegionsBatch other = (UfsRegionsBatch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsRegionsBatch[ id=" + id + " ]";
    }
    
}
