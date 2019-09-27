package ke.tra.ufs.webportal.wrappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AgentTerminationWrapper<T> {

    @NotNull
    @Size(
            min = 1,
            max = 1000
    )
    private T[] ids;
    @NotNull
    @Size(
            max = 255
    )
    private String notes;
    @NotNull
    @Size(
            max = 100
    )
    private String terminationReason;

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

    public String getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }
}
