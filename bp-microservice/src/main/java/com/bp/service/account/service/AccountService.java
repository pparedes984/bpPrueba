package com.bp.service.account.service;

import com.bp.service.account.model.dto.AccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<AccountDTO> getAllAccounts();
    AccountDTO getAccountById(Long id);
    AccountDTO saveAccount(AccountDTO accountDTO);
    AccountDTO updateAccount(Long id, AccountDTO accountDetails);
    void deleteAccount(Long id);
}
