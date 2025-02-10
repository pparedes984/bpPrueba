package com.bp.service.AccountTransaction.repository;

import com.bp.service.AccountTransaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
