package ke.tra.com.tsync.wrappers.countyrevenues;

import ke.tra.com.tsync.wrappers.TenantId;

import java.math.BigInteger;
import java.util.List;

public class GeographicalRegionId{
    private String regionName;
    private String code;
    private String action;
    private String actionStatus;
    private String intrash;
    private int id;
    private IsParent isParent;
    private BigInteger creationDate;
    private TenantId tenantId;
    private ParentIds parentIds;
    private int tenantIds;
    private File file;
    private String text;
    private List<Children> children;

    public String getRegionName(){
        return regionName;
    }
    public void setRegionName(String input){
        this.regionName = input;
    }
    public String getCode(){
        return code;
    }
    public void setCode(String input){
        this.code = input;
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
    public IsParent getIsParent(){
        return isParent;
    }
    public void setIsParent(IsParent input){
        this.isParent = input;
    }
    public BigInteger getCreationDate(){
        return creationDate;
    }
    public void setCreationDate(BigInteger input){
        this.creationDate = input;
    }
    public TenantId getTenantId(){
        return tenantId;
    }
    public void setTenantId(TenantId input){
        this.tenantId = input;
    }
    public ParentIds getParentIds(){
        return parentIds;
    }
    public void setParentIds(ParentIds input){
        this.parentIds = input;
    }
    public int getTenantIds(){
        return tenantIds;
    }
    public void setTenantIds(int input){
        this.tenantIds = input;
    }
    public File getFile(){
        return file;
    }
    public void setFile(File input){
        this.file = input;
    }
    public String getText(){
        return text;
    }
    public void setText(String input){
        this.text = input;
    }
    public List<Children> getChildren(){
        return children;
    }
    public void setChildren(List<Children> input){
        this.children = input;
    }

    private class File {
    }
}
