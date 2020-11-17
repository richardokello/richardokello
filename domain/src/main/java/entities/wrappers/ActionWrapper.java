package entities.wrappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

public class ActionWrapper<T> {
    @NotNull
    @Size(min = 1, max = 1000)
    private T[] ids;
    @NotNull
    @Size(max = 255)
    private String notes;

    public ActionWrapper() {
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
        return "ActionWrapper{" + "ids=" + Arrays.toString(ids) + ", notes=" + notes + '}';
    }

}
