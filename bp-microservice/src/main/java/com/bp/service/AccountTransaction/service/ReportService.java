package com.bp.service.AccountTransaction.service;

import com.bp.service.AccountTransaction.model.Account;
import com.bp.service.AccountTransaction.model.Transaction;
import com.bp.service.AccountTransaction.model.dto.AccountReportDTO;
import com.bp.service.AccountTransaction.model.dto.ReportResponse;
import com.bp.service.AccountTransaction.repository.AccountRepository;
import com.bp.service.AccountTransaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public ReportService(){

    }

    public ReportResponse generateReport(String startDate, String endDate) {
        // Parsear las fechas de entrada
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        // Obtener todas las cuentas
        List<Account> accounts = accountRepository.findAll();
        List<AccountReportDTO> accountReports = new ArrayList<>();
        for (Account account : accounts) {
            List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(account.getId(), start, end);
            accountReports.add(new AccountReportDTO(account, transactions));
        }

        return new ReportResponse(start, end, accountReports);
    }
}






