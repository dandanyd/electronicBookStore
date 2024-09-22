package com.yindan.bookstore.stfp.manager;

import com.jcraft.jsch.*;

import java.util.Properties;

public class SftpConnectionManager {

    //SFTP连接管理对象
    private static volatile SftpConnectionManager instance;
    //使用jsch
    private ChannelSftp sftpChannel;
    private Session session;

    //Sftp信息
    private SftpConnectionManager() throws JSchException {
        JSch jsch = new JSch();
        // 设置支持的加密算法
        java.util.Properties config = new java.util.Properties();
        config.put("cipher.s2c", "aes256-ctr,aes192-ctr,aes128-ctr");
        config.put("cipher.c2s", "aes256-ctr,aes192-ctr,aes128-ctr");
        config.put("mac.s2c", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
        config.put("mac.c2s", "hmac-sha2-512-etm@openssh.com,hmac-sha2-256-etm@openssh.com");
        config.put("kex", "diffie-hellman-group-exchange-sha256");
        session = jsch.getSession("uftp01", "192.168.1.10", 22); // 填充实际用户名
        session.setPassword("uftp0101"); // 填充实际密码
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

    public ChannelSftp getSftpChannel() {
        return sftpChannel;
    }



}

