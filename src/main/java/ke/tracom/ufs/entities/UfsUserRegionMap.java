/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author ojuma
 */
@Entity
@Table(name = "UFS_USER_REGION_MAP")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsUserRegionMap.findAll", query = "SELECT u FROM UfsUserRegionMap u")
        , @NamedQuery(name = "UfsUserRegionMap.findById", query = "SELECT u FROM UfsUserRegionMap u WHERE u.id = :id")})
public class UfsUserRegionMap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_USER_REGION_MAP_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_USER_REGION_MAP_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_USER_REGION_MAP_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBankRegion regionId;
    @JsonIgnore
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsUser userId;
    @Column(name = "REGION_ID")
    private Long regionIds;
    @Column(name = "USER_ID")
    private Long userIds;

    public UfsUserRegionMap() {
    }

    public UfsUserRegionMap(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UfsBankRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UfsBankRegion regionId) {
        this.regionId = regionId;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
    }

    public Long getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(Long regionIds) {
        this.regionIds = regionIds;
    }

    public Long getUserIds() {
        return userIds;
    }

    public void setUserIds(Long userIds) {
        this.userIds = userIds;
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
        if (!(object instanceof UfsUserRegionMap)) {
            return false;
        }
        UfsUserRegionMap other = (UfsUserRegionMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.fieldagent.entities.UfsUserRegionMap[ id=" + id + " ]";
    }

}
