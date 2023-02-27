package ke.tra.ufs.webportal.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaPublicKeyData {
    private String issuer;

    private String exponent;

    private String ridIndex;

    private String ridList;

    private String modulus;

    private String keyLength;

    private String SHA1;

    private String keyType;

    private String expiry;


}
