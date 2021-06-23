package co.ke.tracom.bprgateway.web.sendmoney.services;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

@Component
public class DesUtil {

	private static final String UNICODE_FORMAT = "UTF8";
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec ks;
	private SecretKeyFactory skf;
	private Cipher cipher;
	byte[] arrayBytes;
	private String myEncryptionKey;
	private String myEncryptionScheme;
	SecretKey key;

	public DesUtil() throws Exception {
		myEncryptionKey = "MwagiruKamonithegreatest";
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		ks = new DESedeKeySpec(arrayBytes);
		skf = SecretKeyFactory.getInstance(myEncryptionScheme);
		cipher = Cipher.getInstance(myEncryptionScheme);
		key = skf.generateSecret(ks);
	}

	public String encryptPlainText(String plainTextInput) {
		String encryptedString = null;
		try {
			
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = plainTextInput.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
			//return encryptedString;
		} catch (IllegalBlockSizeException | InvalidKeyException e) {
			e.printStackTrace();
			//return null;
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptedString;
	}

	public  String _decrypt(String encryptedString) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			return new String(plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
