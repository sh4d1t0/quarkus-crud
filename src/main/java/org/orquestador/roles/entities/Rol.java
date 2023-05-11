package org.orquestador.roles.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "name", example = "admin")
    @NotBlank(message = "Name no puede estar en blanco")
    private String name;

    @Schema(name = "description", example = "Administrador")
    @NotBlank(message = "Description no puede estar en blanco")
    private String description;
}
