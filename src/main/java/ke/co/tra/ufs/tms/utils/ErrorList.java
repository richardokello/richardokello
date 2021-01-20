/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Cornelius M
 * @param <E>
 */
public class ErrorList<E> extends ArrayList<E> {

    private int limit = 10;
    private boolean hasOverflown = false;

    public ErrorList() {
        super();
    }

    public ErrorList(int initialCapacity) {
        super(initialCapacity);
    }

    public ErrorList(Collection c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        if (limit == this.size()) {
            this.hasOverflown = true;
            return false;
        } else {
            return super.add(e);
        }
    }
    /**
     * Used to check if the array limit was exceeded
     * @return 
     */
    public boolean isOverflown() {
        return hasOverflown;
    }
    
    

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
