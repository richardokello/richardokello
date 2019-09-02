package ke.tra.ufs.webportal.entities.wrapper;

public class DashboardStatistics {

    private Long agents;
    private Long bankBranches;
    private Long agentsAssignedDevices;
    private Long bankRegions;
    private Long agentOutlets;
    private Long agentSupervisors;
    private Long backOfficeUsers;
    private Long headOfDistributions;
    private Long branchManagers;
    private Long regionalManagers;

    public DashboardStatistics() {

    }

    public DashboardStatistics(Long agents, Long bankBranches, Long agentsAssignedDevices,Long bankRegions) {
        this.agents = agents;
        this.bankBranches = bankBranches;
        this.agentsAssignedDevices = agentsAssignedDevices;
        this.bankRegions = bankRegions;
    }

    public Long getAgents() {
        return agents;
    }

    public void setAgents(Long agents) {
        this.agents = agents;
    }

    public Long getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(Long bankBranches) {
        this.bankBranches = bankBranches;
    }

    public Long getAgentsAssignedDevices() {
        return agentsAssignedDevices;
    }

    public void setAgentsAssignedDevices(Long agentsAssignedDevices) {
        this.agentsAssignedDevices = agentsAssignedDevices;
    }

    public Long getBankRegions() {
        return bankRegions;
    }

    public void setBankRegions(Long bankRegions) {
        this.bankRegions = bankRegions;
    }

    public Long getAgentSupervisors() {
        return agentSupervisors;
    }

    public void setAgentSupervisors(Long agentSupervisors) {
        this.agentSupervisors = agentSupervisors;
    }

    public Long getBackOfficeUsers() {
        return backOfficeUsers;
    }

    public void setBackOfficeUsers(Long backOfficeUsers) {
        this.backOfficeUsers = backOfficeUsers;
    }

    public Long getHeadOfDistributions() {
        return headOfDistributions;
    }

    public void setHeadOfDistributions(Long headOfDistributions) {
        this.headOfDistributions = headOfDistributions;
    }

    public Long getBranchManagers() {
        return branchManagers;
    }

    public void setBranchManagers(Long branchManagers) {
        this.branchManagers = branchManagers;
    }

    public Long getRegionalManagers() {
        return regionalManagers;
    }

    public void setRegionalManagers(Long regionalManagers) {
        this.regionalManagers = regionalManagers;
    }

    public Long getAgentOutlets() {
        return agentOutlets;
    }

    public void setAgentOutlets(Long agentOutlets) {
        this.agentOutlets = agentOutlets;
    }
}
