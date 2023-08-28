package org.orquestador.credit.entities;

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
@Table(name = "credit")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Credit extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT AUTO_INCREMENT")
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "cliente", example = "John")
    @NotBlank(message = "El cliente no puede estar en blanco")
    private String clientName;

    @Schema(description = "cliente", example = "John")
    @NotBlank(message = "El cliente no puede estar en blanco")
    private String clientLastName;

    @Schema(description = "cliente", example = "John")
    @NotBlank(message = "El cliente no puede estar en blanco")
    private String clientMaternalLastName;

    @Schema(description = "Relacion Invex", example = "John")
    @NotBlank(message = "La relacion invex no puede estar en blanco")
    private String relationInvex;

    @Schema(description = "Razon social", example = "John")
    @NotBlank(message = "La razon social no puede estar en blanco")
    private String businessName;

    @Schema(description = "RFC", example = "John")
    @NotBlank(message = "El RFC no puede estar en blanco")
    private String rfc;

    @Schema(description = "Fecha Constitucion", example = "John")
    @NotBlank(message = "La fecha de constitucion no puede estar en blanco")
    private String constitucionDate;

    @Schema(description = "Grupo empresarial", example = "John")
    @NotBlank(message = "El grupo empresarial no puede estar en blanco")
    private String businessGroup;

    @Schema(description = "Calle", example = "John")
    @NotBlank(message = "La calle no puede estar en blanco")
    private String street;

    @Schema(description = "Numero exterior", example = "John")
    @NotBlank(message = "El numero exterior no puede estar en blanco")
    private String outdoorNumber;

    @Schema(description = "Numero interior", example = "John")
    @NotBlank(message = "El numero interior no puede estar en blanco")
    private String internalNumber;

    @Schema(description = "Codigo postal", example = "John")
    @NotBlank(message = "El codigo postal no puede estar en blanco")
    private String postalCode;

    @Schema(description = "Colonia", example = "John")
    @NotBlank(message = "La colonia no puede estar en blanco")
    private String suburb;

    @Schema(description = "Municipio", example = "John")
    @NotBlank(message = "El municipio no puede estar en blanco")
    private String town;

    @Schema(description = "Estado", example = "John")
    @NotBlank(message = "El estado no puede estar en blanco")
    private String state;

    @Schema(description = "Sector", example = "John")
    @NotBlank(message = "El sector no puede estar en blanco")
    private String economicSector;

    @Schema(description = "subsector", example = "John")
    @NotBlank(message = "El subsector no puede estar en blanco")
    private String economicSubsector;

    @Schema(description = "Actividad Economica", example = "John")
    @NotBlank(message = "La actividad economica no puede estar en blanco")
    private String activityEconomic;

    // Constructor, getters y setters
}
