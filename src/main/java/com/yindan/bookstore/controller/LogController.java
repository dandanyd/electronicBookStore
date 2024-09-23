package com.yindan.bookstore.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @ResponseBody
    @GetMapping("/getFileLog")
    public List<String> getFileLog() {
        File file = new File("file.log");
        List<String> lines = new ArrayList<>();
        int maxLines = 50; // 最大读取行数
        int lineCount = 0; // 当前行计数

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null && lineCount < maxLines) {
                lines.add(line);
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file content.", e);
        }

        return lines;
    }

}

