/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

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
@Table(name = "UFS_PASSWORD_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsPasswordHistory.findAll", query = "SELECT u FROM UfsPasswordHistory u")
    , @NamedQuery(name = "UfsPasswordHistory.findById", query = "SELECT u FROM UfsPasswordHistory u WHERE u.id = :id")
    , @NamedQuery(name = "UfsPasswordHistory.findByPassword", query = "SELECT u FROM UfsPasswordHistory u WHERE u.password = :password")
    , @NamedQuery(name = "UfsPasswordHistory.findByChangeDate", query = "SELECT u FROM UfsPasswordHistory u WHERE u.changeDate = :changeDate")})
public class UfsPasswordHistory implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "CHANGE_DATE",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_PASSWORD_HISTORY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_PASSWORD_HISTORY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_PASSWORD_HISTORY_SEQ")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser userId;

    public UfsPasswordHistory() {
    }

    public UfsPasswordHistory(Long id) {
        this.id = id;
    }

    public UfsPasswordHistory(Long id, String password, Date changeDate) {
        this.id = id;
        this.password = password;
        this.changeDate = changeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
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
        if (!(object instanceof UfsPasswordHistory)) {
            return false;
        }
        UfsPasswordHistory other = (UfsPasswordHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsPasswordHistory[ id=" + id + " ]";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
}
