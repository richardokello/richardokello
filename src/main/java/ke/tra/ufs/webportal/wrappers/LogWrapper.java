package ke.tra.ufs.webportal.wrappers;

public class LogWrapper {


    private String activityType;
    private String status;
    private String entityName;
    private String entityId;
    private String description;
    private String notes;

    private String source;
    private String ipAddress;
    private String clientId;
    private Long _userId;

    public LogWrapper() {
    }

    public LogWrapper(String activityType, String status, String entityName, String entityId,
                      String description, String notes, String source, String ipAddress,
                      String clientId, Long _userId) {
        this.activityType = activityType;
        this.status = status;
        this.entityName = entityName;
        this.entityId = entityId;
        this.description = description;
        this.notes = notes;
        this.source = source;
        this.ipAddress = ipAddress;
        this.clientId = clientId;
        this._userId = _userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long get_userId() {
        return _userId;
    }

    public void set_userId(Long _userId) {
        this._userId = _userId;
    }
}
