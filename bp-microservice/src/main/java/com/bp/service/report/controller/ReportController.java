package com.bp.service.report.controller;

import com.bp.service.report.model.dto.ReportResponse;
import com.bp.service.report.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reportes")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public ReportResponse getReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {


        return reportService.generateReport(startDate, endDate);
    }

    @GetMapping(value = "/pdf", produces = "application/pdf")
    public void getReportPdf(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) {
        reportService.generateReportPdf(startDate, endDate, response);
    }
}
