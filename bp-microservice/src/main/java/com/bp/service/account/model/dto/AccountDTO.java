package com.bp.service.account.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    @NotNull(message = "El numero de cuenta no puede ser nulo")
    private String accountNumber;
    @NotNull(message = "El tipo de cuenta no puede ser nulo")
    @Pattern(regexp = "AHORROS|CORRIENTE", message = "El tipo de cuenta debe ser AHORROS o CORRIENTE")
    private String accountType;
    @NotNull(message = "El valor de la cuenta no puede ser nulo")
    @Positive(message = "El valor deL balance debe ser positivo")
    private Double balance;
    @NotNull(message = "El estado de la cuenta no puede ser nulo")
    @Pattern(regexp = "ACTIVA|INACTIVA", message = "El tipo de cuenta debe ser ACTIVA o INACTIVA")
    private String state;
    @NotNull(message = "El ID del cliente no puede ser nulo")
    private Long clientId;
}
