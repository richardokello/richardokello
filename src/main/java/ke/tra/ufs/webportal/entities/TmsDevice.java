package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    @GenericGenerator(
            name = "TMS_DEVICE_SEQ2",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_SEQ2"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "TMS_DEVICE_SEQ2")
    @Basic(optional = false)
    @Column(name = "DEVICE_ID")
    private BigDecimal deviceId;

    @Basic(optional = false)
    @Searchable
    @Column(name = "SERIAL_NO")
    private String serialNo;

    @Column(name = "STATUS")
    @ModifiableField
    private String status;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date creationDate;

    @ModifiableField
    @Filter
    @Column(name = "ACTION")
    private String action;

    @ModifiableField
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    @ModifiableField
    @Column(name = "INTRASH")
    private String intrash;

    @Column(name = "PART_NUMBER")
    private String partNumber;

    @ModifiableField
    @Column(name = "DEVICE_OUTLET_NAME")
    private String deviceFieldName;

    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne(optional = true)
    private TmsEstateItem estateId;

    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsBankBranches bankBranchId;

    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    @JsonIgnore
    private UfsCustomerOutlet outletId;

    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;

    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    @JsonIgnore
    private UfsOrganizationUnits tenantId;

    @Column(name = "TENANT_ID")
    private String tenantIds;

    @Column(name = "BANK_BRANCH_ID")
    @ModifiableField
    private BigDecimal bankBranchIds;

    @Column(name = "OUTLET_ID")
    @ModifiableField
    private BigDecimal outletIds;

    @ModifiableField
    @Column(name = "CUSTOMER_OWNER_NAME")
    private String customerOwnerName;

    @JoinColumn(name = "DEVICE_TYPE", referencedColumnName = "DEVICE_TYPE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsDeviceType deviceType;

    @Column(name = "DEVICE_TYPE")
    @Filter
    @ModifiableField
    private BigDecimal deviceTypeId;

    @Basic(optional = false)
    @Searchable
    @Column(name = "IMEI_NO")
    @ModifiableField
    private String imeiNo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    @ModifiableField
    private List<TmsDeviceTidsMids> deviceTidsMidsList;

    @JoinColumn(name = "MASTER_PROFILE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private ParGlobalMasterProfile masterProfile;

    @Column(name = "MASTER_PROFILE")
    @Filter
    @ModifiableField
    private BigDecimal masterProfileId;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tmsDevice")
    private List<ParDeviceSelectedOptions> deviceOptions;

    @Column(name = "RELEASE_DEVICE_COUNT")
    private Integer releaseDeviceCount;

    @Column(name = "TID")
    private String tid;

}
