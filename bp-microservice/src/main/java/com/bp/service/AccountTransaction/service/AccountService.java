package com.bp.service.AccountTransaction.service;

import com.bp.service.AccountTransaction.model.Account;
import com.bp.service.AccountTransaction.repository.AccountRepository;
import com.bp.service.ClientPerson.service.ClientService;
import com.bp.service.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private static final Logger logger = LogManager.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    public List<Account> getAllAccounts() {
        logger.info("Obteniendo todas las cuentas");
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        logger.info("Obteniendo cuenta con ID: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cuenta con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cuenta no encontrada");
                });
    }

    public Account saveAccount(Account account) {
        logger.info("Creando cuenta: {}", account.getAccountNumber());
        if(clientService.verifyClientExists(account.getClientId()).equals(false)) {
            throw new RuntimeException("Cliente no encontrado para crear cuenta");
        }
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account accountDetails) {
        logger.info("Actualizando cuenta con ID: {}", id);
        Account account =  accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        if (accountDetails.getAccountNumber() != null)
            account.setAccountNumber(accountDetails.getAccountNumber());
        if (accountDetails.getAccountType() != null)
            account.setAccountType(accountDetails.getAccountType());
        if (accountDetails.getOpeningBalance() != null)
            account.setOpeningBalance(accountDetails.getOpeningBalance());
        if (accountDetails.getState() != null)
            account.setState(accountDetails.getState());
        return accountRepository.save(account);

    }

    public void deleteAccount(Long id) {
        logger.info("Eliminando cuenta con ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cuenta con ID {} no encontrada", id);
                    return new ResourceNotFoundException("Cuenta no encontrada");
                });

        accountRepository.delete(account);
    }
}
