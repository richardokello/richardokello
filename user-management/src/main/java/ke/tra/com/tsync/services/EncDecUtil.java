/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.services;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
    
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;


/**
 *
 * @author Tracom
 */

@Service
public class EncDecUtil {

	private static final String UNICODE_FORMAT = "UTF8";
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private final KeySpec ks;
	private final SecretKeyFactory skf;
	private final Cipher cipher;
	byte[] arrayBytes;
	private final String myEncryptionKey;
	private final String myEncryptionScheme;
	SecretKey key;


	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EncDecUtil.class);


	public EncDecUtil() throws Exception {

		myEncryptionKey = "MwagiruKamonithegreatest";
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		ks = new DESedeKeySpec(arrayBytes);
		skf = SecretKeyFactory.getInstance(myEncryptionScheme);
		cipher = Cipher.getInstance(myEncryptionScheme);
		key = skf.generateSecret(ks);
	}

	public String _encrypt(String unencryptedString) {
		String encryptedString = null;
		try {
			
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
			//return encryptedString;
		} catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			//return null;
		}
		return encryptedString;
	}

	public String _decrypt(String encryptedString) {
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedText;
	}


}