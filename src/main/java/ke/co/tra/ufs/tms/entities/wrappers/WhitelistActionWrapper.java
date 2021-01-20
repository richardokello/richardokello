/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

/**
 *
 * @author Owori Juma
 * @param <T>
 */
public class WhitelistActionWrapper<T> {
    @NotNull
    @Size(min = 1, max = 1000)
    private T[] ids;
    @NotNull
    @Size(max = 255)
    private String notes;

    public WhitelistActionWrapper() {
    }

    public T[] getIds() {
        return ids;
    }

    public void setIds(T[] ids) {
        this.ids = ids;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "WhitelistActionWrapper{" + "ids=" + Arrays.toString(ids) + ", notes=" + notes + '}';
    }

}
