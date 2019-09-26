
package ke.tra.ufs.webportal.wrappers;


import com.google.gson.annotations.Expose;

public class Header {

    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
