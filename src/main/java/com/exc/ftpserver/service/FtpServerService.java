 package com.exc.ftpserver.service;

 import java.io.File;
 import java.util.Objects;

 import javax.annotation.PostConstruct;
 import javax.annotation.PreDestroy;

 import org.apache.ftpserver.ConnectionConfigFactory;
 import org.apache.ftpserver.DataConnectionConfigurationFactory;
 import org.apache.ftpserver.FtpServer;
 import org.apache.ftpserver.FtpServerFactory;
 import org.apache.ftpserver.ftplet.FtpException;
 import org.apache.ftpserver.listener.ListenerFactory;
 import org.apache.ftpserver.ssl.SslConfigurationFactory;
 import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.stereotype.Component;

 /**
  * ftp服务器
  * @author huangmin
  * @date 2019/07/10
  */
 @Component
 public class FtpServerService {
     private static final Logger logger = LoggerFactory.getLogger(FtpServerService.class);
     private FtpServer ftpServer;

     @Value("${server.host}")
     private String host;
     @Value("${ftp.port}")
     private int port;
     @Value("${ftp.passive-ports}")
     private String passivePorts;
     @Value("${ftp.max-login}")
     private Integer maxLogin;
     @Value("${ftp.max-threads}")
     private Integer maxThreads;
     @Value("${file.keystore-name}")
     private String keystoreName;
     @Value("${file.user-configuration-name}")
     private String userConfigurationName;


     @PostConstruct
     private void start() {
         FtpServerFactory serverFactory = new FtpServerFactory();

         // FTP服务连接配置
         ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
         connectionConfigFactory.setAnonymousLoginEnabled(false);
         connectionConfigFactory.setMaxLogins(maxLogin);
         connectionConfigFactory.setMaxThreads(maxThreads);
         serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());

         ListenerFactory listenerFactory = new ListenerFactory();
         // 配置FTP端口
         listenerFactory.setPort(port);
         // 被动模式配置(按需)
         if (!Objects.equals(passivePorts, "")) {
             DataConnectionConfigurationFactory dataConnectionConfFactory = new DataConnectionConfigurationFactory();
             dataConnectionConfFactory.setIdleTime(60);
             dataConnectionConfFactory.setImplicitSsl(true);
             dataConnectionConfFactory.setActiveLocalPort(53101);
             
             logger.info("进行被动模式配置, 被动端口号范围:{}", passivePorts);
             dataConnectionConfFactory.setPassivePorts(passivePorts);
             if (!(Objects.equals(host, "localhost") || Objects.equals(host, "127.0.0.1"))) {
                 logger.info("进行被动模式配置,本机地址:{}", host);
                 dataConnectionConfFactory.setPassiveExternalAddress(host);
             }
             listenerFactory.setDataConnectionConfiguration(
                     dataConnectionConfFactory.createDataConnectionConfiguration());
         }
         
         // define SSL configuration
         SslConfigurationFactory ssl = new SslConfigurationFactory();
         ssl.setKeystoreFile(new File(keystoreName));
         ssl.setKeystorePassword("exc-ledznyjy20190710");
         // set the SSL configuration for the listener
         listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
         listenerFactory.setImplicitSsl(false);
         listenerFactory.setIdleTimeout(60);

         // 替换默认监听器
         serverFactory.addListener("default", listenerFactory.createListener());

         // 设置用户控制中心
         PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
         userManagerFactory.setFile(new File(userConfigurationName));
         serverFactory.setUserManager(userManagerFactory.createUserManager());

         // 创建并启动FTP服务
         ftpServer = serverFactory.createServer();
         try {
             ftpServer.start();
         } catch (FtpException e) {
             logger.error("ftp启动异常:", e);
             throw new RuntimeException(e);
         }
         logger.info("ftp启动成功,端口号:" + port);
     }

     @PreDestroy
     private void stop() {
         if (ftpServer != null) {
             logger.warn("ftp停止工作:");
             ftpServer.stop();
         }
     }

 }
