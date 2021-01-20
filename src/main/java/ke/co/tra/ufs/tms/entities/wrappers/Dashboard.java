/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import java.util.List;

/**
 *
 * @author Owori Juma
 */
public class Dashboard {
    
    private boolean isAdmin;
    private boolean isOperator;
    private List<DashboardItemsWrapper> single;
    private List<MultiItemWrapper> multi;

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public Dashboard setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public boolean isIsOperator() {
        return isOperator;
    }

    public Dashboard setIsOperator(boolean isOperator) {
        this.isOperator = isOperator;
        return this;
    }

    public List<DashboardItemsWrapper> getSingle() {
        return single;
    }

    public void setSingle(List<DashboardItemsWrapper> single) {
        this.single = single;
    }

    public List<MultiItemWrapper> getMulti() {
        return multi;
    }

    public void setMulti(List<MultiItemWrapper> multi) {
        this.multi = multi;
    }

}
