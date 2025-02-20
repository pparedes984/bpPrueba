package com.bp.service.client.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends PersonDTO {
    @NotNull(message = "El estado del cliente no puede ser nulo")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private String state;
    @NotNull(message = "La contrseña del cliente no puede ser nulo")
    private String password;
}
