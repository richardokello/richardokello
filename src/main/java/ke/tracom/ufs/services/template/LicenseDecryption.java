package ke.tracom.ufs.services.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import ke.tracom.ufs.entities.wrapper.LicenseDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class LicenseDecryption {

    public  Optional<LicenseDetails> getLicenseDetails(String licenseKey) {
        System.out.println("licenseKey = " + licenseKey);
        String s = new String(Base64.getDecoder().decode(licenseKey.getBytes(StandardCharsets.UTF_8)));
        log.info("Get License String = " + s);
        String[] split = s.split("_");
        return decrypt(split[0], split[1], split[2]);
    }

    public  Optional<LicenseDetails> decrypt(String initVector, String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            System.out.println(new ObjectMapper().readValue(original, LicenseDetails.class));
            return Optional.of(new ObjectMapper().readValue(original, LicenseDetails.class));

        } catch (Exception ex) {
            log.error("Unable to decrypt license key provided " + ex.getMessage());
        }
        return Optional.empty();
    }
}
