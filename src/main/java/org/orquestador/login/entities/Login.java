package org.orquestador.login.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Login {
    @NotBlank(message = "Email no puede estar en blanco")
    @Email(message = "Email debe ser una dirección de correo electrónico válida")
    private String email;

    @NotBlank(message = "Password no puede estar en blanco")
    private String password;
}
