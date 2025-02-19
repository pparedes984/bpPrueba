package com.bp.service.account.controller;

import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testGetAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(List.of(new AccountDTO(1L, "12345", "AHORROS", 1000.0, "ACTIVA", 1L)));

        ResponseEntity<List<AccountDTO>> response = accountController.getAllAccounts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("12345", response.getBody().get(0).getAccountNumber());
    }

    @Test
    void testGetAccountById() {
        AccountDTO accountDTO = new AccountDTO(1L, "12345", "AHORROS", 1000.0, "ACTIVA", 1L);
        when(accountService.getAccountById(1L)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccountById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("12345", response.getBody().getAccountNumber());
    }

    @Test
    void testCreateAccount() {
        AccountDTO accountDTO = new AccountDTO(1L, "12345", "AHORROS", 1000.0, "ACTIVA", 1L);
        when(accountService.saveAccount(accountDTO)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.createAccount(accountDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("12345", response.getBody().getAccountNumber());
    }

    @Test
    void testUpdateAccount() {
        AccountDTO accountDTO = new AccountDTO(1L, "12345", "AHORROS", 1000.0, "ACTIVA", 1L);
        when(accountService.updateAccount(1L, accountDTO)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.updateAccount(1L, accountDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("12345", response.getBody().getAccountNumber());
    }

    @Test
    void testDeleteAccount() {
        doNothing().when(accountService).deleteAccount(1L);

        ResponseEntity<Void> response = accountController.deleteAccount(1L);

        assertEquals(204, response.getStatusCodeValue());
    }
}


