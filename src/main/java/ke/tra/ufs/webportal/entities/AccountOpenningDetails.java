package ke.tra.ufs.webportal.entities;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ACCOUNT_OPENNING_DETAILS")
public class AccountOpenningDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "ACCOUNT_OPENING_DETAILS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ACCOUNT_OPENING_DETAILS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "ACCOUNT_OPENING_DETAILS_SEQ")
    @Column(name = "ID")
    private Long Id;
    @Searchable
    @Filter
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Searchable
    @Filter
    @Column(name = "LAST_NAME")
    private String lastName;
    @Searchable
    @Filter
    @Column(name = "OTHER_NAME")
    private String otherName;
    @Searchable
    @Filter
    @Column(name = "DATE_OF_BIRTH")
    private String dateOfBirth;
    @Searchable
    @Filter
    @Column(name = "PLACE_OF_BIRTH")
    private String placeOfBirth;
    @Searchable
    @Filter
    @Column(name = "GENDER")
    private String gender;
    @Searchable
    @Filter
    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;
    @Searchable
    @Filter
    @Column(name = "NATIONALITY")
    private String nationality;
    @Searchable
    @Filter
    @Column(name = "CUSTOMER_FULL_NAME")
    private String fullName;
    @Searchable
    @Filter
    @Column(name = "CUSTOMER_TYPE")
    private String customerType;
    @Searchable
    @Filter
    @Column(name = "OCCUPATION")
    private String occupation;
    @Searchable
    @Filter
    @Column(name = "OFFICE_PHONE")
    private String officePhone;
    @Searchable
    @Filter
    @Column(name = "TID")
    private String tid;
    @Searchable
    @Filter
    @Column(name = "MID")
    private String mid;
    @Searchable
    @Filter
    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;
    @Searchable
    @Filter
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    @Searchable
    @Filter
    @Column(name = "ID_TYPE")
    private String idType;
    @Searchable
    @Filter
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Searchable
    @Filter
    @Column(name = "BRANCH_CODE")
    private String branchCode;
    @Searchable
    @Filter
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    @Filter(isDateRange = true)
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    @Searchable
    @Filter
    @Column(name = "STATUS")
    private String status;
}
