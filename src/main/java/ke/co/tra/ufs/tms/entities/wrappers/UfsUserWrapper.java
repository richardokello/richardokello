/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ke.co.tra.ufs.tms.entities.UfsUserRole;
import lombok.Data;

/**
 * @author Owori Juma
 */
@Data
public class UfsUserWrapper {

    private String fullName;
    private BigDecimal userId;
    private String userType;
    private String status;
    private Date creationDate;
    private String action;
    private String actionStatus;
    private Set<UfsUserRole> userRoles;

    public UfsUserWrapper(BigDecimal userId) {
        this.userId = userId;
    }

    public UfsUserWrapper(String fullName, BigDecimal userId, String userType,
                          String status, Date creationDate, String action, String actionStatus) {
        this.fullName = fullName;
        this.userId = userId;
        this.userType = userType;
        this.status = status;
        this.creationDate = creationDate;
        this.action = action;
        this.actionStatus = actionStatus;
    }


}
