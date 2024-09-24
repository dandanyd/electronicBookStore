package com.yindan.bookstore.service;

import com.yindan.bookstore.entity.ReportDetailsEntity;
import com.yindan.bookstore.entity.ReportEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ReportBorrowSaleService{

    void reportData(HttpServletResponse response);

   // void reportDataAuto() throws IOException;

    void addReport(List<ReportEntity> reportEntities, List<ReportDetailsEntity> reportDetailsEntityList);

}
