/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_SCHEDULE_FILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsScheduleFile.findAll", query = "SELECT t FROM TmsScheduleFile t")
    , @NamedQuery(name = "TmsScheduleFile.findByFileId", query = "SELECT t FROM TmsScheduleFile t WHERE t.fileId = :fileId")
    , @NamedQuery(name = "TmsScheduleFile.findByFileName", query = "SELECT t FROM TmsScheduleFile t WHERE t.fileName = :fileName")
    , @NamedQuery(name = "TmsScheduleFile.findByFilePath", query = "SELECT t FROM TmsScheduleFile t WHERE t.filePath = :filePath")
    , @NamedQuery(name = "TmsScheduleFile.findByTmpFile", query = "SELECT t FROM TmsScheduleFile t WHERE t.tmpFile = :tmpFile")
    , @NamedQuery(name = "TmsScheduleFile.findByIsApp", query = "SELECT t FROM TmsScheduleFile t WHERE t.isApp = :isApp")
    , @NamedQuery(name = "TmsScheduleFile.findByIntrash", query = "SELECT t FROM TmsScheduleFile t WHERE t.intrash = :intrash")})
public class TmsScheduleFile implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_SCHEDULE_FILE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_SCHEDULE_FILE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_SCHEDULE_FILE_SEQ")
    @Basic(optional = false)
    @Column(name = "FILE_ID")//
    private BigDecimal fileId;
    @Size(max = 50)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Size(max = 120)
    @Column(name = "FILE_PATH")
    private String filePath;
    @Size(max = 150)
    @Column(name = "TMP_FILE")
    private String tmpFile;
    @Size(max = 5)
    @Column(name = "IS_APP")
    private String isApp;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
    @ManyToOne
    private TmsScheduler scheduleId;

    public TmsScheduleFile() {
    }

    public TmsScheduleFile(BigDecimal fileId) {
        this.fileId = fileId;
    }

    public BigDecimal getFileId() {
        return fileId;
    }

    public void setFileId(BigDecimal fileId) {
        this.fileId = fileId;
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

    public String getTmpFile() {
        return tmpFile;
    }

    public void setTmpFile(String tmpFile) {
        this.tmpFile = tmpFile;
    }

    public String getIsApp() {
        return isApp;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public TmsScheduler getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(TmsScheduler scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileId != null ? fileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsScheduleFile)) {
            return false;
        }
        TmsScheduleFile other = (TmsScheduleFile) object;
        if ((this.fileId == null && other.fileId != null) || (this.fileId != null && !this.fileId.equals(other.fileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsScheduleFile[ fileId=" + fileId + " ]";
    }
    
}
