/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
@Entity
@Table(name = "TMS_APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsApp.findAll", query = "SELECT t FROM TmsApp t")
    , @NamedQuery(name = "TmsApp.findByAppId", query = "SELECT t FROM TmsApp t WHERE t.appId = :appId")
    , @NamedQuery(name = "TmsApp.findByProductId", query = "SELECT t FROM TmsApp t WHERE t.productId = :productId")
    , @NamedQuery(name = "TmsApp.findByModelId", query = "SELECT t FROM TmsApp t WHERE t.modelId = :modelId")
    , @NamedQuery(name = "TmsApp.findByAppName", query = "SELECT t FROM TmsApp t WHERE t.appName = :appName")
    , @NamedQuery(name = "TmsApp.findByAppVersion", query = "SELECT t FROM TmsApp t WHERE t.appVersion = :appVersion")
    , @NamedQuery(name = "TmsApp.findByDescription", query = "SELECT t FROM TmsApp t WHERE t.description = :description")
    , @NamedQuery(name = "TmsApp.findByNotesFilepath", query = "SELECT t FROM TmsApp t WHERE t.notesFilepath = :notesFilepath")
    , @NamedQuery(name = "TmsApp.findByReleaseDate", query = "SELECT t FROM TmsApp t WHERE t.releaseDate = :releaseDate")
    , @NamedQuery(name = "TmsApp.findByAction", query = "SELECT t FROM TmsApp t WHERE t.action = :action")
    , @NamedQuery(name = "TmsApp.findByActionStatus", query = "SELECT t FROM TmsApp t WHERE t.actionStatus = :actionStatus")
    , @NamedQuery(name = "TmsApp.findByIntrash", query = "SELECT t FROM TmsApp t WHERE t.intrash = :intrash")})
public class TmsApp implements Serializable {

    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsDeviceModel model;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsProduct product;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "TMS_APP_SEQ", sequenceName = "TMS_APP_SEQ")
    @GeneratedValue(generator = "TMS_APP_SEQ")
    @Basic(optional = false)//
    @Column(name = "APP_ID")
    private BigDecimal appId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "APP_NAME")
    private String appName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "APP_VERSION")
    private String appVersion;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 255)
    @Column(name = "NOTES_FILEPATH")
    private String notesFilepath;
    @Column(name = "RELEASE_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
    @Size(max = 10)
    @Column(name = "\"ACTION\"")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    @JsonIgnore
    private String intrash;
    @Column(name = "PRODUCT_ID")
    @NotNull
    private BigDecimal productId;
    @NotNull
    @Column(name = "MODEL_ID")
    private BigDecimal modelId;
    @Transient
    @NotNull
    @JsonIgnore
    private MultipartFile application;

    public TmsApp() {
        this.application = new MockMultipartFile("test", "test".getBytes());
    }

    public TmsApp(BigDecimal appId) {
        this.appId = appId;
    }

    public TmsApp(BigDecimal appId, UfsProduct productId, UfsDeviceModel modelId, String appName, String appVersion) {
        this.appId = appId;
        this.product = productId;
        this.model = modelId;
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public BigDecimal getAppId() {
        return appId;
    }

    public void setAppId(BigDecimal appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotesFilepath() {
        return notesFilepath;
    }

    public void setNotesFilepath(String notesFilepath) {
        this.notesFilepath = notesFilepath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public BigDecimal getProductId() {
        return productId;
    }

    public void setProductId(BigDecimal productId) {
        this.product = new UfsProduct(productId);
        this.productId = productId;
    }

    public BigDecimal getModelId() {
        return modelId;
    }

    public void setModelId(BigDecimal modelId) {
        this.model = new UfsDeviceModel(modelId);
        this.modelId = modelId;
    }

    public MultipartFile getApplication() {
        return application;
    }

    public void setApplication(MultipartFile application) {
        this.application = application;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appId != null ? appId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsApp)) {
            return false;
        }
        TmsApp other = (TmsApp) object;
        if ((this.appId == null && other.appId != null) || (this.appId != null && !this.appId.equals(other.appId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.TmsApp[ appId=" + appId + " ]";
    }

    public UfsDeviceModel getModel() {
        return model;
    }

    public void setModel(UfsDeviceModel model) {
        this.model = model;
    }

    public UfsProduct getProduct() {
        return product;
    }

    public void setProduct(UfsProduct product) {
        this.product = product;
    }

}
