package org.orquestador.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
//@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users extends PanacheEntityBase {
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
