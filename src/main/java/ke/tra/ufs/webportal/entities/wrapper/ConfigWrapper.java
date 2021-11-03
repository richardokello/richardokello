/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities.wrapper;

/**
 *
 * @author ojuma
 */
import java.util.HashMap;
import java.util.Map;

public class ConfigWrapper {

    private String merchantReference;
    private String merchantName;
    private String outletNumber;
    private String address;
    private String location;
    private String phone;
    private String postilionIp;
    private long postilionPort;
    private String tmsServerIp;
    private long tmsServerPort;
    private String tsyncIp;
    private long tsyncPort;
    private String adminPassword;
    private String merchantPassword;
    private String receiptProfile;
    private long transactionCounter;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostilionIp() {
        return postilionIp;
    }

    public void setPostilionIp(String postilionIp) {
        this.postilionIp = postilionIp;
    }

    public long getPostilionPort() {
        return postilionPort;
    }

    public void setPostilionPort(long postilionPort) {
        this.postilionPort = postilionPort;
    }

    public String getTmsServerIp() {
        return tmsServerIp;
    }

    public void setTmsServerIp(String tmsServerIp) {
        this.tmsServerIp = tmsServerIp;
    }

    public long getTmsServerPort() {
        return tmsServerPort;
    }

    public void setTmsServerPort(long tmsServerPort) {
        this.tmsServerPort = tmsServerPort;
    }

    public String getTsyncIp() {
        return tsyncIp;
    }

    public void setTsyncIp(String tsyncIp) {
        this.tsyncIp = tsyncIp;
    }

    public long getTsyncPort() {
        return tsyncPort;
    }

    public void setTsyncPort(long tsyncPort) {
        this.tsyncPort = tsyncPort;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getReceiptProfile() {
        return receiptProfile;
    }

    public void setReceiptProfile(String receiptProfile) {
        this.receiptProfile = receiptProfile;
    }

    public long getTransactionCounter() {
        return transactionCounter;
    }

    public void setTransactionCounter(long transactionCounter) {
        this.transactionCounter = transactionCounter;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
