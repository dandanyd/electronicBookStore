package com.yindan.bookstore.stfp.strategy;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException;

public interface Strategy<T> {
    void execute(ChannelSftp channel, File localFilePath, String remoteFilePath) throws SftpException;

    void execute(ChannelSftp channel, String remoteFilePath) throws SftpException;

    void execute(ChannelSftp channel, String localFilePath ,String remoteFilePath) throws SftpException;

    void excelSftpexecute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException, IOException;

    void excelSftpexecute(ChannelSftp channel, String remoteFilePath) throws SftpException, IOException;

}
