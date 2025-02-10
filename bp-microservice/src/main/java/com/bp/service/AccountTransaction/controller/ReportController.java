package com.bp.service.AccountTransaction.controller;

import com.bp.service.AccountTransaction.model.dto.ReportResponse;
import com.bp.service.AccountTransaction.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
