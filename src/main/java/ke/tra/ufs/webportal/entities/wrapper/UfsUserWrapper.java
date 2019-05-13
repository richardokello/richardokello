/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.wrapper;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Owori Juma
 */
public class UfsUserWrapper {

    private String fullName;    
    private BigDecimal userId;
    private String userType;
    private String msisdn;
    private String emailAddress;
    private String status;
    private String passwordStatus;
    private Date lastLogin;
    private Date passwordChangeDate;
    private Date creationDate;
    private String action;
    private String actionStatus;

    public UfsUserWrapper(BigDecimal userId) {
        this.userId = userId;
    }

    public UfsUserWrapper(String fullName, BigDecimal userId, String userType, 
            String msisdn, String emailAddress, String status, String passwordStatus, 
            Date lastLogin, Date passwordChangeDate, Date creationDate, String action, String actionStatus) {
        this.fullName = fullName;
        this.userId = userId;
        this.userType = userType;
        this.msisdn = msisdn;
        this.emailAddress = emailAddress;
        this.status = status;
        this.passwordStatus = passwordStatus;
        this.lastLogin = lastLogin;
        this.passwordChangeDate = passwordChangeDate;
        this.creationDate = creationDate;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ApiModelProperty(hidden = true)
    public String getPasswordStatus() {
        return passwordStatus;
    }

    public void setPasswordStatus(String passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    @ApiModelProperty(hidden = true)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @ApiModelProperty(hidden = true)
    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }

    @ApiModelProperty(hidden = true)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsUser[ userId=" + userId + " ]";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
