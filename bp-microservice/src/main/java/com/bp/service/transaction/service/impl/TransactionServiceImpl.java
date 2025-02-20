package com.bp.service.transaction.service.impl;

import com.bp.service.account.model.Account;
import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.service.AccountService;
import com.bp.service.transaction.model.Transaction;
import com.bp.service.transaction.model.dto.TransactionDTO;
import com.bp.service.transaction.model.TransactionType;
import com.bp.service.transaction.repository.TransactionRepository;
import com.bp.service.transaction.service.TransactionService;
import com.bp.service.exception.InactiveAccountException;
import com.bp.service.exception.ResourceNotFoundException;
import com.bp.service.exception.SaldoInsuficienteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {


    private final AccountService accountService;

    private final TransactionRepository transactionRepository;

    @Transactional
    public List<TransactionDTO> getAllTransactions() {
        log.info("Obteniendo todos los movimientos");
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO getTransactionById(Long id) {
        log.info("Obteniendo movimiento con ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Movimiento con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Movimiento no encontrado");
                });
        return convertToDTO(transaction);

    }

    @Transactional
    public TransactionDTO saveTransaction(Long accountId, TransactionDTO transactionDTO) {
        log.info("Creando movimiento asociado al numero de cuenta {}", accountId);
        AccountDTO account = accountService.getAccountById(accountId);
        validateAccountIsActive(account);



        Transaction transaction = convertToEntity(transactionDTO);
        transaction.setDate(LocalDateTime.now());
        transaction.setAccountId(account.getId());
        transaction.setBalance(calculateNewBalance(account.getBalance(), transaction));
        account.setBalance(transaction.getBalance());



        accountService.updateAccount(accountId, account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDetails) {
        log.info("Actualizando movimiento con ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        AccountDTO accountDTO = accountService.getAccountById(transaction.getAccountId());
        double saldoOriginal = revertOldTransaction(transaction, accountDTO.getBalance());

        if (transactionDetails.getDate() != null)
            transaction.setDate(transactionDetails.getDate());
        if (transactionDetails.getTransactionType() != null)
            transaction.setTransactionType(TransactionType.valueOf(transactionDetails.getTransactionType().toUpperCase()));
        if (transactionDetails.getValue() != null)
            transaction.setValue(transactionDetails.getValue());

        transaction.setBalance(calculateNewBalance(saldoOriginal, transaction));
        accountDTO.setBalance(transaction.getBalance());
        accountService.updateAccount(transaction.getAccountId(), accountDTO);

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return convertToDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Eliminando movimiento con ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Movimiento con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Movimiento no encontrado");
                });

        transactionRepository.delete(transaction);
    }

    private void validateAccountIsActive(AccountDTO account) {
        if ("INACTIVA".equals(account.getState())) {
            throw new InactiveAccountException("La cuenta está inactiva. No se puede procesar la transacción.");
        }
    }

    private double calculateNewBalance(double currentBalance, Transaction transaction) {
        if (transaction.getTransactionType() == TransactionType.CREDITO) {
            return currentBalance + transaction.getValue();
        } else {
            if (currentBalance < transaction.getValue()) {
                log.error("Saldo insuficiente para la transacción");
                throw new SaldoInsuficienteException("Saldo insuficiente para la transacción");
            }
            return currentBalance - transaction.getValue();
        }
    }

    private double revertOldTransaction(Transaction transaction, double currentBalance) {
        if (transaction.getTransactionType() == TransactionType.CREDITO) {
            return currentBalance - transaction.getValue();
        } else {
            return currentBalance + transaction.getValue();
        }
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getDate(),
                transaction.getTransactionType().name(),
                transaction.getValue(),
                transaction.getBalance(),
                transaction.getAccountId()
        );
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setDate(transactionDTO.getDate());
        transaction.setValue(transactionDTO.getValue());
        transaction.setBalance(transactionDTO.getBalance());
        transaction.setTransactionType(TransactionType.valueOf(transactionDTO.getTransactionType().toUpperCase()));

        return transaction;
    }

    private AccountDTO convertToDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getBalance(),
                account.getState().name(),
                account.getClientId()

        );
    }
}
