package com.bp.service.transaction.controller;

import com.bp.service.transaction.model.dto.TransactionDTO;
import com.bp.service.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("api/movimientos")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        log.info("Handling GET request to /movimientos");
        try {
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving transactions: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        log.info("Handling GET request to /movimientos/{}", id);
        try {
            TransactionDTO transaction =  transactionService.getTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving transaction with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/{accountId}")
    public ResponseEntity<TransactionDTO> createTransaction(
            @PathVariable Long accountId, @RequestBody TransactionDTO transaction) {
        log.info("Handling POST request to /movimientos");
        try {
            TransactionDTO newTransaction = transactionService.saveTransaction(accountId, transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id, @RequestBody TransactionDTO transactionDetails) {
        log.info("Handling PUT request to /movimientos/{}", id);
        try {
            TransactionDTO updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating transaction with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.info("Handling DELETE request to /movimientos/{}", id);
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting transaction with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
