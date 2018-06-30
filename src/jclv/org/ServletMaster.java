package jclv.org;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import jclv.security.utils.AssymetricEncryptUtils;
import jclv.security.utils.KeysUtils;
import jclv.security.utils.RWUtils;


@MultipartConfig
public class ServletMaster extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.print("{\"status\":200,\"res\":\"TEST/SERVLET\"}");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				
		Part file = req.getPart("file");
		InputStream filecontent = file.getInputStream();
		OutputStream os = null;
		try {
			String baseDir = System.getProperty("user.dir")+"\\uploads\\encrypted\\";
			os = new FileOutputStream(baseDir + "/" + this.getFileName(file));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				System.out.println("Writing file to location");
				os.write(bytes, 0, read);
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (filecontent != null) {
				System.out.println("Closed File Content");
				filecontent.close();
			}
			if (os != null) {
				System.out.println("closed Output Stream");
				os.close();
			}
			this.decryptFile(file , res);
			PrintWriter out = res.getWriter();
			out.print("{\"status\":200,\"res\":\"file/decrypted\"}");
		}

	}
	
	private void decryptFile(Part file,HttpServletResponse res) {
		try{
			System.out.println("file name: "+this.getFileName(file));
			System.out.println("Decrypting file ");
//			String file = FileUtils.readFileToString(new File("prueba.txt"), StandardCharsets.UTF_8);
			byte[] encryptedFile = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/uploads/encrypted/"+this.getFileName(file)));
			System.out.println("encrypted: " + encryptedFile);
			
//			PublicKey publicKey = KeysUtils.loadPublicKey("keys/public.pem");
			PrivateKey privateKey = KeysUtils.loadPrivateKey("keys/private-pkcs8.pem");
			System.out.println("private key: "+privateKey);
			// Encripto la variable TEXT con la llave publica
//			byte[] encrypted = AssymetricEncryptUtils.Encrypt(publicKey, encryptedFile);
			
			// La desencripto con la llave privada
			byte[] decrypted = AssymetricEncryptUtils.Decrypt(privateKey, encryptedFile);
			
			RWUtils.writeBytesToFile(decrypted, "uploads/decrypted/"+this.getFileName(file));
//			RWUtils.writeBytesToFile(encrypted, "uploads/encrypted/prueba.txt");
			
			System.out.println("Clear text: " + encryptedFile);
//			System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
			System.out.println("Descrypted: " + new String(decrypted, StandardCharsets.UTF_8));
			PrintWriter out = res.getWriter();
			out.print("{\"status\":200,\"res\":\"file/decrypted\"}");
			}catch(Exception e) {
				e.printStackTrace();
				
			}
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