package com.yindan.bookstore.monitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.stfp.manager.SftpConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class SftpFileMonitorBean {

    @Autowired
    private SftpFileMonitor monitor;

    @PostConstruct
    public void initializeMonitor() {
        try {
            // 获取SFTP连接
//            SftpConnectionManager manager = SftpConnectionManager.getInstance();
//            ChannelSftp channel = manager.getSftpChannel();
//            String remoteDirectory = "/data/sftp/uftp01/upload";

            // 初始化并启动监控器
            //monitor = new SftpFileMonitor(channel, remoteDirectory);
            monitor.startMonitoring();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroyMonitor() throws JSchException, SftpException {
        SftpConnectionManager manager = SftpConnectionManager.getInstance();
        // 应用程序停止时关闭连接
        if (monitor != null) {
            monitor.stopMonitoring();
        }
        manager.close();
    }

}
