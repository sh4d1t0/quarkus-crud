package org.orquestador.users.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    // Password filed is not include for security
    private String roleName;

    // Constructor, getters y setters
}
