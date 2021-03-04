/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "UFS_MCC")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UfsMcc implements Serializable, Comparable<UfsMcc> {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(
            name = "MCC_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MCC_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "MCC_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Searchable
    @ModifiableField
    @Column(name = "NAME")
    private String name;
    @Searchable
    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;
    @ModifiableField
    @Column(name = "VALUE")
    @Searchable
    private String value;
    @ModifiableField
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Filter
    @ModifiableField
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Filter
    @ModifiableField
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @Filter
    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Transient
    @Getter(value = AccessLevel.NONE)
    private String mccName;

    public String getMccName() {
        return getName() + " - " + getValue();
    }

    public UfsMcc(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(UfsMcc o) {
        return Integer.parseInt(getValue()) - Integer.parseInt(o.getValue());
    }
}
