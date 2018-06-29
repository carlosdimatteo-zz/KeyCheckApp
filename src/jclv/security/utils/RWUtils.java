package jclv.security.utils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RWUtils {
	
	public static void writeBytesToFile(byte[] arrayOfByte, String path) throws IOException{
		FileOutputStream fileOutput = new FileOutputStream(System.getProperty("user.dir") + '\\' + path);
		fileOutput.write(arrayOfByte);
		fileOutput.close();
	}
	
	public static String readFileToString(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
}
