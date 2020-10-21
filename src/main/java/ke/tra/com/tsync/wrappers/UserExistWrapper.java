package ke.tra.com.tsync.wrappers;

import lombok.Data;

@Data
public class UserExistWrapper {
    Boolean userExistByUsernameAndSerialNo = false;
    Boolean userExistByIdNumber = false;
    Long userId;
}
