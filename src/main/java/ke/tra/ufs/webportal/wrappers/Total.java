package ke.tra.ufs.webportal.wrappers;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Total {

    @Expose
    private List<Values> values;

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }
}
