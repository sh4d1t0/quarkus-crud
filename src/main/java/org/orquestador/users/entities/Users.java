package org.orquestador.users.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
//@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "name", example = "John")
    @NotBlank(message = "Name no puede estar en blanco")
    private String name;

    @Schema(description = "email", example = "john@example.com")
    @NotBlank(message = "Email no puede estar en blanco")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(description = "password", example = "Unpassword1")
    @NotBlank(message = "Password no puede estar en blanco")
    private String password;
}
