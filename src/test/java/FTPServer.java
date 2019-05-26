

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class FTPServer {
//修改类RETR在package org.apache.ftpserver.command.impl
	public static void main(String[] args) throws FtpException {
		// TODO Auto-generated method stub
		/*PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("C:/Users/yipu/workspace/MyFtpServer/src/users.properties"));
		//userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
		userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
		UserManager um = userManagerFactory.createUserManager();
		BaseUser user = new BaseUser();
		user.setName("andy");
		user.setPassword("12345678");
		user.setHomeDirectory("d:/ftp/");
		um.save(user);*/
		
		FtpServerFactory serverFactory = new FtpServerFactory();
		
		ListenerFactory factory = new ListenerFactory();
		// set the port of the listener
		factory.setPort(2121);
		// replace the default listener
		serverFactory.addListener("default", factory.createListener());
		
		FtpServer server = serverFactory.createServer();
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		File myFile = new File("users.properties");
		if(myFile.exists()){
			userManagerFactory.setFile(myFile);
			serverFactory.setUserManager(userManagerFactory.createUserManager());
			// start the server
			System.out.println("server.start");
			server.start();
		}
		else{
			System.out.println("找不到 users.properties");
		}
		

	}

}
