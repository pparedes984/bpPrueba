package com.bp.service.transaction.service.impl;

import com.bp.service.account.model.Account;
import com.bp.service.account.model.accountState;
import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.service.AccountService;
import com.bp.service.account.service.impl.AccountServiceImpl;
import com.bp.service.transaction.model.Transaction;
import com.bp.service.transaction.model.dto.TransactionDTO;
import com.bp.service.transaction.model.transactionType;
import com.bp.service.transaction.repository.TransactionRepository;
import com.bp.service.transaction.service.TransactionService;
import com.bp.service.exception.InactiveAccountException;
import com.bp.service.exception.ResourceNotFoundException;
import com.bp.service.exception.SaldoInsuficienteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Verificar si la cuenta está ACTIVA antes de procesar la transacción
        if (account.getState().equals("INACTIVA")) {
            log.error("La cuenta {} está inactiva. No se puede realizar la transacción.", accountId);
            throw new InactiveAccountException("La cuenta está inactiva. No se puede procesar la transacción.");
        }

        Transaction transaction = convertToEntity(transactionDTO);
        transaction.setDate(LocalDateTime.now());
        if(transaction.getTransactionType().equals(transactionType.CREDITO)) {
            transaction.setBalance(account.getBalance() + (transaction.getValue()));
        } else {
            if (account.getBalance() < transaction.getValue()) {
                log.error("Saldo no disponible");
                throw new SaldoInsuficienteException("Saldo no disponible");
            }
            transaction.setBalance(account.getBalance() - transaction.getValue());

        }

        transaction.setAccountId(account.getId());
        account.setBalance(transaction.getBalance());

        accountService.updateAccount(accountId, account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDetails) {
        log.info("Actualizando movimiento con ID: {}", id);
        Transaction transaction =  transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        if (transactionDetails.getDate() != null)
            transaction.setDate(transactionDetails.getDate());
        if (transactionDetails.getTransactionType() != null)
            transaction.setTransactionType(transactionType.valueOf(transactionDetails.getTransactionType().toUpperCase()));
        if (transactionDetails.getBalance() != null)
            transaction.setBalance(transactionDetails.getBalance());
        if (transactionDetails.getValue() != null)
            transaction.setValue(transactionDetails.getValue());

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
        transaction.setTransactionType(transactionType.valueOf(transactionDTO.getTransactionType().toUpperCase()));

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
