package jclv.org;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class Main {
	
	public static void main(String[] args) throws LifecycleException {
		
		int port = 8080;
		String web = "\\app";
		
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		Context context = null;
		
		Connector connector = tomcat.getConnector();
		connector.setURIEncoding("UTF-8");
		connector.setMaxPostSize(5000000);
		
		try {
			context = tomcat.addWebapp("/", System.getProperty("user.dir") + web);
		} catch(ServletException e) {
			e.printStackTrace();
		}		
		// Servlets
		Tomcat.addServlet(context, "ServletMaster", new ServletMaster());
		context.addServletMappingDecoded("/test", "ServletMaster");
		
		// Configs
		context.setAllowCasualMultipartParsing(true);
		
		// Init
		// KeyPair.getInstance("keys/private-pkcs8.pem", "keys/public.pem");
		
		tomcat.start();
	    tomcat.getServer().await();
	}

}
