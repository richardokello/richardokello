package ke.tracom.ufs.wrappers;

import java.math.BigDecimal;

public class IsInitiatorWrapper {
    private BigDecimal userId;
    private String entity;
    private String entityId;
    private String activity;

    public IsInitiatorWrapper(){}
    public IsInitiatorWrapper(BigDecimal userId, String entity, String entityId, String activity) {
        this.userId = userId;
        this.entity = entity;
        this.entityId = entityId;
        this.activity = activity;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
