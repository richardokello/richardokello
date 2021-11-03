package ke.tra.ufs.webportal.entities.wrapper;


import java.math.BigDecimal;

public class contactPersonDeviceWrapper {

    private Long contactPersonId;
    private BigDecimal deviceId;

    public Long getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Long contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public BigDecimal getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(BigDecimal deviceId) {
        this.deviceId = deviceId;
    }
}
