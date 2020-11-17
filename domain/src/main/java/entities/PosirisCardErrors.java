/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "POSIRIS_CARD_ERRORS")
@NamedQueries({
    @NamedQuery(name = "PosirisCardErrors.findAll", query = "SELECT p FROM PosirisCardErrors p")})
public class PosirisCardErrors implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PFID")
    private Long pfid;
    @Basic(optional = false)
    @Column(name = "TERMINAL_ID")
    private String terminalId;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "TDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tdate;
    @Column(name = "TUSER")
    private String tuser;
    @Column(name = "RRN")
    private String rrn;

    public PosirisCardErrors() {
    }

    public PosirisCardErrors(Long pfid) {
        this.pfid = pfid;
    }

    public PosirisCardErrors(Long pfid, String terminalId) {
        this.pfid = pfid;
        this.terminalId = terminalId;
    }

    public Long getPfid() {
        return pfid;
    }

    public void setPfid(Long pfid) {
        this.pfid = pfid;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Date getTdate() {
        return tdate;
    }

    public void setTdate(Date tdate) {
        this.tdate = tdate;
    }

    public String getTuser() {
        return tuser;
    }

    public void setTuser(String tuser) {
        this.tuser = tuser;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pfid != null ? pfid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosirisCardErrors)) {
            return false;
        }
        PosirisCardErrors other = (PosirisCardErrors) object;
        if ((this.pfid == null && other.pfid != null) || (this.pfid != null && !this.pfid.equals(other.pfid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.PosirisCardErrors[ pfid=" + pfid + " ]";
    }
    
}
