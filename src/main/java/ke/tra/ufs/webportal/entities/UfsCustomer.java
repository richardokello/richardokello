/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UfsCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_CUSTOMER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "PIN")
    private String pinNumber;
    @Size(max = 30)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "LOCAL_REG_NUMBER")
    private String localRegistrationNumber;
    @ModifiableField
    @Column(name = "DATE_ISSUED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssued;
    @ModifiableField
    @Column(name = "VALID_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;
    @ModifiableField
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "PHONENUMBER")
    private String businessPrimaryContactNo;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdAt;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @Size(max = 20)
    @ModifiableField
    @Column(name = "BUSINESS_LICENCE_NUMBER")
    private String businessLicenceNumber;
    @JoinColumn(name = "CUSTOMER_CLASS", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsCustomerClass customerClass;
    @ModifiableField
    @Column(name = "CUSTOMER_CLASS")
    private BigInteger customerClassId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    @ModifiableField
    @Filter
    @Searchable
    private String tenantIds;
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "CUSTOMER_NAME")
    private String businessName;
    @Column(name = "TERMINATION_REASON")
    @Filter
    @Searchable
    private String terminationReason;

    @Column(name = "TERMINATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminationDate;

    @JoinColumn(name = "BUSINESS_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsBusinessType businessTypeId;
    @ModifiableField
    @Column(name = "BUSINESS_TYPE_ID")
    private Long businessTypeIds;
    @Size(max = 15)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "SECONDARY_PHONENUMBER")
    private String businessSecondaryContactNo;
    @Size(max = 50)
    @ModifiableField
    @Column(name = "EMAIL_ADDRESS")
    private String businessEmailAddress;
    @JoinColumn(name = "CUSTOMER_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerType customerType;
    @ModifiableField
    @Column(name = "CUSTOMER_TYPE_ID")
    private BigDecimal customerTypeId;
    @JoinColumn(name = "COMMERCIAL_ACTIVITY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsCommercialActivities commercialActivity;
    @ModifiableField
    @Column(name = "COMMERCIAL_ACTIVITY")
    private Long commercialActivityId;
    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private TmsEstateItem estate;
    @Column(name = "ESTATE_ID")
    @ModifiableField
    private BigDecimal estateId;
    @Size(max = 20)
    @Filter
    @Searchable
    @Column(name = "STATUS", insertable = false)
    private String status;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    @Filter
    private String createdBy;
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "MID")
    private String mid;

    @JoinColumn(name = "MCC", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsMcc mccId;
    @ModifiableField
    @Column(name = "MCC")
    @ModifiableChildEntityField(entityName = UfsMcc.class)
    @FieldNickName(name = "MCC")
    private BigDecimal mccIds;
    @JoinColumn(name = "MAIN_BANK", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBanks ufsBanks;
    @Column(name = "MAIN_BANK")
    @Filter
    @ModifiableField
    @ModifiableChildEntityField(entityName = UfsBanks.class)
    @FieldNickName(name = "MAINBANK")
    private Long mainBank;

}
