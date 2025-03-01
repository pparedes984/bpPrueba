package com.bp.service.account.model;

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
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AccountState state;

    @Column(name = "clientId")
    private Long clientId;
}