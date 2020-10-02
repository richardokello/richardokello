package ke.tra.ufs.webportal.service.template;


import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.DashboardItemsWrapper;
import ke.tra.ufs.webportal.repository.*;
import ke.tra.ufs.webportal.service.DashboardStatisticService;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public DashboardStatisticServiceTemplate(CustomerRepository customerRepository, UfsBankBranchesRepository bankBranchesRepository,
                                             UfsBankRegionRepository bankRegionRepository, TmsDeviceRepository tmsDeviceRepository,
                                             UserRepository userRepository, UfsCustomerOutletRepository customerOutletRepository,
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
        List<UfsUser> resultList = userRepository.findByUserTypeIdAndIntrash(userType.getTypeId(), intrash);
        Integer size = resultList.size();
        return size.longValue();

    }

    @Override
    public List<DashboardItemsWrapper> getDashboardStatistics() {

        List<DashboardItemsWrapper> single = new ArrayList<>();
        single.add(new DashboardItemsWrapper("Total Customers", getTotalAgents(AppConstants.NO), "/agency-webportal/customers"));
        single.add(new DashboardItemsWrapper("Total Bank Branches", getTotalBankBranches(AppConstants.NO), "/agency-webportal/bank-branches"));
        single.add(new DashboardItemsWrapper("Total Bank Regions", getTotalBankRegions(AppConstants.NO), "/agency-webportal/bank-regions"));
        single.add(new DashboardItemsWrapper("Agents Assigned Device", getTotalAssignedDeviceAgents(AppConstants.NO), "/agency-webportal/device-management/assigned-devices-list"));
//        single.add(new DashboardItemsWrapper("Total Outlets", getTotalOutlets(AppConstants.NO), "/agency-webportal/customer-outlets"));
        single.add(new DashboardItemsWrapper("Total Outlets", getTotalOutlets(AppConstants.NO), ""));
        //single.add(new DashboardItemsWrapper("Agent Supervisors", getTotalTypeUsers(AppConstants.USER_TYPE_AGENT_SUPERVISOR, AppConstants.NO), "/common-modules/users/agent-supervisors"));
        single.add(new DashboardItemsWrapper("BackOffice Users", getTotalTypeUsers(AppConstants.USER_TYPE_BACKOFFICE_USER, AppConstants.NO), "/common-modules/users/back-office"));
        //single.add(new DashboardItemsWrapper("Head Of Distribution", getTotalTypeUsers(AppConstants.USER_TYPE_HEAD_OF_DISTRIBUTION, AppConstants.NO), "/common-modules/users/hod"));
        //single.add(new DashboardItemsWrapper("Branch Managers", getTotalTypeUsers(AppConstants.USER_TYPE_BRANCH_MANAGER, AppConstants.NO), "/common-modules/users/branch-managers"));
        //single.add(new DashboardItemsWrapper("Regional Managers", getTotalTypeUsers(AppConstants.USER_TYPE_REGIONAL_MANAGER, AppConstants.NO), "/common-modules/users/regional-managers"));
        return single;
    }
}
