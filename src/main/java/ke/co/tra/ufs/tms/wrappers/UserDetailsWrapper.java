package ke.co.tra.ufs.tms.wrappers;

import ke.co.tra.ufs.tms.entities.UfsWorkgroup;

import java.util.List;

public class UserDetailsWrapper {
    private String accountNumber;
    private String localRegNumber;
    private String customerName;
    private String outletName;
    private String outletCode;
    private String contactPerson;
    private String idNumber;
    private String phoneNumber;
    private String assistantRole;
    private List<PosUserWrapper> posUserWrapper;
    private UfsWorkgroup ufsWorkgroup;

    public UserDetailsWrapper() {
    }

    public List<PosUserWrapper> getPosUserWrapper() {
        return posUserWrapper;
    }

    public void setPosUserWrapper(List<PosUserWrapper> posUserWrapper) {
        this.posUserWrapper = posUserWrapper;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLocalRegNumber() {
        return localRegNumber;
    }

    public void setLocalRegNumber(String localRegNumber) {
        this.localRegNumber = localRegNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAssistantRole() {
        return assistantRole;
    }

    public void setAssistantRole(String assistantRole) {
        this.assistantRole = assistantRole;
    }

    public UfsWorkgroup getUfsWorkgroup() {
        return ufsWorkgroup;
    }

    public void setUfsWorkgroup(UfsWorkgroup ufsWorkgroup) {
        this.ufsWorkgroup = ufsWorkgroup;
    }
}
