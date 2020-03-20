/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

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
@Table(name = "TMS_SCHEDULE_FILE")
@NamedQueries({
    @NamedQuery(name = "TmsScheduleFile.findAll", query = "SELECT t FROM TmsScheduleFile t")})
public class TmsScheduleFile implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "FILE_ID")
    private BigDecimal fileId;
    @Column(name = "SCHEDULE_ID")
    private BigInteger scheduleId;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "TMP_FILE")
    private String tmpFile;
    @Column(name = "IS_APP")
    private String isApp;
    @Column(name = "INTRASH")
    private String intrash;

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

    public BigInteger getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(BigInteger scheduleId) {
        this.scheduleId = scheduleId;
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
        return "com.mycompany.oracleufs.TmsScheduleFile[ fileId=" + fileId + " ]";
    }
    
}
