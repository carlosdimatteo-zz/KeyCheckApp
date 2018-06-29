import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // Algorithm, mode, padding.
//		String text = "Simple plain text";
		
//		String file = FileUtils.readFileToString(new File("prueba.txt"), StandardCharsets.UTF_8);
		String file = RWUtils.readFileToString("prueba.txt", StandardCharsets.UTF_8);
		System.out.println(file);
		
		PublicKey publicKey = KeysUtils.loadPublicKey("keys/public.pem");
		PrivateKey privateKey = KeysUtils.loadPrivateKey("keys/private-pkcs8.pem");
		
		// Encripto la variable TEXT con la llave publica
		byte[] encrypted = AssymetricEncryptUtils.Encrypt(publicKey, file);
		
		// La desencripto con la llave privada
		byte[] descrypted = AssymetricEncryptUtils.Decrypt(privateKey, encrypted);
		
		RWUtils.writeBytesToFile(descrypted, "uploads/decrypted/prueba.txt");
		RWUtils.writeBytesToFile(encrypted, "uploads/encrypted/prueba.txt");
		
		System.out.println("Clear text: " + file);
		System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
		System.out.println("Descrypted: " + new String(descrypted, StandardCharsets.UTF_8));
	}

}
