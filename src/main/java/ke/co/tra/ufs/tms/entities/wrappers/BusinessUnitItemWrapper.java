/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.NotNull;

/**
 *
 * @author tracom9
 */
public class BusinessUnitItemWrapper {
    
    BigDecimal unitItemId;
    BigInteger isParent;
    BigDecimal parentId;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    BigDecimal unitId;

    public BigDecimal getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(BigDecimal unitItemId) {
        this.unitItemId = unitItemId;
    }

    public BigInteger getIsParent() {
        return isParent;
    }

    public void setIsParent(BigInteger isParent) {
        this.isParent = isParent;
    }

    public BigDecimal getParentId() {
        return parentId;
    }

    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitId() {
        return unitId;
    }

    public void setUnitId(BigDecimal unitId) {
        this.unitId = unitId;
    }
    
    
}
