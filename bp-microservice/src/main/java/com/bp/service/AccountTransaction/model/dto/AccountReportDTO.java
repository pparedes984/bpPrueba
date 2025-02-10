package com.bp.service.AccountTransaction.model.dto;


import com.bp.service.AccountTransaction.model.Account;
import com.bp.service.AccountTransaction.model.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class AccountReportDTO {

    private Long accountId;
    private String accountNumber;
    private Double currentBalance;
    private String accountType;
    private List<Transaction> transactions;

    public AccountReportDTO(Account account, List<Transaction> transactions) {
        this.accountId = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.currentBalance = account.getOpeningBalance();
        this.transactions = transactions;
    }
    // toString (opcional)
    @Override
    public String toString() {
        return "AccountReportDTO{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", currentBalance=" + currentBalance +
                ", transactions=" + transactions +
                '}';
    }
}
