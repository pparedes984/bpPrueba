package com.bp.service.AccountTransaction.controller;

import com.bp.service.AccountTransaction.model.Transaction;
import com.bp.service.AccountTransaction.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movimientos")
public class TransactionController {
    private static final Logger logger = LogManager.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        logger.debug("Handling GET request to /movimientos");
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        logger.debug("Handling GET request to /movimientos/{}", id);
        Transaction transaction =  transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long accountId, @RequestBody Transaction transaction) {
        logger.debug("Handling POST request to /movimientos");
        Transaction newTransaction = transactionService.saveTransaction(accountId, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id, @RequestBody Transaction transactionDetails) {
        logger.debug("Handling PUT request to /movimientos/{}", id);
        Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        logger.debug("Handling DELETE request to /movimientos/{}", id);
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
