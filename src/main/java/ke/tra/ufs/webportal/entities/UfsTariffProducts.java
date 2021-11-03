package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "UFS_TARIFF_PRODUCTS")
public class UfsTariffProducts implements Serializable {

    @Id
    @SequenceGenerator(name = "UFS_TARIFF_PRODUCTS_SEQ", sequenceName = "UFS_TARIFF_PRODUCTS_SEQ")
    @GeneratedValue(generator = "UFS_TARIFF_PRODUCTS_SEQ")
    @Column(name = "ID")
    private BigInteger id;

    @ModifiableField
    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ModifiableField
    @Column(name = "MIN_LIMIT")
    private Double minLimit;

    @ModifiableField
    @Column(name = "MAX_LIMIT")
    private Double maxLimit;

    @Filter
    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @ModifiableField
    @Filter
    @Column(name = "BANK_ACCOUNT")
    private String glAccount;

    @Column(name = "TARIFF")
    private BigInteger tariffId;

    @ManyToOne
    @JoinColumn(name = "TARIFF", referencedColumnName = "ID", insertable = false, updatable = false)
    private UfsTariffs tariff;


    @Filter(isDateRange = true)
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Size(max = 15)
    @Column(name = "ACTION", insertable = false)
    private String action;

    @Filter
    @Size(max = 20)
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;

    @Size(max = 5)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

}
