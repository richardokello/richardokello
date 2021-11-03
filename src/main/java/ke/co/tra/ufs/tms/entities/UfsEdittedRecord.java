package ke.co.tra.ufs.tms.entities;

import ke.axle.chassis.annotations.EditDataWrapper;
import ke.axle.chassis.annotations.EditEntity;
import ke.axle.chassis.annotations.EditEntityId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_MODIFIED_RECORD")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsEdittedRecord.findAll", query = "SELECT u FROM UfsEdittedRecord u")
        , @NamedQuery(name = "UfsEdittedRecord.findById", query = "SELECT u FROM UfsEdittedRecord u WHERE u.id = :id")
        , @NamedQuery(name = "UfsEdittedRecord.findByUfsEntity", query = "SELECT u FROM UfsEdittedRecord u WHERE u.ufsEntity = :ufsEntity")
        , @NamedQuery(name = "UfsEdittedRecord.findByEntityId", query = "SELECT u FROM UfsEdittedRecord u WHERE u.entityId = :entityId")
        , @NamedQuery(name = "UfsEdittedRecord.findByValues", query = "SELECT u FROM UfsEdittedRecord u WHERE u.values = :values")
        , @NamedQuery(name = "UfsEdittedRecord.findByEdittedOn", query = "SELECT u FROM UfsEdittedRecord u WHERE u.edittedOn = :edittedOn")})
public class UfsEdittedRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_MODIFIED_RECORD_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "UFS_MODIFIED_RECORD_SEQ")
                    ,
                    @Parameter(name = "initial_value", value = "1")
                    ,
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_MODIFIED_RECORD_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 50)
    @Column(name = "UFS_ENTITY")
    @EditEntity
    private String ufsEntity;
    @Size(max = 20)
    @Column(name = "ENTITY_ID")
    @EditEntityId
    private String entityId;
    @Size(max = 4000)
    @Column(name = "\"VALUES\"")
    @EditDataWrapper
    private String values;
    @Column(name = "EDITTED_ON", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date edittedOn;

    public UfsEdittedRecord() {
    }

    public UfsEdittedRecord(BigDecimal id) {
        this.id = id;
    }

    public UfsEdittedRecord(String ufsEntity, String values, String entityId) {
        this.ufsEntity = ufsEntity;
        this.values = values;
        this.entityId = entityId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUfsEntity() {
        return ufsEntity;
    }

    public void setUfsEntity(String ufsEntity) {
        this.ufsEntity = ufsEntity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Date getEdittedOn() {
        return edittedOn;
    }

    public void setEdittedOn(Date edittedOn) {
        this.edittedOn = edittedOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsEdittedRecord)) {
            return false;
        }
        UfsEdittedRecord other = (UfsEdittedRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsEdittedRecord{" + "id=" + id + ", ufsEntity=" + ufsEntity + ", entityId=" + entityId + ", values=" + values + '}';
    }



}

