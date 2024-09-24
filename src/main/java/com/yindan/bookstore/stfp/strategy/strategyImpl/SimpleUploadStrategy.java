package com.yindan.bookstore.stfp.strategy.strategyImpl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.dao.BorrowingDao;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.stfp.strategy.Strategy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SimpleUploadStrategy implements Strategy {

    @Autowired
    private BorrowingDao borrowingDao;
  /*  @Override
    public void upload(ChannelSftp channel, File file, String remotePath) throws IOException, SftpException {
        // 尝试打开文件输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // 使用 SFTP 通道上传文件
            channel.put(fis, remotePath + "/" + file.getName());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getPath());
            e.printStackTrace();
        } catch (SftpException e) {
            System.err.println("Error uploading file to SFTP server.");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream.");
                    e.printStackTrace();
                }
            }
        }
    }
*/
    @Override
    public void execute(ChannelSftp channel, File file, String remoteFilePath) throws SftpException {
        // 尝试打开文件输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // 使用 SFTP 通道上传文件
            channel.put(fis, remoteFilePath + "/" + file.getName());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getPath());
            e.printStackTrace();
        } catch (SftpException e) {
            System.err.println("Error uploading file to SFTP server.");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream.");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException, IOException {
        generateExcelFile(localFilePath);
        try {
            channel.put(localFilePath, remoteFilePath);
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void excelSftpexecute(ChannelSftp channel, String remoteFilePath) throws SftpException, IOException {

    }

    @Override
    public void execute(ChannelSftp channel, String remoteFilePath) throws SftpException {

    }

    @Override
    public void execute(ChannelSftp channel, String localFilePath, String remoteFilePath) throws SftpException {

    }

    //生成excel
    public void generateExcelFile(String filePath) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormat.format(new Date());


        // 创建工作表
        Sheet sheet = workbook.createSheet(nowDate + " Daily Summary and Details");

        // 设置日期格式
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // 创建一个带有红色背景的单元格样式
        CellStyle redCellStyle = workbook.createCellStyle();
        redCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        List<ReportDto> reports = borrowingDao.getReports();
        List<ReportDetailsDto> reportDetails = borrowingDao.getReportDetails();

        // 创建汇总部分
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("类型");
        headerRow.createCell(1).setCellValue("书籍分类");
        headerRow.createCell(2).setCellValue("数量");

        int sumCount = 1;
        // 添加汇总数据
        for (ReportDto report : reports) {
            Row summaryRow1 = sheet.createRow(sumCount ++);
            summaryRow1.createCell(0).setCellValue(report.getType());
            summaryRow1.createCell(1).setCellValue(report.getCategory());
            summaryRow1.createCell(2).setCellValue(report.getQuantity());
        }


        // 创建空行以分隔汇总和详细清单

        sheet.createRow(sumCount++);

        // 创建详细清单部分
        Row detailsHeaderRow = sheet.createRow(sumCount ++);
        detailsHeaderRow.createCell(0).setCellValue("书籍ID");
        detailsHeaderRow.createCell(1).setCellValue("书名");
        detailsHeaderRow.createCell(2).setCellValue("作者");
        detailsHeaderRow.createCell(3).setCellValue("ISBN");
        detailsHeaderRow.createCell(4).setCellValue("借阅/销售日期");
        detailsHeaderRow.createCell(5).setCellValue("应还日期");
        detailsHeaderRow.createCell(6).setCellValue("剩余天数");
        detailsHeaderRow.createCell(7).setCellValue("借阅人/购买人姓名");
        detailsHeaderRow.createCell(8).setCellValue("联系方式");
        detailsHeaderRow.createCell(9).setCellValue("类型");

        // 添加详细清单数据
        for (ReportDetailsDto reportDetail : reportDetails) {
            Row detailRow1 = sheet.createRow(sumCount ++);
            detailRow1.createCell(0).setCellValue(reportDetail.getBookId());
            detailRow1.createCell(1).setCellValue(reportDetail.getTitle());
            detailRow1.createCell(2).setCellValue(reportDetail.getAuthor());
            detailRow1.createCell(3).setCellValue(reportDetail.getIsbn());
            Cell cell = detailRow1.createCell(4);
            cell.setCellValue(reportDetail.getBorrowSaleDate());
            cell.setCellStyle(dateCellStyle);
            Cell cell2 = detailRow1.createCell(5);
            cell2.setCellValue(reportDetail.getDueDate());
            cell2.setCellStyle(dateCellStyle);
            detailRow1.createCell(6).setCellValue(null == reportDetail.getDiffDate() ? "" : reportDetail.getDiffDate().toString());
            detailRow1.createCell(7).setCellValue(reportDetail.getUsername());
            detailRow1.createCell(8).setCellValue(reportDetail.getPhoneNumber());
            detailRow1.createCell(9).setCellValue(reportDetail.getType());
            if (null != reportDetail.getDiffDate() && reportDetail.getDiffDate() <= 2) {
                for (Cell cell3 : detailRow1) {
                    // 克隆红色样式以避免覆盖日期格式
                    CellStyle clonedRedCellStyle = workbook.createCellStyle();
                    clonedRedCellStyle.cloneStyleFrom(redCellStyle);
                    if (cell3.getColumnIndex() == 4 || cell3.getColumnIndex() == 5|| cell3.getColumnIndex() == 6) {
                        // 保留日期格式
                        clonedRedCellStyle.setDataFormat(cell3.getCellStyle().getDataFormat());
                    }
                    cell3.setCellStyle(clonedRedCellStyle);
                }
            }

        }


        // 自动调整列宽
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入Excel文件
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
