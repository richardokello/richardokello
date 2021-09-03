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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ke.axle.chassis.annotations.Filter;
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
    @Size(min = 1, max = 100)
    @Column(name = "BANK_NAME")
    private String bankName;
    @Size(max = 50)
    @Column(name = "BANK_CODE")
    private String bankCode;
    @Size(max = 10)
    @Column(name = "PORT")
    private String port;
    @Size(max = 30)
    @Column(name = "SETTLEMENT_ACCOUNT")
    private String settlementAccount;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "BANK_TYPE")
    private String bankType;
    @Size(max = 20)
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
    @ManyToOne
    private UfsCountries country;
    @JoinColumn(name = "SETTLEMENT_CURRENCY", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsCurrency settlementCurrency;

    @Column(name = "BANK_GUARANTEE")
    private BigInteger bankGuarantee;
    @Column(name = "INTERCHANGE_FEE")
    private BigInteger interchangeFee;
    @Column(name = "SETUP_FEE")
    private BigInteger setupFee;
    @Column(name = "SETTLEMENT_CURRENCY")
    private BigInteger settlementCurrencys;
    @Column(name = "COUNTRY")
    private Long countrys;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @Filter
    @Column(name = "TENANT_ID")
    private String tenantIds;
    @Transient
    private List<UfsBankBins> ufsBankBins;

    
}
