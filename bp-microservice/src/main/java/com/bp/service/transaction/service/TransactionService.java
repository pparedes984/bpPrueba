package com.bp.service.transaction.service;

import com.bp.service.transaction.model.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    TransactionDTO getTransactionById(Long id);
    TransactionDTO saveTransaction(Long accountId, TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDetails);
    void deleteTransaction(Long id);
}