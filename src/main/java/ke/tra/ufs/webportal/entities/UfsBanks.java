/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author kmwangi
 */
@Entity
@Table(name = "UFS_BANKS")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UfsBanks implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANKS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANKS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_BANKS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Size(min = 1, max = 100)
    @Column(name = "BANK_NAME")
    private String bankName;
    @Size(max = 50)
    @ModifiableField
    @Column(name = "BANK_CODE")
    private String bankCode;
    @Size(max = 10)
    @ModifiableField
    @Column(name = "PORT")
    private String port;
    @Size(max = 30)
    @ModifiableField
    @Column(name = "SETTLEMENT_ACCOUNT")
    private String settlementAccount;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @ModifiableField
    @Column(name = "BANK_TYPE")
    private String bankType;
    @Size(max = 20)
    @ModifiableField
    @Column(name = "COMMISION_DEFINITION")
    private String commisionDefinition;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    @JoinColumn(name = "COUNTRY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsCountries country;
    @JoinColumn(name = "SETTLEMENT_CURRENCY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsCurrency settlementCurrency;
    @ModifiableField
    @Column(name = "BANK_GUARANTEE")
    private BigInteger bankGuarantee;
    @ModifiableField
    @Column(name = "INTERCHANGE_FEE")
    private BigInteger interchangeFee;
    @ModifiableField
    @Column(name = "SETUP_FEE")
    private BigInteger setupFee;
    @ModifiableField
    @Column(name = "SETTLEMENT_CURRENCY")
    private BigInteger settlementCurrencys;
    @ModifiableField
    @Column(name = "COUNTRY")
    private Long countrys;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdAt;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsOrganizationUnits tenantId;
    @Filter
    @ModifiableField
    @Column(name = "TENANT_ID")
    private String tenantIds;
    @Transient
    private List<UfsBankBins> ufsBankBins;
}
