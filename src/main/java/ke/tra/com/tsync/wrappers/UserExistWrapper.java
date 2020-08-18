package ke.tra.com.tsync.wrappers;

import lombok.Data;

@Data
public class UserExistWrapper {
    Boolean userExistByUsername = false;
    Boolean userExistByIdNumber = false;
    Long userId;
}
