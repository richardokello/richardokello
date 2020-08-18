package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;

/**
 * @author Owori Juma
 * @author Kenneth Mwangi
 */
@Data
@Entity
@Table(name = "TMS_DEVICE")
public class TmsDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "TMS_DEVICE_SEQ2", sequenceName = "TMS_DEVICE_SEQ2")
    @GeneratedValue(generator = "TMS_DEVICE_SEQ2")
    @Basic(optional = false)
    @Column(name = "DEVICE_ID")
    private BigDecimal deviceId;
    @Basic(optional = false)
    @Searchable
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Column(name = "INTRASH",insertable = false)
    private String intrash;
    @Column(name = "PART_NUMBER")
    private String partNumber;
    @Column(name = "DEVICE_OUTLET_NAME")
    private String deviceFieldName;

    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    @JsonIgnore
    private TmsEstateItem estateId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsCustomerOutlet outletId;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits tenantId;

    @Column(name = "TENANT_ID")
    private String tenantIds;
    @Column(name = "BANK_BRANCH_ID")
    private BigDecimal bankBranchIds;
    @Column(name = "OUTLET_ID")
    private BigDecimal outletIds;

    @Column(name = "CUSTOMER_OWNER_NAME")
    private String customerOwnerName;

    @JoinColumn(name = "DEVICE_TYPE", referencedColumnName = "DEVICE_TYPE_ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsDeviceType deviceType;
    @Column(name = "DEVICE_TYPE")
    @Filter
    @ModifiableField
    private BigDecimal deviceTypeId;
    @Basic(optional = false)
    @Searchable
    @Column(name = "IMEI_NO")
    private String imeiNo;

}
