package com.bp.service.transaction.service;
import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.service.AccountService;
import com.bp.service.exception.InactiveAccountException;
import com.bp.service.exception.ResourceNotFoundException;
import com.bp.service.transaction.model.Transaction;
import com.bp.service.transaction.model.dto.TransactionDTO;
import com.bp.service.transaction.model.transactionType;
import com.bp.service.transaction.repository.TransactionRepository;
import com.bp.service.transaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(1L, LocalDateTime.now(), transactionType.CREDITO, 100.0, 500.0, 1L);
        transactionDTO = new TransactionDTO(1L, LocalDateTime.now(), "CREDITO", 100.0, 500.0, 1L);
        accountDTO = new AccountDTO(1L, "123456", "AHORROS", 400.0, "ACTIVA", 1L);
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
    }

    @Test
    void testGetTransactionById_Found() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        TransactionDTO result = transactionService.getTransactionById(1L);
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void testSaveTransaction_Success() {
        when(accountService.getAccountById(1L)).thenReturn(accountDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = transactionService.saveTransaction(1L, transactionDTO);
        assertNotNull(result);
        assertEquals(transactionDTO.getValue(), result.getValue());
    }

    @Test
    void testUpdateTransaction_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = transactionService.updateTransaction(1L, transactionDTO);
        assertNotNull(result);
        assertEquals(transactionDTO.getValue(), result.getValue());
    }

    @Test
    void testUpdateTransaction_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(1L, transactionDTO));
    }

    @Test
    void testDeleteTransaction_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).delete(transaction);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(1L));
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(1L);
        });
        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(1L));
    }
}
