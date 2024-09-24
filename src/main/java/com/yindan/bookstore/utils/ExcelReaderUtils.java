package com.yindan.bookstore.utils;

import com.yindan.bookstore.entity.BookEntity;
import com.yindan.bookstore.entity.ReportDetailsEntity;
import com.yindan.bookstore.entity.ReportEntity;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ExcelReaderUtils {
    public Pair<List<ReportEntity>, List<ReportDetailsEntity>> readExcel(String filePath) throws IOException {
        List<ReportDetailsEntity> reportDetailsEntityList = new ArrayList<>();
        List<ReportEntity> reportEntityList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // 假设第一行为标题行
            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                Iterator<Cell> cellIterator = headerRow.cellIterator();
                List<String> headers = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    headers.add(cell.getStringCellValue());
                }
                int rowIndex = 1;
                // 从第二行开始读取数据
                for (rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null && row.cellIterator().hasNext()) {
                        Iterator<Cell> iterator = row.cellIterator();
                        ReportEntity entity = new ReportEntity();
                        int i = 0;
                        while (iterator.hasNext() && i < headers.size()) {
                            Cell cell = iterator.next();
                            String header = headers.get(i++);
                            Object value = getValueFromCell(cell);
                            switch (header) {
                                case "类型":
                                    entity.setType((String) value);
                                    break;
                                case "书籍分类":
                                    entity.setCategory ((String) value);
                                    break;
                                case "数量":
                                    //entity.setQuantity(Integer.valueOf(value.toString()));
                                    if (value instanceof Double) {
                                        // 将 Double 转换为 int 并设置给 entity
                                        entity.setQuantity(((Double) value).intValue());
                                    } else {
                                        // 如果 value 不是 Double 类型，可以在这里添加相应的错误处理逻辑
                                        throw new IllegalArgumentException("Expected a Double, but got: " + value.getClass().getName());
                                    }
                                    break;
                            }

                        }
                        reportEntityList.add(entity);
                    }else{
                        break;
                    }
                    /*
                    Map<String, Object> salesMap = new HashMap<>();
                    if (row != null && row.cellIterator().hasNext()) {
                        Iterator<Cell> iterator = row.cellIterator();
                        int i = 0;
                        while (iterator.hasNext() && i < headers.size()) {
                            Cell cell = iterator.next();
                            String header = headers.get(i++);
                            Object value = getValueFromCell(cell);
                            switch (header) {
                                case "类型":
                                    String type = (String) value;
                                    salesMap.put(header, type);
                                    break;
                                case "书籍分类":
                                    String category = (String) value;
                                    salesMap.put(header, category);
                                    break;
                                case "数量":
                                    String sum = String.valueOf(value);
                                    salesMap.put(header, sum);
                                    break;
                            }

                        }
                        list.add(salesMap);
                    }else{
                        break;
                    }
                    */
                }

                Row headerRow1 = sheet.getRow(rowIndex + 1);
                if (headerRow1 != null) {
                    Iterator<Cell> cellIterator1 = headerRow1.cellIterator();
                    List<String> headers1 = new ArrayList<>();
                    while (cellIterator1.hasNext()) {
                        Cell cell1 = cellIterator1.next();
                        headers1.add(cell1.getStringCellValue());
                    }
                for (int bookRowIndex = rowIndex + 2; bookRowIndex <= sheet.getLastRowNum(); bookRowIndex++) {
                    Row row = sheet.getRow(bookRowIndex);
                    if (row != null) {
                        Iterator<Cell> iterator = row.cellIterator();
                        ReportDetailsEntity entity = new ReportDetailsEntity();
                        int i = 0;
                        while (iterator.hasNext() && i < headers1.size()) {
                            Cell cell = iterator.next();
                            String header = headers1.get(i++);
                            Object value = getValueFromCell(cell);

                            if (null != value && !value.toString().isEmpty()){
                                switch (header) {
                                    case "书籍ID":
                                        //entity.setBookId(Long.valueOf((String) value) );
                                        if (value instanceof Double) {
                                            // 将 Double 转换成 long，然后包装成 Long 对象
                                            entity.setBookId(((Double) value).longValue());
                                        } else {
                                            // 如果 value 不是 Double 类型，抛出异常或进行其他处理
                                            throw new IllegalArgumentException("Expected a Double, but got: " + (value != null ? value.getClass().getName() : "null"));
                                        }
                                        break;
                                    case "书名":
                                        entity.setTitle((String) value);
                                        break;
                                    case "作者":
                                        entity.setAuthor((String) value);
                                        break;
                                    case "ISBN":
                                        entity.setIsbn((String) value);
                                        break;
                                    case "借阅/销售日期":
                                        Date borrowSaleDate = dateFromExcelSerialNumber((Double) value);
                                        entity.setTransactionDate(borrowSaleDate);
                                        break;
                                    case "应还日期":
                                        Date dueDate = dateFromExcelSerialNumber((Double) value);
//                                    LocalDate localDate = dueDate.toInstant()
//                                            .atZone(ZoneId.systemDefault())
//                                            .toLocalDate();
//                                    entity.setBorrowDate(localDate);
//                                    break;
                                        entity.setDueDate(dueDate);
                                        break;
                                    case "剩余天数":
                                        entity.setDaysRemaining(Integer.valueOf((String) value));
                                        break;
                                    case "借阅人/购买人":
                                        entity.setUserName((String) value);
                                        break;
                                    case "联系方式":
                                        entity.setContactInfo((String) value);
                                        break;
                                    case "类型":
                                        entity.setTransactionType((String) value);
                                        break;

                                }

                            }

                        }
                        reportDetailsEntityList.add(entity);

                    }
                }
                }
            }
        }


        return new Pair<List<ReportEntity>, List<ReportDetailsEntity>>(reportEntityList,reportDetailsEntityList );
    }

    /**
     * 将Excel的日期序列号转换为Java.util.Date对象。
     *
     * @param serialNumber Excel中的日期序列号。
     * @return 对应的Date对象。
     */
    private static Date dateFromExcelSerialNumber(double serialNumber) {
        // Excel的日期序列号是从1900年1月1日开始的
        // 但是Java的Date类是从1970年1月1日开始的
        // 因此需要加上基础偏移量
        long baseDate = 25569; // 1970年1月1日相对于1900年1月1日在Excel中的天数
        long days = (long) serialNumber;
        double fractionOfDay = serialNumber - days;
        long milliseconds = (long) (fractionOfDay * 24 * 60 * 60 * 1000);

        // 计算日期
        long totalMilliseconds = (days - baseDate) * 24 * 60 * 60 * 1000 + milliseconds;
        return new Date(totalMilliseconds);
    }

    private Object getValueFromCell(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

    private Workbook getWorkbook(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            ///return new XSSFWorkbook(fis);
           return  WorkbookFactory.create(fis);
        }
    }
}
