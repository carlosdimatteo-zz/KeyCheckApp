package jclv.security.utils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysUtils {
	

	public static PublicKey loadPublicKey(String path) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		String publicKeyPem = RWUtils.readFileToString(path, StandardCharsets.UTF_8);
		
		publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
								   .replace("-----END PUBLIC KEY-----", "")
								   .replaceAll("\\s", "");
		
		byte[] publicKeyDer = Base64.getDecoder().decode(publicKeyPem);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDer));
	    return publicKey;
	}
	
	public static PrivateKey loadPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String privateKeyPem = RWUtils.readFileToString(path, StandardCharsets.UTF_8);

	    privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
						             .replace("-----END PRIVATE KEY-----", "")
						             .replaceAll("\\s", "");

	    byte[] privateKeyDer = Base64.getDecoder().decode(privateKeyPem);

	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDer));
	    return privateKey;
	}
}
