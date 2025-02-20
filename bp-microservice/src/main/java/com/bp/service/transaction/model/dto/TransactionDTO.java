package com.bp.service.transaction.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "El tipo de transaccion no puede ser nulo")
    @Pattern(regexp = "DEBITO|CREDITO", message = "El tipo de transaccion debe ser DEBITO o CREDITO")
    private String transactionType;
    @NotNull(message = "El valor de la transaccion no puede ser nulo")
    @Positive(message = "El valor de la transaccion debe ser positivo")
    private Double value;
    private Double balance;
    @NotNull(message = "El ID de la cuenta no puede ser nulo")
    private Long accountId;
}
