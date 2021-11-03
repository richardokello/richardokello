/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Kenny
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TMS_DEVICE_TIDS_MIDS")
public class TmsDeviceTidsMids implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "TMS_DEVICE_TIDS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_TIDS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_DEVICE_TIDS_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private TmsDevice deviceId;

    @Column(name = "DEVICE_ID")
    private Long deviceIds;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TID")
    private String tid;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MID")
    private String mid;

    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private UfsBanks ufsBanks;
    @ModifiableField
    @Column(name = "BANK_ID")
    private Long ufsBankId;

    @JoinColumn(name = "SWITCH", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsSwitches switchId;
    @Column(name = "SWITCH")
    @Filter
    @ModifiableField
    private Long switchIds;
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY, mappedBy = "tidMidIds")
    @ModifiableField
    private List<TidMidCurrency> tidMidCurrencyList;

    @Transient
    private List<BigDecimal> currencies;

    @Size(max = 3)
    @Filter
    @Searchable
    @Column(name = "INTRASH", insertable = false)
    private String intrash;

    @Filter
    @NotNull
    @Column(name = "IS_MAIN_BANK")
    private Short isMainBank;

}
