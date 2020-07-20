package ke.tra.com.tsync.wrappers;

import ke.tra.com.tsync.entities.UfsWorkgroup;
import ke.tra.com.tsync.wrappers.ufslogin.TidMid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosUserWrapper {
    // generic pos user wrapper for most(all requests)
    private String fullName;
    private  String idNumber;
    private String phoneNumber;
    private String password;
    private String serialNumber;
    private String username;
    private String MID;
    private String TID;
    private String pin;
    private String confirmPin;
    private String terminationReason;
    private String currentPin;
    private Set<TidMid> tidMids;
    private String appVersion;
    private Integer gender;
    private String email;
    private String userAccessCode;
    private List<ke.tra.com.tsync.entities.wrappers.filters.PosUserWrapper> posUserWrapper;
    private String ufsWorkgroup;

}

