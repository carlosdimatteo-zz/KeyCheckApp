package jclv.org;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import jclv.security.KeyPair;
import jclv.security.utils.AsymmetricEncryptUtils;
import jclv.security.utils.KeysUtils;
import jclv.security.utils.RWUtils;


@MultipartConfig
public class ServletMaster extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String rootAdress = System.getProperty("user.dir");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String fileName = req.getParameter("filename");
		System.out.println(fileName);
		PrintWriter out = res.getWriter();
		String decryptedFile = RWUtils.readFileAsString(System.getProperty("user.dir") + "\\uploads\\decrypted\\" + fileName, StandardCharsets.UTF_8);
		System.out.println(decryptedFile);
		
		out.print("{\"status\":200,\"res\":\"" + decryptedFile + "\"}");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		Collection<Part> files = request.getParts();
		PrintWriter out = response.getWriter();		
		
		try {
			if(files.size() == 1) {
				// SYMMETRIC ENCRYPT
				System.out.println("symmetric encrypt");
				Part file = files.iterator().next();
				this.saveFile(file);
				String fileName = this.getFileName(file);
//				this.decryptWithPrivateKey(fileName);
				this.decryptWithPublicKey(fileName)
//				
			} else {
				// SYMETRIC ENCRYPT				
//				System.out.println("Symmetric encrypt");
//				HashMap<String,String> filesName = new HashMap<String,String>();
//				for(Part file: files) {
//					saveFile(file);
//					String fileName = this.getFileName(file);
//					
//					if(fileName.endsWith(".pem")) {						
//						System.out.println("ES LA CLAVE");
//						filesName.put("publicKey", fileName);
//					} else {
//						System.out.println("ES EL ARCHIVO ENCRIPTADO");
//						filesName.put("encryptedFile", fileName);
//					}
//					
//				}
//				System.out.println(filesName.get("publicKey"));
//				System.out.println(filesName.get("encryptedFile"));
////				PublicKey pk = uploadPublicKey(fileNames.get("publicKey")); // Subir la key con el nombre.
////				this.descryptWithPublicKey(publicKey, fileNames.get("encryptedFile")); // Desencriptar con la key obtenida
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.print("{\"status\":200,\"res\":\"Some/error\"}");
		}
		
		out.print("{\"status\":200,\"res\":\"file/uploaded\"}");
	}
	
	private void saveFile(Part file) throws IOException {		
		InputStream is = file.getInputStream();
		OutputStream os = new FileOutputStream(rootAdress + "/uploads/encrypted/" + this.getFileName(file));
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while((read = is.read(bytes)) != -1) {
			os.write(bytes, 0, read);
		}		
		if(is != null)
			is.close();
		if(os != null)
			os.close();
	}
	
	private void decryptWithPrivateKey(String encryptedFileName) throws Exception {
		System.out.println("File name: " + encryptedFileName);
		byte[] encryptedFile = RWUtils.readFileAsByte(System.getProperty("user.dir")+"/uploads/encrypted/" + encryptedFileName);						
		System.out.println("Encrypted: " + encryptedFile);
		PrivateKey privateKey = KeyPair.getPrivateKey();
		System.out.println("Private key: " + privateKey);	
		byte[] decrypted = AsymmetricEncryptUtils.Decrypt(privateKey, encryptedFile);
		System.out.println("Decrypted: " + decrypted);
		RWUtils.writeBytesToFile(decrypted, "uploads/decrypted/" + encryptedFileName);
	}
	
	
	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}