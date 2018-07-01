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

import jclv.security.KeyPair;
import jclv.security.utils.AssymetricEncryptUtils;
import jclv.security.utils.KeysUtils;
import jclv.security.utils.RWUtils;


@MultipartConfig
public class ServletMaster extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

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
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				
		Part file = req.getPart("file");
		InputStream filecontent = file.getInputStream();
		OutputStream os = null;
		PrintWriter out = res.getWriter();
		
		try {
			String baseDir = System.getProperty("user.dir")+"\\uploads\\encrypted\\";
			os = new FileOutputStream(baseDir + "/" + this.getFileName(file));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				System.out.println("Writing file to location");
				os.write(bytes, 0, read);		
			}
			this.decryptFile(this.getFileName(file));
			
			out.print("{\"status\":200,\"res\":\"file/uploaded\"}");
		
		} catch (Exception e) {				
			e.printStackTrace();
			out.print("{\"status\":200,\"res\":\"Some/error\"}");
		} finally {
			if (filecontent != null) {
				System.out.println("Closed File Content");
				filecontent.close();
			}
			if (os != null) {
				System.out.println("Closed Output Stream");
				os.close();
			}			
		}

	}
	
	private void decryptFile(String encryptedFileName) throws Exception {
		
		System.out.println("File name: " + encryptedFileName);
		byte[] encryptedFile = RWUtils.readFileAsByte(System.getProperty("user.dir")+"/uploads/encrypted/" + encryptedFileName);						
		System.out.println("Encrypted: " + encryptedFile);
		
		PrivateKey privateKey = KeyPair.getPrivateKey();
		System.out.println("Private key: " + privateKey);	
		byte[] decrypted = AssymetricEncryptUtils.Decrypt(privateKey, encryptedFile);
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