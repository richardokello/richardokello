/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.wrappers;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author emuraya
 */
public class MlkUserStaffWrapper {

    private String userType;
    private String gender;
    private String email;

    private String fullName;
    private String dateOfBirth;
    private List<BigDecimal> workgroupIds;
    private String phoneNumber;

    public MlkUserStaffWrapper() {
    }

    public MlkUserStaffWrapper(String userType, String gender, String email, String fullName, String dateOfBirth, List<BigDecimal> workgroupIds, String phoneNumber) {
        this.userType = userType;
        this.gender = gender;
        this.email = email;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.workgroupIds = workgroupIds;
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public List<BigDecimal> getWorkgroupIds() {
        return workgroupIds;
    }

    public void setWorkgroupIds(List<BigDecimal> workgroupIds) {
        this.workgroupIds = workgroupIds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
