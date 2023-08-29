package org.orquestador.users.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.orquestador.roles.entities.Rol;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Users extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT AUTO_INCREMENT")
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "name", example = "John")
    @NotBlank(message = "Name cannot be null")
    private String name;

    @Schema(description = "email", example = "john@example.com")
    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(description = "password", example = "Unpassword1")
    @NotBlank(message = "Password cannot be")
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id", columnDefinition = "INT")
    private Rol rol;

    // Constructor, getters y setters
}
