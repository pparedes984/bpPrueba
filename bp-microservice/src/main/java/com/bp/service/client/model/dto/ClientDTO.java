package com.bp.service.client.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends PersonDTO {
    private String state;
    private String password;
}
