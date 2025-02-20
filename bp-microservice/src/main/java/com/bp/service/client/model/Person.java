package com.bp.service.client.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="gender", nullable = false)
    private PersonGender gender;


    @Column(name="age", nullable = false)
    private int age;

    @Column(name="dni", nullable = false, unique = true)
    private String dni;

    @Column(name="adress")
    private String address;
    @Column(name="telephone")
    private String telephone;
}
