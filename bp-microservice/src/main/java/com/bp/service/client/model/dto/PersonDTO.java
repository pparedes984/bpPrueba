package com.bp.service.client.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Long id;
    @NotNull(message = "El nombre del cliente no puede ser nulo")
    private String name;
    @NotNull(message = "El genero del clienteno puede ser nulo")
    @Pattern(regexp = "MASCULINO|FEMENINO", message = "El genero debe ser MASCULINO o FEMENINO")
    private String gender;  // Se usa String en lugar de Enum
    @NotNull(message = "La edad del cliente no puede ser nulo")
    @Positive(message = "La edad deL cliente debe ser positivo")
    private int age;
    @NotNull(message = "El dni del cliente no puede ser nulo")
    private String dni;
    @NotNull(message = "La direccion del cliente no puede ser nulo")
    private String address;
    @NotNull(message = "El telefono del cliente no puede ser nulo")
    private String telephone;
}
