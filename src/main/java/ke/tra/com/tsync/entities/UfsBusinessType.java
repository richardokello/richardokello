package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;


/**
 * @author Tracom
 */
@Entity
@Data
@Table(name = "UFS_BUSINESS_TYPE")
public class UfsBusinessType implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "BUSINESS_TYPE")
    private String businessType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "businessTypeId")
    private Collection<UfsCustomer> ufsCustomerCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BUSINESS_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BUSINESS_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_BUSINESS_TYPE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdAt;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

}
