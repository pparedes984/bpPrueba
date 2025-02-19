package com.bp.service.report.service;

import com.bp.service.account.model.Account;
import com.bp.service.transaction.model.Transaction;
import com.bp.service.report.model.dto.AccountReportDTO;
import com.bp.service.report.model.dto.ReportResponse;
import com.bp.service.account.repository.AccountRepository;
import com.bp.service.transaction.repository.TransactionRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public void generateReportPdf(String startDate, String endDate, HttpServletResponse response) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Agregar Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reporte de Cuentas y Movimientos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Obtener datos
            List<Account> accounts = accountRepository.findAll();

            for (Account account : accounts) {
                document.add(new Paragraph("Cuenta: " + account.getAccountNumber()));
                document.add(new Paragraph("Saldo: " + account.getBalance()));

                List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(
                        account.getId(),
                        LocalDateTime.parse(startDate),
                        LocalDateTime.parse(endDate)
                );

                PdfPTable table = new PdfPTable(3); // 3 Columnas
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);

                // Encabezados
                table.addCell(new PdfPCell(new Phrase("Fecha")));
                table.addCell(new PdfPCell(new Phrase("Tipo")));
                table.addCell(new PdfPCell(new Phrase("Monto")));

                for (Transaction transaction : transactions) {
                    table.addCell(transaction.getDate().toString());
                    table.addCell(transaction.getTransactionType().name());
                    table.addCell(transaction.getBalance().toString());
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            document.close();

            // Configurar la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Reporte.pdf");
            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando el PDF", e);
        }
    }

}






