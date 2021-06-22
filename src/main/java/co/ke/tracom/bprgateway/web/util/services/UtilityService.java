package co.ke.tracom.bprgateway.web.util.services;

import co.ke.tracom.bprgateway.web.util.data.Field47Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.text.DecimalFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityService {
    private final String ENCRYPTION_PASSWORD = "BPRAGENCY098";
    private final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndTripleDES";

    public Field47Data processField47(String ISOMsgField47) {
        int i = 0;
        String Tag = null;
        String TagLength = null;
        String TagValue = null;
        boolean more = true;

        int skip = 0;

        Field47Data field47Data = new Field47Data();

        while (more) {
            try {

                Tag = ISOMsgField47.substring(skip, skip + 2);
                TagLength = ISOMsgField47.substring(skip + 2, skip + 4);

                if (Integer.parseInt(TagLength) > 0) {
                    TagValue = ISOMsgField47.substring(skip + 4, skip + 4 + Integer.parseInt(TagLength));
                } else TagValue = null;

                switch (Integer.parseInt(Tag)) {
                    case 99:
                        // HostServer = TagValue;
                        break;
                    case 1:
                        // sourcechannel = TagValue;
                        break;
                    case 2:
                        System.out.println("CardHolderName = " + TagValue);
                        break;
                    case 3:
                        // ReceiptNumber = TagValue;
                        break;
                    case 4:
                        System.out.println("BIN = " + TagValue);
                        break;
                    case 6:
                        // Route = TagValue;
                        break;
                    case 7:
                        // TripFrom = TagValue;
                        break;
                    case 8:
                        // TripTo = TagValue;
                        break;
                    case 9:
                        // Driver_Name = TagValue;
                        break;
                    case 10:
                        // Driver_id = TagValue;
                        break;
                    case 11:
                        // Conductor_Name = TagValue;
                        break;
                    case 12: // =====operator_ID=====
                        System.out.println("userID = " + TagValue);
                        field47Data.setUserID(TagValue);
                        break;
                    case 13:
                        break;
                    case 14: // Response Last Name
                        break;
                    case 15: // Response role
                    case 16: // Response Offpeak fare
                        System.out.println("password = " + TagValue);
                        break;
                    case 17: // Role
                        System.out.println("ROLE_ = " + TagValue);
                        break;
                    case 18: // Response Userdefined fare
                        break;
                    case 19:
                        System.out.println("sacconame = " + TagValue);
                        break;
                    case 24:
                        System.out.println("Counter = " + TagValue);
                        break;
                    case 25:
                        System.out.println("appVersion = " + TagValue);
                        field47Data.setAppVersion(TagValue);
                        break;
                    case 29:
                        System.out.println("usergender = " + TagValue);
                        break;
                    case 30:
                        System.out.println("userName = " + TagValue);
                        field47Data.setUserName(TagValue);
                        break;
                    case 31:
                        System.out.println("fullName = " + TagValue);
                        break;
                    case 32:
                        System.out.println("eMail = " + TagValue);
                        break;
                    case 33:
                        System.out.println("phoneNo = " + TagValue);
                        break;
                    case 34:
                        System.out.println("identification = " + TagValue);
                        break;
                    case 35:
                        System.out.println("userpassword = " + TagValue);
                        field47Data.setUserpassword(TagValue);
                        break;
                    case 36:
                        System.out.println("terminalpin = " + TagValue);
                        field47Data.setTerminalpin(TagValue);
                        break;
                    case 37:
                        System.out.println("userworkgroup = " + TagValue);
                        field47Data.setUserworkgroup(TagValue);
                        break;
                    case 38:
                        System.out.println("terminalSerialNo = " + TagValue);
                        field47Data.setTerminalSerialNo(TagValue);
                        break;
                    default:
                        break;
                }
                i++;
                skip = skip + Integer.parseInt(TagLength) + 4;
                if (((skip) == ISOMsgField47.length()) || (i > 50)) {
                    more = false;
                }
            } catch (Exception e) {
                break;
            }
        }

        return field47Data;
    }

    public String decryptSensitiveData(String encryptedText) {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(ENCRYPTION_PASSWORD);
            encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
            return encryptor.decrypt(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String encryptSensitiveData(String plaintext) {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(ENCRYPTION_PASSWORD);
            encryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
            String encryptedText = encryptor.encrypt(plaintext);
            return encryptor.decrypt(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String encryptText(String text) {
        try {
            Key aesKey = new SecretKeySpec("Trx#279!DTCeioc?".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            return new String(Base64.encodeBase64(encrypted));
        } catch (InvalidKeyException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException | NullPointerException | java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException ex) {
            log.info("Failed text encryption. "+ ex.getMessage());
            ex.printStackTrace();
            return text;
        }
    }

    public String decryptText(String encryptedText) {
        try {
            Key aesKey = new SecretKeySpec("Trx#279!DTCeioc?".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, aesKey);
            return new String(cipher.doFinal(Base64.decodeBase64(encryptedText.getBytes())));
        } catch (IllegalArgumentException | InvalidKeyException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException | NullPointerException | java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException ex) {
            log.info("Failed text decryption. "+ ex.getMessage());
            ex.printStackTrace();
            return encryptedText;
        }
    }

    /**
     * Customer are required to enter payment details. Some customers provide non-alphanumeric
     * characters or empty input that causes the request to T24 to fail The function below ensures an
     * alphanumeric value is returned
     *
     * @param paymentdetail1
     * @param DefaultValue
     * @return
     */
    public String sanitizePaymentDetails(String paymentdetail1, String DefaultValue) {
        String paymentDetail = paymentdetail1.isEmpty() ? DefaultValue : paymentdetail1.trim();
        return paymentDetail.replaceAll("\\P{Alnum}", " ");
    }

    public String formatDecimal(float money) {
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return  decimalFormat.format(money);
    }
}
