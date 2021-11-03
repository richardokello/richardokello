
package ke.tra.ufs.webportal.wrappers;

import com.google.gson.annotations.Expose;

import javax.annotation.Generated;
import java.util.List;
import java.util.Set;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ReportData {

    @Expose
    private Set<Header> header;
    @Expose
    private List<Row> rows;

    public Set<Header> getHeader() {
        return header;
    }

    public void setHeader(Set<Header> header) {
        this.header = header;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
