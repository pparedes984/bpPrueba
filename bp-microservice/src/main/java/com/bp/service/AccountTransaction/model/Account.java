package com.bp.service.AccountTransaction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "accountType")
    private String accountType;

    @Column(name = "openingBalance")
    private Double openingBalance;

    @Column(name = "state")
    private accountState state;

    @Column(name = "clientId")
    private Long clientId;
}