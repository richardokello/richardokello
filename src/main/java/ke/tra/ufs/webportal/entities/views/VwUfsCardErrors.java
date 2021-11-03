/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.views;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ekangethe
 */
@Entity
@Table(name = "VW_UFS_CARD_ERRORS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwUfsCardErrors.findAll", query = "SELECT v FROM VwUfsCardErrors v")
    , @NamedQuery(name = "VwUfsCardErrors.findByTerminalId", query = "SELECT v FROM VwUfsCardErrors v WHERE v.terminalId = :terminalId")
    , @NamedQuery(name = "VwUfsCardErrors.findByErrorCode", query = "SELECT v FROM VwUfsCardErrors v WHERE v.errorCode = :errorCode")
    , @NamedQuery(name = "VwUfsCardErrors.findByDescription", query = "SELECT v FROM VwUfsCardErrors v WHERE v.description = :description")
    , @NamedQuery(name = "VwUfsCardErrors.findByTuser", query = "SELECT v FROM VwUfsCardErrors v WHERE v.tuser = :tuser")
    , @NamedQuery(name = "VwUfsCardErrors.findByTxnreference", query = "SELECT v FROM VwUfsCardErrors v WHERE v.txnreference = :txnreference")})
public class VwUfsCardErrors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Id
    @Size(min = 1, max = 30)
    @Column(name = "TERMINAL_ID")
    private String terminalId;
    @Size(max = 30)
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Size(max = 30)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 30)
    @Column(name = "TUSER")
    private String tuser;
    @Size(max = 14)
    @Column(name = "TXNREFERENCE")
    private String txnreference;

    public VwUfsCardErrors() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTuser() {
        return tuser;
    }

    public void setTuser(String tuser) {
        this.tuser = tuser;
    }

    public String getTxnreference() {
        return txnreference;
    }

    public void setTxnreference(String txnreference) {
        this.txnreference = txnreference;
    }
    
}
