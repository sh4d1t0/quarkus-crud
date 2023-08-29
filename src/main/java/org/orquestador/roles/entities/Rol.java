package org.orquestador.roles.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rol extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT AUTO_INCREMENT")
    @Schema(hidden = true)
    private Long id;

    @Schema(name = "name", example = "admin")
    @NotBlank(message = "Name cannot be null")
    private String name;

    @Schema(name = "description", example = "Administrador")
    @NotBlank(message = "Description cannot be null")
    private String description;
}
