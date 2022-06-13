package ke.tracom.ufs.entities.wrapper;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LicenseDetails {
    private int terminalCount;
    private String expiryDate;
    private String customerName;
    private String projectName;
}
