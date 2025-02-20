package com.bp.service.account.service.impl;

import com.bp.service.account.model.Account;
import com.bp.service.account.model.AccountState;
import com.bp.service.account.model.AccountType;
import com.bp.service.account.model.dto.AccountDTO;
import com.bp.service.account.repository.AccountRepository;
import com.bp.service.account.service.AccountService;
import com.bp.service.client.service.ClientService;
import com.bp.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ClientService clientService;

    @Transactional
    public List<AccountDTO> getAllAccounts() {
        log.info("Obteniendo todas las cuentas");
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountDTO getAccountById(Long id) {
        log.info("Obteniendo cuenta con ID: {}", id);
        Account account =  accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cuenta con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cuenta no encontrada");
                });
        return convertToDTO(account);
    }

    @Transactional
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        log.info("Creando cuenta: {}", accountDTO.getAccountNumber());
        Account account = convertToEntity(accountDTO);
        if(!clientService.verifyClientExists(accountDTO.getClientId())) {
            throw new RuntimeException("Cliente no encontrado para crear cuenta");
        }
        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }

    @Transactional
    public AccountDTO updateAccount(Long id, AccountDTO accountDetails) {
        log.info("Actualizando cuenta con ID: {}", id);
        Account account =  accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        if (accountDetails.getAccountNumber() != null)
            account.setAccountNumber(accountDetails.getAccountNumber());
        if (accountDetails.getAccountType() != null)
            account.setAccountType(AccountType.valueOf(accountDetails.getAccountType().toUpperCase()));
        if (accountDetails.getBalance() != null)
            account.setBalance(accountDetails.getBalance());
        if (accountDetails.getState() != null)
            account.setState(AccountState.valueOf(accountDetails.getState().toUpperCase()));
        Account updatedAccount = accountRepository.save(account);
        return convertToDTO(updatedAccount);

    }

    @Transactional
    public void deleteAccount(Long id) {
        log.info("Eliminando cuenta con ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cuenta con ID {} no encontrada", id);
                    return new ResourceNotFoundException("Cuenta no encontrada");
                });

        accountRepository.delete(account);
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

    private Account convertToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(AccountType.valueOf(accountDTO.getAccountType().toUpperCase()));
        account.setBalance(accountDTO.getBalance());
        account.setState(AccountState.valueOf(accountDTO.getState().toUpperCase()));
        account.setClientId(accountDTO.getClientId());
        return account;
    }
}
