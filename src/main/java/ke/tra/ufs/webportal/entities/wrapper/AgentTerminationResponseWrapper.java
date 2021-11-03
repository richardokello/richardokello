package ke.tra.ufs.webportal.entities.wrapper;

import java.util.Date;

public class AgentTerminationResponseWrapper {

    private String agentName;
    private String ownerName;
    private String email;
    private String geographicalRegion;
    private String terminationReason;
    private Date terminationDate;

    public AgentTerminationResponseWrapper() {

    }

    public AgentTerminationResponseWrapper(String agentName, String ownerName, String email, String geographicalRegion, String terminationReason,Date terminationDate) {
        this.agentName = agentName;
        this.ownerName = ownerName;
        this.email = email;
        this.geographicalRegion = geographicalRegion;
        this.terminationReason = terminationReason;
        this.terminationDate = terminationDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(String geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }
}
