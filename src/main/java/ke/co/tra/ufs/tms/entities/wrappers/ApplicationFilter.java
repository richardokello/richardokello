/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.entities.wrappers;

import ke.co.tra.ufs.tms.entities.wrappers.filters.CommonFilter;

/**
 *
 * @author Cornelius M
 */
public class ApplicationFilter extends CommonFilter{
    
    private String productId;
    private String modelId;

    public ApplicationFilter() {
        this.productId = "";
        this.modelId = "";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    
    

}
