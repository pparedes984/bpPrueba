package com.bp.service.AccountTransaction.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ReportResponse {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<AccountReportDTO> accountReports;

    public ReportResponse(LocalDateTime startDate, LocalDateTime endDate, List<AccountReportDTO> accountReports) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountReports = accountReports;
    }


    // toString (opcional)
    @Override
    public String toString() {
        return "ReportResponse{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", accountReports=" + accountReports +
                '}';
    }
}
