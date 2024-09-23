package com.yindan.bookstore.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LogController {

    @GetMapping("/log")
    public String getLog(Model model) {
        List<String> logLines = new ArrayList<>();
        try {
            // 假设日志文件位于类路径下的 "logs" 文件夹中
            Resource resource = new ClassPathResource("logs/app.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logLines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error reading the log file: " + e.getMessage());
        }

//        model.addAttribute("logLines", logLines);
//        return "logView"; // 返回 Thymeleaf 模板
        return "";
    }


//    @ResponseBody
//    @GetMapping("/getFileLog")
//    public List<String> getFileLog() {
//        File file = new File("file.log");
//        List<String> lines = new ArrayList<>();
//        int maxLines = 50; // 最大读取行数
//        int lineCount = 0; // 当前行计数
//
//        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
//            String line;
//            while ((line = reader.readLine()) != null && lineCount < maxLines) {
//                lines.add(line);
//                lineCount++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to read file content.", e);
//        }
//
//        return lines;
//    }



    @ResponseBody
    @RequestMapping(value = "/getFileLog", method = RequestMethod.GET)
    public List<String> readLogFile() {
        int bufferSize = 1024 * 1024; // 1MB 缓冲区大小
        LinkedList<String> lastLines = new LinkedList<>();
        Pattern datePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}"); // 匹配日期格式

        try (BufferedReader reader = new BufferedReader(new FileReader("file.log"), bufferSize)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastLines.addLast(line);
            }
            // 确保列表大小不超过50
            while (lastLines.size() > 50) {
                lastLines.removeFirst();
            }
            // 处理日期行
            List<String> processedLines = new ArrayList<>();
            for (String currentLine : lastLines) {
                Matcher matcher = datePattern.matcher(currentLine);
                if (matcher.find()) {
                    // 添加换行符
                    processedLines.add("\n" + currentLine);
                } else {
                    processedLines.add(currentLine);
                }
            }
            return processedLines;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}

