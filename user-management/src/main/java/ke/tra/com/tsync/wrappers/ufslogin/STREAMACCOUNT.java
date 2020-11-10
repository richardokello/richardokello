
package ke.tra.com.tsync.wrappers.ufslogin;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "payable_item",
    "account_number"
})
public class STREAMACCOUNT {
    @JsonProperty("payable_item")
    private String payableItem;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public STREAMACCOUNT() {
    }

    /**
     * 
     * @param payableItem
     * @param accountNumber
     */
    public STREAMACCOUNT(String payableItem, String accountNumber) {
        super();
        this.payableItem = payableItem;
        this.accountNumber = accountNumber;
    }

    @JsonProperty("payable_item")
    public String getPayableItem() {
        return payableItem;
    }

    @JsonProperty("payable_item")
    public void setPayableItem(String payableItem) {
        this.payableItem = payableItem;
    }

    public STREAMACCOUNT withPayableItem(String payableItem) {
        this.payableItem = payableItem;
        return this;
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("account_number")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public STREAMACCOUNT withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public STREAMACCOUNT withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("payableItem", payableItem).append("accountNumber", accountNumber).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(payableItem).append(additionalProperties).append(accountNumber).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof STREAMACCOUNT) == false) {
            return false;
        }
        STREAMACCOUNT rhs = ((STREAMACCOUNT) other);
        return new EqualsBuilder().append(payableItem, rhs.payableItem).append(additionalProperties, rhs.additionalProperties).append(accountNumber, rhs.accountNumber).isEquals();
    }

}
