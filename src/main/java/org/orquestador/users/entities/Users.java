package org.orquestador.users.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.orquestador.roles.entities.Rol;

@Data
@Table(name = "users")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Rol rol;
}
