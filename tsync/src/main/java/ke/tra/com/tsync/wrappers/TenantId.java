package ke.tra.com.tsync.wrappers;

import ke.tra.com.tsync.wrappers.countyrevenues.Children;

import java.util.List;

public class TenantId{
    private String name;
    private String action;
    private String actionStatus;
    private String intrash;
    private int id;
    private int isParent;
    private int levelIds;
    private List<Children> children;
    private int parentIds;
    private String text;

    public String getName(){
        return name;
    }
    public void setName(String input){
        this.name = input;
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
    public int getIsParent(){
        return isParent;
    }
    public void setIsParent(int input){
        this.isParent = input;
    }
    public int getLevelIds(){
        return levelIds;
    }
    public void setLevelIds(int input){
        this.levelIds = input;
    }
    public List<Children> getChildren(){
        return children;
    }
    public void setChildren(List<Children> input){
        this.children = input;
    }
    public int getParentIds(){
        return parentIds;
    }
    public void setParentIds(int input){
        this.parentIds = input;
    }
    public String getText(){
        return text;
    }
    public void setText(String input){
        this.text = input;
    }
}