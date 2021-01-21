/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.EditDataWrapper;
import ke.axle.chassis.annotations.EditEntity;
import ke.axle.chassis.annotations.EditEntityId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_EDITTED_RECORD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEdittedRecord.findAll", query = "SELECT u FROM UfsEdittedRecord u")
    , @NamedQuery(name = "UfsEdittedRecord.findById", query = "SELECT u FROM UfsEdittedRecord u WHERE u.id = :id")
    , @NamedQuery(name = "UfsEdittedRecord.findByUfsEntity", query = "SELECT u FROM UfsEdittedRecord u WHERE u.ufsEntity = :ufsEntity")
    , @NamedQuery(name = "UfsEdittedRecord.findByEntityId", query = "SELECT u FROM UfsEdittedRecord u WHERE u.entityId = :entityId")
    , @NamedQuery(name = "UfsEdittedRecord.findByData", query = "SELECT u FROM UfsEdittedRecord u WHERE u.data = :data")
    , @NamedQuery(name = "UfsEdittedRecord.findByCreationDate", query = "SELECT u FROM UfsEdittedRecord u WHERE u.creationDate = :creationDate")})
public class UfsEdittedRecord implements Serializable {

    @EditEntity
    @Size(max = 50)
    @Column(name = "UFS_ENTITY")
    private String ufsEntity;
    @Size(max = 20)
    @EditEntityId
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Size(max = 4000)
    @EditDataWrapper
    @Column(name = "DATA")
    private String data;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_EDITTED_RECORD_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_EDITTED_RECORD_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_EDITTED_RECORD_SEQ")
    @Column(name = "ID")
    private Long id;

    public UfsEdittedRecord() {
    }

    public UfsEdittedRecord(Long id) {
        this.id = id;
    }

    public UfsEdittedRecord(Long id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
        return "ke.tracom.ufs.entities.UfsEdittedRecord[ id=" + id + " ]";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
