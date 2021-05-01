package co.ke.tracom.bprgateway.web.accountopening.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NIDData {
    String nationalId;
    String firstName;
    String surname;
}
