
package ke.tra.ufs.webportal.wrappers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Values {

    @Expose
    private Long amount = 0L;
    @Expose
    private Integer count = 0;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        if (amount != null) {
            this.amount = amount;
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count != null) {
            this.count = count;
        }
    }
}
