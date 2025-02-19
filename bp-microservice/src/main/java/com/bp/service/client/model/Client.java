package com.bp.service.client.model;

import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private clientState state;
}


