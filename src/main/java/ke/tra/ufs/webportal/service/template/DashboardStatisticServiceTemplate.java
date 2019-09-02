package ke.tra.ufs.webportal.service.template;


import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.DashboardStatistics;
import ke.tra.ufs.webportal.repository.*;
import ke.tra.ufs.webportal.service.DashboardStatisticService;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardStatisticServiceTemplate implements DashboardStatisticService {

    private final CustomerRepository customerRepository;
    private final UfsBankBranchesRepository bankBranchesRepository;
    private final UfsBankRegionRepository bankRegionRepository;
    private final TmsDeviceRepository tmsDeviceRepository;
    private final UfsCustomerOutletRepository customerOutletRepository;
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public DashboardStatisticServiceTemplate(CustomerRepository customerRepository,UfsBankBranchesRepository bankBranchesRepository,
                                             UfsBankRegionRepository bankRegionRepository,TmsDeviceRepository tmsDeviceRepository,
                                             UserRepository userRepository,UfsCustomerOutletRepository customerOutletRepository,
                                             UserTypeRepository userTypeRepository) {
        this.customerRepository = customerRepository;
        this.bankBranchesRepository = bankBranchesRepository;
        this.bankRegionRepository = bankRegionRepository;
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.userRepository = userRepository;
        this.customerOutletRepository = customerOutletRepository;
        this.userTypeRepository = userTypeRepository;
    }

    private Long getTotalAgents(String intrash) {
        List<UfsCustomer> resultList = customerRepository.findByIntrash(intrash);
        Integer size = resultList.size();
        return size.longValue();
    }

    private Long getTotalBankBranches(String intrash) {
        List<UfsBankBranches> resultList = bankBranchesRepository.findByIntrash(intrash);
        Integer size = resultList.size();
        return size.longValue();
    }

    private Long getTotalBankRegions(String intrash) {
        List<UfsBankRegion> resultList = bankRegionRepository.findByIntrash(intrash);
        Integer size = resultList.size();
        return size.longValue();
    }

    private Long getTotalAssignedDeviceAgents(String intrash) {
        List<TmsDevice> resultList = tmsDeviceRepository.findByIntrash(intrash);
        Integer size = resultList.size();
        return size.longValue();
    }

    private Long getTotalOutlets(String intrash) {
        List<UfsCustomerOutlet> resultList = customerOutletRepository.findByIntrash(intrash);
        Integer size = resultList.size();
        return size.longValue();
    }

    private Long getTotalTypeUsers(String userTypeName, String intrash) {

        UfsUserType userType = userTypeRepository.findByUserType(userTypeName);
        List<UfsUser> resultList = userRepository.findByUserTypeIdAndIntrash(userType.getTypeId(),intrash);
        Integer size = resultList.size();
        return size.longValue();

    }

    @Override
    public DashboardStatistics getDashboardStatistics() {
        DashboardStatistics dashboardStatistics = new DashboardStatistics();
        dashboardStatistics.setAgents(getTotalAgents(AppConstants.NO));
        dashboardStatistics.setBankBranches(getTotalBankBranches(AppConstants.NO));
        dashboardStatistics.setBankRegions(getTotalBankRegions(AppConstants.NO));
        dashboardStatistics.setAgentsAssignedDevices(getTotalAssignedDeviceAgents(AppConstants.NO));
        dashboardStatistics.setAgentOutlets(getTotalOutlets(AppConstants.NO));
        dashboardStatistics.setAgentSupervisors(getTotalTypeUsers(AppConstants.USER_TYPE_AGENT_SUPERVISOR,AppConstants.NO));
        dashboardStatistics.setBackOfficeUsers(getTotalTypeUsers(AppConstants.USER_TYPE_BACKOFFICE_USER,AppConstants.NO));
        dashboardStatistics.setHeadOfDistributions(getTotalTypeUsers(AppConstants.USER_TYPE_HEAD_OF_DISTRIBUTION,AppConstants.NO));
        dashboardStatistics.setBranchManagers(getTotalTypeUsers(AppConstants.USER_TYPE_BRANCH_MANAGER,AppConstants.NO));
        dashboardStatistics.setRegionalManagers(getTotalTypeUsers(AppConstants.USER_TYPE_REGIONAL_MANAGER,AppConstants.NO));
        return dashboardStatistics;
    }
}
