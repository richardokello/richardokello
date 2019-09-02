package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.wrapper.DashboardItemsWrapper;
import java.util.List;

public interface DashboardStatisticService {

    public List<DashboardItemsWrapper> getDashboardStatistics();

}
