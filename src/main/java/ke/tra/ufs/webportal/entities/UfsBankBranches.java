/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ke.axle.chassis.annotations.Filter;

/**
 * @author ojuma
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UFS_BANK_BRANCHES")
public class UfsBankBranches implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Searchable
    @Filter
    @ModifiableField
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Searchable
    @Filter
    @ModifiableField
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "bankBranchId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<UfsGls> ufsGlsSet;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    @JsonIgnore
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    @Filter
    @ModifiableField
    private String tenantIds;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsBanks ufsBanks;
    @Column(name = "BANK_ID")
    @Filter
    @ModifiableField
    private Long ufsBankId;


    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANK_BRANCHES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANK_BRANCHES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_BANK_BRANCHES_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    //    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
//    @ManyToOne(optional = false)
//    @JsonIgnore
//    private UfsBankRegion bankRegionId;
    @Column(name = "BANK_REGION_ID")
    @ModifiableField
    private BigDecimal bankRegionIds;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsGeographicalRegion geographicalRegionId;
    @Column(name = "GEOGRAPHICAL_REGION_ID")
    @ModifiableField
    private BigDecimal geographicalRegionIds;

}
