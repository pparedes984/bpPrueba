package com.bp.service.client.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Long id;
    private String name;
    private String gender;  // Se usa String en lugar de Enum
    private int age;
    private String dni;
    private String address;
    private String telephone;
}
