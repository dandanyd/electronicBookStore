package com.yindan.bookstore.stfp.strategy;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Strategy {
    void execute(ChannelSftp channel, File localFilePath, String remoteFilePath) throws SftpException;

    void execute(ChannelSftp channel, String remoteFilePath) throws SftpException;

    void execute(ChannelSftp channel, String localFilePath ,String remoteFilePath) throws SftpException;

    void excelSftpexecute(ChannelSftp channel, String localFilePath, String remoteFilePath, List<ReportDto> reports , List<ReportDetailsDto> reportDetails) throws SftpException, IOException;

    void excelSftpexecute(ChannelSftp channel, String remoteFilePath) throws SftpException, IOException;

}
