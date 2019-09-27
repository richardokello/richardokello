
package ke.tra.ufs.webportal.wrappers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Values {

    @Expose
    private Long amount = 0L;
    @Expose
    private Long count = 0L;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        if (amount != null) {
            this.amount = amount;
        }
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        if (count != null) {
            this.count = count;
        }
    }
}
