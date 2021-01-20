package ke.co.tra.ufs.tms.entities.wrappers;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class Billers {

    private Long id;
    private String billerId;
    private String billerName;
    private String billerCategory;
    private Date dateCreated;
    private Short status;
    private String createdBy;
    private String confirmedBy;
    private MultipartFile file;

    public Billers() {
    }

    public Billers(Long id) {
        this.id = id;
    }

    public Billers(Long id, String billerId, String billerName, String billerCategory, Short status) {
        this.id = id;
        this.billerId = billerId;
        this.billerName = billerName;
        this.billerCategory = billerCategory;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getBillerCategory() {
        return billerCategory;
    }

    public void setBillerCategory(String billerCategory) {
        this.billerCategory = billerCategory;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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
        if (!(object instanceof Billers)) {
            return false;
        }
        Billers other = (Billers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ufs.tidservice.tidfetch.entities.Billers[ id=" + id + " ]";
    }

}
