package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "UFS_CONTACT_PERSON")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsContactPerson.findAll", query = "SELECT u FROM UfsContactPerson u"),
        @NamedQuery(name = "UfsContactPerson.findById", query = "SELECT u FROM UfsContactPerson u WHERE u.id = :id"),
        @NamedQuery(name = "UfsContactPerson.findByName", query = "SELECT u FROM UfsContactPerson u WHERE u.name = :name"),
        @NamedQuery(name = "UfsContactPerson.findByIdNumber", query = "SELECT u FROM UfsContactPerson u WHERE u.idNumber = :idNumber"),
        @NamedQuery(name = "UfsContactPerson.findByPhoneNumber", query = "SELECT u FROM UfsContactPerson u WHERE u.phoneNumber = :phoneNumber"),
        @NamedQuery(name = "UfsContactPerson.findByEmail", query = "SELECT u FROM UfsContactPerson u WHERE u.email = :email"),
        @NamedQuery(name = "UfsContactPerson.findByAction", query = "SELECT u FROM UfsContactPerson u WHERE u.action = :action"),
        @NamedQuery(name = "UfsContactPerson.findByActionStatus", query = "SELECT u FROM UfsContactPerson u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsContactPerson.findByIntrash", query = "SELECT u FROM UfsContactPerson u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsContactPerson.findByCreatedAt", query = "SELECT u FROM UfsContactPerson u WHERE u.createdAt = :createdAt")})
public class UfsContactPerson implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerOutlet outletId;

    @Column(name = "OUTLET_ID")
    private Long outletIds;

    @OneToMany(mappedBy = "contactPersonId")
    private Collection<UfsPosUser> ufsPosUserCollection;

    public UfsContactPerson() {
    }

    public UfsContactPerson(Long id) {
        this.id = id;
    }

    public UfsContactPerson(Long id, String name, String idNumber, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UfsCustomerOutlet getOutletId() {
        return outletId;
    }

    public void setOutletId(UfsCustomerOutlet outletId) {
        this.outletId = outletId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsPosUser> getUfsPosUserCollection() {
        return ufsPosUserCollection;
    }

    public void setUfsPosUserCollection(Collection<UfsPosUser> ufsPosUserCollection) {
        this.ufsPosUserCollection = ufsPosUserCollection;
    }

    public Long getOutletIds() {
        return outletIds;
    }

    public void setOutletIds(Long outletIds) {
        this.outletIds = outletIds;
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
        if (!(object instanceof UfsContactPerson)) {
            return false;
        }
        UfsContactPerson other = (UfsContactPerson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsContactPerson[ id=" + id + " ]";
    }

}
