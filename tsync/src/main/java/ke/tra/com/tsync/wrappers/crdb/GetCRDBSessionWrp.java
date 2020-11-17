package ke.tra.com.tsync.wrappers.crdb;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCRDBSessionWrp {
    String password;
    String partnerID;
    String code;
}

