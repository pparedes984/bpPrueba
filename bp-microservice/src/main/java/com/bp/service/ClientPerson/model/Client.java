package com.bp.service.ClientPerson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "clientId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Person {

    @Column(name="password" , nullable = false)
    private String password;

    @Column(name="state", nullable = false)
    private clientState state;
}


