/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers.filters;

import javax.validation.constraints.Size;

/**
 * @author Cornelius M
 */
public class WhitelistFilter extends CommonFilter {

    @Size(max = 200)
    private String modelId;

    @Size(max = 200)
    private String serialNo;

    private String assignStr;

    public WhitelistFilter() {
        this.modelId = "";
        this.serialNo = "";
        this.assignStr = "";
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getAssignStr() {
        return assignStr;
    }

    public void setAssignStr(String assignStr) {
        this.assignStr = assignStr;
    }
}
