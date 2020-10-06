/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_POS_USER")
@Data
public class UfsPosUser implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @GenericGenerator(
            name = "POS_USER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "POS_USER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "POS_USER_SEQ")
    @Column(name = "POS_USER_ID")
    private BigDecimal posUserId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ACTIVE_STATUS")
    private String activeStatus;
    @Size(max = 255)
    @Column(name = "PIN")
    private String pin;
    @Size(max = 10)
    @Column(name = "PIN_STATUS")
    private String pinStatus;
    @Column(name = "PIN_LAST_LOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pinLastLogin;
    @Column(name = "PIN_LOGIN_ATTEMTPS")
    private BigInteger pinLoginAttemtps;
    @Column(name = "PIN_CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pinChangeDate;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH",insertable = false)
    private String intrash;
    @Column(name = "USERNAME")
    private String username;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private TmsDevice tmsDevice;
    @Column(name = "DEVICE_ID")
    @Filter
    @ModifiableField
    private BigDecimal tmsDeviceId;
    @Column(name = "POS_ROLE")
    @Filter
    @ModifiableField
    private String posRole;
    @JoinColumn(name = "CUSTOMER_OWNER_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsCustomerOwners customerOwners;
    @Column(name = "CUSTOMER_OWNER_ID")
    @Filter
    @ModifiableField
    private Long customerOwnersId;
    @JoinColumn(name = "CONTACT_PERSON_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsContactPerson contactPerson;
    @Column(name = "CONTACT_PERSON_ID")
    @Filter
    @ModifiableField
    private Long contactPersonId;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ID_NUMBER")
    private String idNumber;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "OTHERNAME")
    private String otherName;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;


}
