package ke.tra.com.tsync.wrappers;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SparrowRequestWrapper {
    @NotNull
    private String accountNumber;
}
