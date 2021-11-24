package ke.co.tra.ufs.tms.entities.wrappers.filters;

public class ParCommonFilter extends CommonFilter{

    private String customerTypeId;
    private String menuLevel;
    private String type;

    public ParCommonFilter() {

        this.customerTypeId = "";
        this.menuLevel = "";
        this.type = "";
    }

    public String getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(String customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
