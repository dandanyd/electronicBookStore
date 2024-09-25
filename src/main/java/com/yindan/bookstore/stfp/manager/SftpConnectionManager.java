package com.yindan.bookstore.stfp.manager;

import com.jcraft.jsch.*;
import com.yindan.bookstore.constant.SftpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SftpConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger("FILE");

    //SFTP连接管理对象
    private static volatile SftpConnectionManager instance;
    //使用jsch
    private ChannelSftp sftpChannel;

    private Session session;


    //Sftp信息
    private SftpConnectionManager() throws JSchException {
       /* JSch jsch = new JSch();
        // 设置支持的加密算法
        java.util.Properties config = new java.util.Properties();
        config.put("cipher.s2c", "aes256-ctr,aes192-ctr,aes128-ctr");
        config.put("cipher.c2s", "aes256-ctr,aes192-ctr,aes128-ctr");
        config.put("mac.s2c", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
        config.put("mac.c2s", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
        config.put("kex", "diffie-hellman-group-exchange-sha256");
        //session = jsch.getSession("uftp01", "192.168.1.10", 22); // 填充实际用户名
        session = jsch.getSession(SftpConstant.USER_NAME, SftpConstant.HOST, SftpConstant.PORT); // 填充实际用户名
        session.setPassword(SftpConstant.PASSWORD); // 填充实际密码
        //session.setConfig("kex","diffie-hellman-group1-sha1");
        session.setConfig("StrictHostKeyChecking", "no");
//        Properties config = new Properties();
//        config.put("StrictHostKeyChecking", "no"); // 不验证 HostKey
//        config.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group-exchange-sha1,"
//                + "diffie-hellman-group-exchange-sha256");
        //session.setConfig(config);
        session.setTimeout(30000); // 设置超时时间为30秒
        session.connect();
        sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();

        */
    }

    //使用单例模式初始化SftpConnectionManager
    public static SftpConnectionManager getInstance() throws JSchException {
        if (instance == null) {
            synchronized (SftpConnectionManager.class) {
                if (instance == null) {
                    instance = new SftpConnectionManager();
                }
            }
        }
        return instance;
    }



    public  void close() throws SftpException {
        if (sftpChannel != null && sftpChannel.isConnected()) {
            sftpChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

//    public ChannelSftp getSftpChannel() {
//        return sftpChannel;
//    }

    public synchronized ChannelSftp getSftpChannel() throws JSchException {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            initializeConnection();
        }
        return sftpChannel;
    }

    private synchronized void initializeConnection() throws JSchException {
        if (sftpChannel == null || !sftpChannel.isConnected()) {
            JSch jsch = new JSch();
            // 设置支持的加密算法
            java.util.Properties config = new java.util.Properties();
            config.put("cipher.s2c", "aes256-ctr,aes192-ctr,aes128-ctr");
            config.put("cipher.c2s", "aes256-ctr,aes192-ctr,aes128-ctr");
            config.put("mac.s2c", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
            config.put("mac.c2s", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
            config.put("kex", "diffie-hellman-group-exchange-sha256");
            config.put("StrictHostKeyChecking", "no");

            try {
                // 创建会话
                session = jsch.getSession(SftpConstant.USER_NAME, SftpConstant.HOST, SftpConstant.PORT);
                session.setPassword(SftpConstant.PASSWORD);
                session.setConfig(config);
                session.setTimeout(30000); // 设置超时时间为30秒
                session.connect();
                logger.info("SFTP session connected to {}:{}", SftpConstant.HOST, SftpConstant.PORT);

                // 打开SFTP通道
                sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect(); // 连接SFTP通道
                logger.info("SFTP channel opened successfully.");
            } catch (JSchException e) {
                logger.error("Failed to connect to SFTP server: {}", e.getMessage(), e);
                throw e; // 重新抛出异常，让调用者处理
            }

        }
    }



}

