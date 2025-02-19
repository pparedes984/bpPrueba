package com.bp.service.transaction.controller;

import com.bp.service.transaction.model.dto.TransactionDTO;
import com.bp.service.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        transactionDTO = new TransactionDTO(1L, LocalDateTime.now(), "CREDITO", 500.0, 1500.0, 1L);
    }

    @Test
    void getAllTransactions_ShouldReturnListOfTransactions() {
        List<TransactionDTO> transactions = Arrays.asList(transactionDTO);
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<List<TransactionDTO>> response = transactionController.getAllTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        when(transactionService.getTransactionById(1L)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.getTransactionById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction() {
        when(transactionService.saveTransaction(eq(1L), any(TransactionDTO.class))).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.createTransaction(1L, transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        when(transactionService.updateTransaction(eq(1L), any(TransactionDTO.class))).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.updateTransaction(1L, transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService, times(1)).deleteTransaction(1L);
    }
}

