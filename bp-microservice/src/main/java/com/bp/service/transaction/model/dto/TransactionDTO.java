package com.bp.service.transaction.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private LocalDateTime date;
    private String transactionType;
    private Double value;
    private Double balance;
    private Long accountId;
}
