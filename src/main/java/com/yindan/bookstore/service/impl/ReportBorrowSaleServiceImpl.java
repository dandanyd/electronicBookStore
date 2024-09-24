package com.yindan.bookstore.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.yindan.bookstore.dao.BorrowingDao;
import com.yindan.bookstore.dao.ReportDao;
import com.yindan.bookstore.dao.ReportDetailsDao;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.entity.ReportDetailsEntity;
import com.yindan.bookstore.entity.ReportEntity;
import com.yindan.bookstore.service.ReportBorrowSaleService;
import com.yindan.bookstore.stfp.manager.SftpConnectionManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReportBorrowSaleServiceImpl implements ReportBorrowSaleService {

    @Autowired
    private BorrowingDao borrowingDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private ReportDetailsDao reportDetailsDao;


    public void reportData(HttpServletResponse response){
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

//        // 保存文件
//        try (FileOutputStream fileOut = new FileOutputStream("/Users/yindandan/Desktop/renren/daily_report.xlsx")) {
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            workbook.write(excelContent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + nowDate + "-dailyReport.xlsx");
            response.setContentType("application/msexcel");
            workbook.write(output);
            workbook.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /*
    public void reportDataAuto() throws IOException {
        ChannelSftp channel = sftpManager.getSftpChannel();

        //临时文件
        File tempFile = File.createTempFile("tempExcel", ".xlsx");
        String filePath = tempFile.getAbsolutePath();

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

        try {
            channel.put(filePath, "/data/sftp/uftp01/upload/");
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
          //  channel.disconnect();
        }

    }

     */

    public void addReport(List<ReportEntity> reportEntities, List<ReportDetailsEntity> reportDetailsEntityList){
        for (ReportEntity reportEntity : reportEntities) {
            reportDao.insert(reportEntity);
        }
        for (ReportDetailsEntity entity : reportDetailsEntityList) {
            reportDetailsDao.insert(entity);
        }
    }
}
