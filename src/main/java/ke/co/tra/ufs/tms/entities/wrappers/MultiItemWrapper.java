package ke.co.tra.ufs.tms.entities.wrappers;

import java.util.List;

/**
 *
 * @author Owori Juma
 */
public class MultiItemWrapper {

    private String name;
    private List<DashboardItemsWrapper> series;

    public MultiItemWrapper(String name, List<DashboardItemsWrapper> series) {
        this.name = name;
        this.series = series;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DashboardItemsWrapper> getSeries() {
        return series;
    }

    public void setSeries(List<DashboardItemsWrapper> series) {
        this.series = series;
    }

}
