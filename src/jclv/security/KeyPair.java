package jclv.security;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import jclv.security.utils.KeysUtils;

public class KeyPair {
	
	private static KeyPair keyPair = null;
	
	private static HashMap<String, Object> keys = new HashMap<String, Object>(); 
	
	private KeyPair(String privatePath, String publicPath) {
		try {
			keys.put("private", KeysUtils.loadPrivateKey(privatePath));
			keys.put("public", KeysUtils.loadPublicKey(publicPath));	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getInstance(String privatePath, String publicPath) {
		if(keyPair == null) {
			synchronized (KeyPair.class) {
				if(keyPair == null) {
					keyPair = new KeyPair(privatePath, publicPath);
				}
			}
		}
	}
	
	public static PrivateKey getPrivateKey() {
		return (PrivateKey) keys.get("private");
	}
	
	public static PublicKey getPublicKey() {
		return (PublicKey) keys.get("public");
	}

}
