package com.bp.service.AccountTransaction.service;

import com.bp.service.AccountTransaction.model.Account;
import com.bp.service.AccountTransaction.model.Transaction;
import com.bp.service.AccountTransaction.model.accountState;
import com.bp.service.AccountTransaction.repository.TransactionRepository;
import com.bp.service.exception.InactiveAccountException;
import com.bp.service.exception.ResourceNotFoundException;
import com.bp.service.exception.SaldoInsuficienteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);


    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;


    public List<Transaction> getAllTransactions() {
        logger.info("Obteniendo todos los movimientos");
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        logger.info("Obteniendo movimiento con ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Movimiento con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Movimiento no encontrado");
                });
    }

    public Transaction saveTransaction(Long accountId, Transaction transaction) {
        logger.info("Creando movimiento asociado al numero de cuenta {}", accountId);
        Account account = accountService.getAccountById(accountId);
        // Verificar si la cuenta está ACTIVA antes de procesar la transacción
        if (account.getState() == accountState.INACTIVA) {
            logger.error("La cuenta {} está inactiva. No se puede realizar la transacción.", accountId);
            throw new InactiveAccountException("La cuenta está inactiva. No se puede procesar la transacción.");
        }

        transaction.setDate(LocalDateTime.now());
        if(transaction.getTransactionType().equals("CREDITO")) {
            transaction.setBalance(account.getOpeningBalance() + (transaction.getValue()));
        } else {
            if (account.getOpeningBalance() < transaction.getValue()) {
                logger.error("Saldo no disponible");
                throw new SaldoInsuficienteException("Saldo no disponible");
            }
            transaction.setBalance(account.getOpeningBalance() - transaction.getValue());

        }

        transaction.setAccountId(account.getId());
        account.setOpeningBalance(transaction.getBalance());

        accountService.updateAccount(accountId, account);

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        logger.info("Actualizando movimiento con ID: {}", id);
        Transaction transaction =  transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        if (transactionDetails.getDate() != null)
            transaction.setDate(transactionDetails.getDate());
        if (transactionDetails.getTransactionType() != null)
            transaction.setTransactionType(transactionDetails.getTransactionType());
        if (transactionDetails.getBalance() != null)
            transaction.setBalance(transactionDetails.getBalance());
        if (transactionDetails.getValue() != null)
            transaction.setValue(transactionDetails.getValue());

        return transactionRepository.save(transaction);

    }

    public void deleteTransaction(Long id) {
        logger.info("Eliminando movimiento con ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Movimiento con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Movimiento no encontrado");
                });

        transactionRepository.delete(transaction);
    }
}
