package ke.tracom.ufs.entities.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpConfigWrapper {

    private String otpAttempts;
    private Integer expiry;
}
