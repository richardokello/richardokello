package ke.tra.com.tsync.entities.wrappers.filters;


import ke.tra.com.tsync.wrappers.ufslogin.TidMid;

import java.util.Set;

public class PosUserWrapper {
    private String serialnumber;
    private Set<TidMid> tidMids;

    public PosUserWrapper() {
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public Set<TidMid> getTidMids() {
        return tidMids;
    }

    public void setTidMids(Set<TidMid> tidMids) {
        this.tidMids = tidMids;
    }

}
