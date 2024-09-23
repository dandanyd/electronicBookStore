package com.yindan.bookstore.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportBorrowSaleService{

    void reportData(HttpServletResponse response);

    void reportDataAuto() throws IOException;

}
