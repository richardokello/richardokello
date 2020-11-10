package ke.tra.com.tsync.wrappers;

public class SettlementCurrency{
    private String name;
    private String code;
    private String codeName;
    private String symbol;
    private int decimalValue;
    private int numericValue;
    private String action;
    private String actionStatus;
    private String intrash;
    private int id;
    private int tenantIds;

    public String getName(){
        return name;
    }
    public void setName(String input){
        this.name = input;
    }
    public String getCode(){
        return code;
    }
    public void setCode(String input){
        this.code = input;
    }
    public String getCodeName(){
        return codeName;
    }
    public void setCodeName(String input){
        this.codeName = input;
    }
    public String getSymbol(){
        return symbol;
    }
    public void setSymbol(String input){
        this.symbol = input;
    }
    public int getDecimalValue(){
        return decimalValue;
    }
    public void setDecimalValue(int input){
        this.decimalValue = input;
    }
    public int getNumericValue(){
        return numericValue;
    }
    public void setNumericValue(int input){
        this.numericValue = input;
    }
    public String getAction(){
        return action;
    }
    public void setAction(String input){
        this.action = input;
    }
    public String getActionStatus(){
        return actionStatus;
    }
    public void setActionStatus(String input){
        this.actionStatus = input;
    }
    public String getIntrash(){
        return intrash;
    }
    public void setIntrash(String input){
        this.intrash = input;
    }
    public int getId(){
        return id;
    }
    public void setId(int input){
        this.id = input;
    }
    public int getTenantIds(){
        return tenantIds;
    }
    public void setTenantIds(int input){
        this.tenantIds = input;
    }
}
