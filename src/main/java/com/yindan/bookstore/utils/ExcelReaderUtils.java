package com.yindan.bookstore.utils;

import com.yindan.bookstore.entity.BookEntity;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ExcelReaderUtils {
    public Pair<List<Map<String, Object>>, List<BookEntity>> readExcel(String filePath) throws IOException {
        List<BookEntity> books = new ArrayList<>();
        List list = new ArrayList();
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
                    Map<String, Object> salesMap = new HashMap<>();
                    if (row != null) {
                        Iterator<Cell> iterator = row.cellIterator();
                        int i = 0;
                        while (iterator.hasNext() && i < headers.size()) {
                            Cell cell = iterator.next();
                            String header = headers.get(i++);
                            Object value = getValueFromCell(cell);
                            switch (header) {
                                case "借阅/销售":
                                    String type = (String) value;
                                    salesMap.put(header, type);
                                    break;
                                case "类型":
                                    String genre = (String) value;
                                    salesMap.put(header, genre);
                                    break;
                                case "汇总":
                                    String sum = String.valueOf(value);
                                    salesMap.put(header, sum);
                                    break;
                            }

                        }
                        list.add(salesMap);
                    }else{
                        break;
                    }
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
                        BookEntity book = new BookEntity();
                        int i = 0;
                        while (iterator.hasNext() && i < headers1.size()) {
                            Cell cell = iterator.next();
                            String header = headers1.get(i++);
                            Object value = getValueFromCell(cell);

                            switch (header) {
                                case "书籍":
                                    book.setTitle((String) value);
                                    break;
                                case "作者":
                                    book.setAuthor((String) value);
                                    break;
//                                case "年份":
//                                    if (value instanceof Double) {
//                                        Double doubleValue = (Double) value;
//                                        // 使用 Math.round() 进行四舍五入转换
//                                        int intValue = (int) Math.round(doubleValue);
//                                        book.setYear(intValue);
//                                    }
//                                    break;
//                                case "借阅人":
//                                    book.setBorrower((String) value);
//                                    break;
//                                case "时间":
//                                    Date javaUtilDate = dateFromExcelSerialNumber((Double) value);
//                                    LocalDate localDate = javaUtilDate.toInstant()
//                                            .atZone(ZoneId.systemDefault())
//                                            .toLocalDate();
//                                    book.setBorrowDate(localDate);
//                                    break;
                            }

                        }
                        books.add(book);

                    }
                }
                }
            }
        }


        return new Pair<List<Map<String, Object>>, List<BookEntity>>(list, books);
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
