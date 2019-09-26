
package ke.tra.ufs.webportal.wrappers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Row {

    @Expose
    private String type;
    @Expose
    private Values values;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Values getValues() {
        return values;
    }

    public void setValues(Values values) {
        this.values = values;
    }

}
