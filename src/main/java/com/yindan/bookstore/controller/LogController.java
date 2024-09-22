package com.yindan.bookstore.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
}

