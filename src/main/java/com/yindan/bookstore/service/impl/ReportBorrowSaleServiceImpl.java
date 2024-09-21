package com.yindan.bookstore.service.impl;

import com.yindan.bookstore.dao.BorrowingDao;
import com.yindan.bookstore.dto.ReportDetailsDto;
import com.yindan.bookstore.dto.ReportDto;
import com.yindan.bookstore.service.ReportBorrowSaleService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReportBorrowSaleServiceImpl implements ReportBorrowSaleService {

    @Autowired
    private BorrowingDao borrowingDao;

    public void reportData(){
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

        // 保存文件
        try (FileOutputStream fileOut = new FileOutputStream("/Users/yindandan/Desktop/renren/daily_report.xlsx")) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭工作簿
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
