package jclv.security.utils;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class AsymmetricEncryptUtils {
	
	public static byte[] Encrypt(PublicKey pk, String message) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
		
		return encrypted;
	}
	
	public static byte[] Decrypt(PrivateKey pk, byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, pk);
		byte[] descrypted = cipher.doFinal(encrypted);
		
		return descrypted;
	}
}
