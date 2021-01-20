package ke.co.tra.ufs.tms.entities.wrappers;

import java.util.List;

public class EquityConfigWrapper {
    private String merchantPassword;
    private String address;
    private String way4ServerIp;
    private String mid;
    private String way4ServerPort;
    private String tid;
    private String posirisServerPort;
    private String merchantName;
    private String outletNumber;
    private String receiptProfile;
    private String phone;
    private String posirisServerIp;
    private String pmsServerPort;
    private String location;
    private String transactionCounter;
    private String pmsServerIp;
    private String adminPassword;
    private String outletName;
    private List<Multicurrency> multicurrency ;

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWay4ServerIp() {
        return way4ServerIp;
    }

    public void setWay4ServerIp(String way4ServerIp) {
        this.way4ServerIp = way4ServerIp;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getWay4ServerPort() {
        return way4ServerPort;
    }

    public void setWay4ServerPort(String way4ServerPort) {
        this.way4ServerPort = way4ServerPort;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPosirisServerPort() {
        return posirisServerPort;
    }

    public void setPosirisServerPort(String posirisServerPort) {
        this.posirisServerPort = posirisServerPort;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOutletNumber() {
        return outletNumber;
    }

    public void setOutletNumber(String outletNumber) {
        this.outletNumber = outletNumber;
    }

    public String getReceiptProfile() {
        return receiptProfile;
    }

    public void setReceiptProfile(String receiptProfile) {
        this.receiptProfile = receiptProfile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosirisServerIp() {
        return posirisServerIp;
    }

    public void setPosirisServerIp(String posirisServerIp) {
        this.posirisServerIp = posirisServerIp;
    }

    public String getPmsServerPort() {
        return pmsServerPort;
    }

    public void setPmsServerPort(String pmsServerPort) {
        this.pmsServerPort = pmsServerPort;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTransactionCounter() {
        return transactionCounter;
    }

    public void setTransactionCounter(String transactionCounter) {
        this.transactionCounter = transactionCounter;
    }

    public String getPmsServerIp() {
        return pmsServerIp;
    }

    public void setPmsServerIp(String pmsServerIp) {
        this.pmsServerIp = pmsServerIp;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public List<Multicurrency> getMulticurrency() {
        return multicurrency;
    }

    public void setMulticurrency(List<Multicurrency> multicurrency) {
        this.multicurrency = multicurrency;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }
}
