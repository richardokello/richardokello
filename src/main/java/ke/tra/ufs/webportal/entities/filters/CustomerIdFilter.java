package ke.tra.ufs.webportal.entities.filters;

public class CustomerIdFilter extends CommonFilter{

    private String customerIds;

    public CustomerIdFilter() {
        this.customerIds = "";
    }

    public String getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }
}
