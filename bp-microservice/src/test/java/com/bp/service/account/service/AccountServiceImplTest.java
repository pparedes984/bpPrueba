package com.bp.service.account.service;
import com.bp.service.account.model.Account;
import com.bp.service.account.model.accountState;
import com.bp.service.account.model.accountType;
import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.repository.AccountRepository;
import com.bp.service.account.service.impl.AccountServiceImpl;
import com.bp.service.client.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    void testGetAllAccounts() {

        Account account = new Account(1L, "12345", accountType.AHORROS, 1000.0, accountState.ACTIVA, 1L);
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountDTO> result = accountService.getAllAccounts();

        assertEquals(1, result.size());
        assertEquals("12345", result.get(0).getAccountNumber());
    }

    @Test
    void testGetAccountById() {
        Account account = new Account(1L, "12345", accountType.AHORROS, 1000.0, accountState.ACTIVA, 1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDTO result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals("12345", result.getAccountNumber());
    }

    @Test
    void testUpdateAccount() {
        AccountDTO accountDTO = new AccountDTO(1L, "12345", "AHORROS", 1000.0, "ACTIVA", 1L);
        Account existingAccount = new Account(1L, "12345", accountType.AHORROS, 1000.0, accountState.ACTIVA, 1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        AccountDTO result = accountService.updateAccount(1L, accountDTO);

        assertEquals("12345", result.getAccountNumber());
    }

    @Test
    void testDeleteAccount() {
        Account account = new Account(1L, "12345", accountType.AHORROS, 1000.0, accountState.ACTIVA, 1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    void testSaveAccount() {
        Long clientId = 1L;
        AccountDTO accountDTO = new AccountDTO(null, "123456789", "AHORROS", 1000.00, "ACTIVA", clientId);

        doReturn(true).when(clientService).verifyClientExists(clientId);

        // Simulación de cuenta
        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountType.valueOf(accountDTO.getAccountType().toUpperCase()));
        account.setBalance(accountDTO.getBalance());
        account.setState(accountState.valueOf(accountDTO.getState().toUpperCase()));
        account.setClientId(accountDTO.getClientId());

        // Mocking repository save
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        AccountDTO result = accountService.saveAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(accountDTO.getAccountNumber(), result.getAccountNumber());
        assertEquals(accountDTO.getAccountType().toUpperCase(), result.getAccountType());
        assertEquals(accountDTO.getBalance(), result.getBalance());
        assertEquals(accountDTO.getState().toUpperCase(), result.getState());
        assertEquals(accountDTO.getClientId(), result.getClientId());

        // Verificar interacción
        verify(clientService, times(1)).verifyClientExists(clientId);
    }
}

